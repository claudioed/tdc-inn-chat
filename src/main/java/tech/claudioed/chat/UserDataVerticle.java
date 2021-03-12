package tech.claudioed.chat;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.ext.web.client.WebClient;
import tech.claudioed.chat.data.User;
import tech.claudioed.chat.data.UserId;

public class UserDataVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    WebClient client = WebClient.create(this.vertx);
    this.vertx.eventBus().consumer("request.user.data",handler ->{
      var userId = Json.decodeValue(handler.body().toString(), UserId.class);
      var url = String.format("%s/%s","http://localhost:9090/users",userId.getId());
      client.getAbs(url).send().onSuccess(userData -> {
        var user = Json.decodeValue(userData.body(), User.class);
        if(!user.isBlocked()){
          handler.reply(Json.encode(user));
        }else{
          handler.fail(1000,"user is blocked");
        }
      }).onFailure(err -> {
        handler.fail(1001,"user is blocked");
      });
    });
  }

}
