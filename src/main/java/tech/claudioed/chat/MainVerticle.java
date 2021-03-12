package tech.claudioed.chat;

import io.grpc.ManagedChannel;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.grpc.VertxChannelBuilder;
import io.vertx.micrometer.PrometheusScrapingHandler;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import tech.claudioed.chat.handlers.CreateMessageHandler;
import tech.claudioed.chat.handlers.CreateThreadHandler;
import tech.claudioed.chat.infra.DatasourceConfig;
import tech.claudioed.chat.infra.JwtConfig;
import tech.claudioed.chat.infra.OpenAPIConfig;
import tech.claudioed.chat.infra.PoliceOfficerConfig;
import tech.claudioed.police.man.grpc.PoliceOfficerGrpc;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainVerticle extends AbstractVerticle {

  Logger LOG = LoggerFactory.getLogger(this.getClass());

  private DatasourceConfig datasourceConfig;

  private PoliceOfficerConfig policeOfficerConfig;

  private OpenAPIConfig apiConfig;

  private PgPool client;

  private PoliceOfficerGrpc.PoliceOfficerFutureStub policeOfficer;

  @Override
  public void start(Promise<Void> startPromise) {
    initConfig().result();
    this.datasourceConfig = new DatasourceConfig(config().getJsonObject("database", new JsonObject()));
    this.policeOfficerConfig = new PoliceOfficerConfig(config().getJsonObject("police-officer", new JsonObject()));
    this.apiConfig = new OpenAPIConfig(config().getJsonObject("openAPI", new JsonObject()));

    ManagedChannel channel = VertxChannelBuilder
      .forAddress(vertx, this.policeOfficerConfig.getHost(), this.policeOfficerConfig.getPort())
      .usePlaintext()
      .build();

    this.policeOfficer = PoliceOfficerGrpc.newFutureStub(channel);

    PgConnectOptions connectOptions = new PgConnectOptions()
      .setPort(this.datasourceConfig.getPort())
      .setHost(this.datasourceConfig.getHost())
      .setDatabase(this.datasourceConfig.getDatabase())
      .setUser(this.datasourceConfig.getUser())
      .setPassword(this.datasourceConfig.getPassword());

    PoolOptions poolOptions = new PoolOptions()
      .setMaxSize(5);

    client = PgPool.pool(this.vertx, connectOptions, poolOptions);

    CompositeFuture.all(updateDB(),jwtAuth().compose(this::createRouter).compose(this::startServer).onComplete(res ->{
      LOG.info("Chat started!");
      startPromise.complete();
    }));
  }

  private Future<HttpServer> startServer(Router router){
    var server = vertx.createHttpServer(new HttpServerOptions().setPort(8888))
      .requestHandler(router);
    return server.listen();
  }

  private Future<Router> createRouter(JWTAuth jwtAuth) {
    LOG.info("OAS: " + this.apiConfig.getPath());
    return RouterBuilder.create(vertx, apiConfig.getPath()).compose(builder -> {
      builder.securityHandler("openId",JWTAuthHandler.create(jwtAuth));
      builder.operation("create-thread").handler(new CreateThreadHandler(this.client,this.vertx));
      builder.operation("create-messages").handler(new CreateMessageHandler(this.client,this.vertx,this.policeOfficer));
      var router = builder.createRouter();
      router.errorHandler(400, ctx -> {
        LOG.error("Bad Request", ctx.failure());
      });
      // Infra endpoints
      router.route("/metrics").handler(PrometheusScrapingHandler.create());
      var healthCheckHandler = HealthCheckHandler.create(this.vertx);
      healthCheckHandler.register("database",
        result -> client.getConnection(connection -> {
          if (connection.failed()) {
            result.fail(connection.cause());
          } else {
            connection.result().close();
            result.complete(Status.OK());
          }
        }));
      router.route("/health").handler(healthCheckHandler);
      return Future.succeededFuture(router);
    });
  }

  private Future<Void> updateDB(){
    return Future.future(ft ->{
      Configuration config = new FluentConfiguration()
        .dataSource(datasourceConfig.jdbcUrl(), datasourceConfig.getUser(), datasourceConfig.getPassword());
      Flyway flyway = new Flyway(config);
      flyway.migrate();
      ft.complete();
    });
  }

  private Future<JWTAuth> jwtAuth() {
    var promise = Promise.<JWTAuth>promise();
    var jwtConfig = new JwtConfig(config().getJsonObject("jwt", new JsonObject()));
    var jwksUri = jwtConfig.jwks();
    var webClient = WebClient.create(this.vertx);
    // fetch JWKS from `/certs` endpoint
    webClient.get(jwksUri.getPort(), jwksUri.getHost(), jwksUri.getPath())
      .as(BodyCodec.jsonObject())
      .send(ar -> {
        if (!ar.succeeded()) {
          promise.fail(String.format("Could not fetch JWKS from URI: %s", jwksUri));
          return;
        }
        var response = ar.result();
        var jwksResponse = response.body();
        var keys = jwksResponse.getJsonArray("keys");
        // Configure JWT validation options
        var jwtOptions = new JWTOptions();
        jwtOptions.setIssuer(jwtConfig.issuer());
        // extract JWKS from keys array
        var jwks = ((List<Object>) keys.getList()).stream()
          .map(o -> new JsonObject((Map<String, Object>) o))
          .collect(Collectors.toList());
        // configure JWTAuth
        var jwtAuthOptions = new JWTAuthOptions();
        jwtAuthOptions.setJwks(jwks);
        jwtAuthOptions.setJWTOptions(jwtOptions);
        // TODO authorizer
        JWTAuth jwtAuth = JWTAuth.create(vertx, jwtAuthOptions);
        promise.complete(jwtAuth);
      });

    return promise.future().compose(Future::succeededFuture);
  }

  public Future<JsonObject> initConfig(){
    var configPath = System.getenv("VERTX_CONFIG_PATH");
    LOG.info("Config Path: " + configPath);
    ConfigStoreOptions fileStore = new ConfigStoreOptions()
      .setType("file")
      .setConfig(new JsonObject().put("path", configPath));
    ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(fileStore);
    ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
    return retriever.getConfig();
  }

}


