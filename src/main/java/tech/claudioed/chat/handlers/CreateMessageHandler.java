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
import tech.claudioed.police.man.grpc.MessageData;
import tech.claudioed.police.man.grpc.PoliceOfficerGrpc;

import java.util.Collections;
import java.util.UUID;

public class CreateMessageHandler implements Handler<RoutingContext> {

  private static final String NEW_MESSAGE_METRIC = "new_messages";

  private final Logger LOG = LoggerFactory.getLogger(this.getClass());

  private final PgPool dbClient;

  private final Vertx vertx;

  private final PoliceOfficerGrpc.PoliceOfficerFutureStub policeOfficer;

  private final DeliveryOptions deliveryOptions = new DeliveryOptions().setTracingPolicy(TracingPolicy.ALWAYS);

  private final MeterRegistry meterRegistry = BackendRegistries.getDefaultNow();

  public CreateMessageHandler(PgPool dbClient, Vertx vertx, PoliceOfficerGrpc.PoliceOfficerFutureStub policeOfficer) {
    this.dbClient = dbClient;
    this.vertx = vertx;
    this.policeOfficer = policeOfficer;
  }

  @Override
  public void handle(RoutingContext rc) {

    SqlTemplate<Message, SqlResult<Void>> insertTemplate = SqlTemplate
      .forUpdate(this.dbClient,
        "INSERT INTO messages (id, thread_id, user_id, message)  VALUES ( #{id}, #{thread_id}, #{user_id}, #{message} )")
      .mapFrom(
        MessageParametersMapper.INSTANCE);
    var userId = rc.user().principal().getString("sub");
    var threadId = rc.pathParam("id");
    this.vertx.eventBus().request("request.user.data", Json.encode(new UserId(userId)),this.deliveryOptions).onSuccess(user -> {
      SqlTemplate.forQuery(this.dbClient,"SELECT * FROM threads WHERE id=#{id}").mapTo(ThreadRowMapper.INSTANCE).execute(
        Collections.singletonMap("id",threadId)).onSuccess(rows ->{
        if(rows.iterator().hasNext()){
          var thread = rows.iterator().next();
          if (new UserIsAllowedToPostInThread(userId).isSatisfied(thread)){
            var newMessage = Json.decodeValue(rc.getBody(), NewMessage.class);
            var message = Message.createNew(UUID.randomUUID().toString(), threadId, userId, newMessage.getContent());
            insertTemplate.execute(message).onSuccess(result -> {
              if (result.rowCount() > 0) {
                var data = MessageData.newBuilder()
                  .setMessageId(message.getId()).setContent(message.getMessage()).setUserId(message.getUserId()).setThreadId(message.getThreadId()).build();
                this.policeOfficer.registry(data);
                this.meterRegistry.counter(NEW_MESSAGE_METRIC,"user_id",message.getUserId()).increment();
                rc.response().putHeader("content-type", "application/json; charset=utf-8")
                  .setStatusCode(201).end(message.toJson().encode());
              } else {
                LOG.error("No rows affected");
                rc.response().putHeader("content-type", "application/json; charset=utf-8")
                  .setStatusCode(500).end(new JsonObject().encode());
              }
            }).onFailure(err -> {
              LOG.error("Error to create use in database " + err);
              rc.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(400)
                .end();
            });
          }else{
            rc.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(403).end("Not Authorized");
          }
        }else{
          rc.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(404).end("");
        }
      }).onFailure(err ->{
        rc.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(404).end();
      });
    }).onFailure(err -> {
      rc.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(400).end();
    });
  }

}
