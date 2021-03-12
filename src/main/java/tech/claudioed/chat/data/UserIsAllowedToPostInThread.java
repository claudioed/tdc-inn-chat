package tech.claudioed.chat.data;

public class UserIsAllowedToPostInThread {

  private final String userId;

  public UserIsAllowedToPostInThread(String userId) {
    this.userId = userId;
  }

  public Boolean isSatisfied(Thread thread){
    return this.userId.equals(thread.getOwner()) || thread.getUsersInThread().contains(userId);
  }

}
