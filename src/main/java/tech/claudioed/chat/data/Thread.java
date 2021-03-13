package tech.claudioed.chat.data;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.format.SnakeCase;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.templates.annotations.Column;
import io.vertx.sqlclient.templates.annotations.ParametersMapped;
import io.vertx.sqlclient.templates.annotations.RowMapped;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@DataObject(generateConverter = true)
@RowMapped(formatter = SnakeCase.class)
@ParametersMapped(formatter = SnakeCase.class)
public class Thread {

  private String id;

  private String topic;

  private List<String> usersInThread;

  private String users;

  private String owner;

  public Thread(){}

  public Thread(JsonObject json) {
    ThreadConverter.fromJson(json,this);
  }

  private Thread(String id, String topic, List<String> usersInThread, String users, String owner) {
    this.id = id;
    this.topic = topic;
    this.usersInThread = usersInThread;
    this.users = users;
    this.owner = owner;
  }

  public static Thread createNew(String id, String topic, List<String> usersInThread, String owner) {
    return new Thread(id, topic, usersInThread, String.join(",", usersInThread), owner);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public List<String> getUsersInThread() {
    return usersInThread;
  }

  public void setUsersInThread(List<String> usersInThread) {
    this.usersInThread = usersInThread;
  }

  public String getUsers() {
    return users;
  }

  public void setUsers(String users) {
    this.users = users;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public boolean checkUserInThread(String user){
    return Arrays.stream(this.users.split(",")).collect(Collectors.toSet()).contains(user);
  }

}
