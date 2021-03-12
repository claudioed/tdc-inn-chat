package tech.claudioed.chat.data;

import java.util.List;

public class NewThread {

  private String topic;

  private List<String> users;

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

}
