package tech.claudioed.chat.data;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.format.SnakeCase;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.templates.annotations.Column;
import io.vertx.sqlclient.templates.annotations.ParametersMapped;
import io.vertx.sqlclient.templates.annotations.RowMapped;

@DataObject(generateConverter = true)
@RowMapped(formatter = SnakeCase.class)
@ParametersMapped(formatter = SnakeCase.class)
public class Message {

  private String id;

  @Column(name = "thread_id")
  private String threadId;

  @Column(name = "user_id")
  private String userId;

  private String message;

  Message(){}

  public Message(JsonObject json) {
    MessageConverter.fromJson(json,this);
  }

  private Message(String id, String threadId, String userId, String message) {
    this.id = id;
    this.threadId = threadId;
    this.userId = userId;
    this.message = message;
  }

  public static Message createNew(String id, String threadId, String userId, String message) {
    return new Message(id, threadId, userId, message);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    MessageConverter.toJson(this, json);
    return json;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getThreadId() {
    return threadId;
  }

  public void setThreadId(String threadId) {
    this.threadId = threadId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
