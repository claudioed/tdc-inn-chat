package tech.claudioed.chat;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import tech.claudioed.chat.data.User;
import tech.claudioed.chat.data.UserId;
import tech.claudioed.chat.infra.UserServiceConfig;

public class UserDataVerticle extends AbstractVerticle {

  Logger LOG = LoggerFactory.getLogger(this.getClass());

  private UserServiceConfig userServiceConfig;

  @Override
  public void start(Promise<Void> startPromise) {
    WebClient client = WebClient.create(this.vertx);
    initConfig().compose(this::userServiceConfig).onSuccess(userServiceConfig ->{
      this.vertx.eventBus().consumer("request.user.data",handler ->{
        var userId = Json.decodeValue(handler.body().toString(), UserId.class);
        var url = String.format("%s:%d/users/%s",userServiceConfig.getHost(),userServiceConfig.getPort(),userId.getId());
        client.getAbs(url).send().onSuccess(userData -> {
          var user = Json.decodeValue(userData.body(), User.class);
          if(!user.isBlocked()){
            LOG.info("User allowed to post");
            handler.reply(Json.encode(user));
          }else{
            LOG.error("User is blocked. Check with administrator");
            handler.fail(1000,"user is blocked");
          }
        }).onFailure(err -> {
          LOG.error("Error to fetch user data. ",err);
          handler.fail(1001,"user is blocked");
        });
      });
    }).onFailure(err ->{
      LOG.error("Error on configure verticle",err);
    });
  }

  public Future<UserServiceConfig> userServiceConfig(JsonObject cfg){
    this.userServiceConfig = new UserServiceConfig(cfg.getJsonObject("user-service", new JsonObject()));
    return Future.succeededFuture(this.userServiceConfig);
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
