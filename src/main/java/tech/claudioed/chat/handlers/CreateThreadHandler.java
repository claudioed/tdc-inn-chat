package tech.claudioed.chat.handlers;

import io.micrometer.core.instrument.MeterRegistry;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.tracing.TracingPolicy;
import io.vertx.ext.web.RoutingContext;
import io.vertx.micrometer.backends.BackendRegistries;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.SqlResult;
import io.vertx.sqlclient.templates.SqlTemplate;
import tech.claudioed.chat.data.*;
import tech.claudioed.chat.data.Thread;

import java.util.UUID;

public class CreateThreadHandler implements Handler<RoutingContext> {

  private static final String NEW_THREAD_METRIC = "new_threads";

  private final Logger LOG = LoggerFactory.getLogger(this.getClass());

  private final PgPool dbClient;

  private final Vertx vertx;

  private final DeliveryOptions deliveryOptions = new DeliveryOptions().setTracingPolicy(TracingPolicy.ALWAYS);

  private final MeterRegistry meterRegistry = BackendRegistries.getDefaultNow();

  public CreateThreadHandler(PgPool dbClient, Vertx vertx) {
    this.dbClient = dbClient;
    this.vertx = vertx;
  }

  @Override
  public void handle(RoutingContext rc) {
    var userId = rc.user().principal().getString("sub");
    this.vertx.eventBus().request("request.user.data", Json.encode(new UserId(userId)),this.deliveryOptions)
      .onSuccess(data -> {
        var user = Json.decodeValue(data.body().toString(), User.class);
        SqlTemplate<Thread, SqlResult<Void>> insertTemplate = SqlTemplate
          .forUpdate(this.dbClient,
            "INSERT INTO threads (id, topic, owner, users)  VALUES ( #{id}, #{topic}, #{owner}, #{users} )")
          .mapFrom(
            ThreadParametersMapper.INSTANCE);
        var newThread = Json.decodeValue(rc.getBody(), NewThread.class);
        var thread = Thread.createNew(UUID.randomUUID().toString(),newThread.getTopic(),newThread.getUsers(),userId);

        insertTemplate.execute(thread).onSuccess(result -> {
          if (result.rowCount() > 0) {
            var threadCreated = ThreadCreated.createNew(thread.getId(),thread.getTopic(),thread.getUsersInThread(),thread.getOwner());
            this.meterRegistry.counter(NEW_THREAD_METRIC,"owner_id",threadCreated.getOwner()).increment();
            rc.response().putHeader("content-type", "application/json; charset=utf-8")
              .setStatusCode(201).end(Json.encode(threadCreated));
          } else {
            LOG.error("No rows affected");
            rc.response().putHeader("content-type", "application/json; charset=utf-8")
              .setStatusCode(500).end(new JsonObject().encode());
          }
        }).onFailure(err -> {
          LOG.error("Error to thread in database " + err);
          rc.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(400)
            .end();
        });
      })
      .onFailure(err -> {
        rc.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(400).end();
      });
  }
}
