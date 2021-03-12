package tech.claudioed.chat.data;

import java.util.List;

public class ThreadCreated {

  private String id;

  private String topic;

  private List<String> users;

  private String owner;

  public ThreadCreated(){}

  private ThreadCreated(String id, String topic, List<String> users, String owner) {
    this.id = id;
    this.topic = topic;
    this.users = users;
    this.owner = owner;
  }

  public static ThreadCreated createNew(String id, String topic, List<String> users, String owner) {
    return new ThreadCreated(id, topic, users,owner);
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

  public List<String> getUsers() {
    return users;
  }

  public void setUsers(List<String> users) {
    this.users = users;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }
}
