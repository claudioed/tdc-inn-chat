package tech.claudioed.chat.data;

public class User {

  private String id;

  private String firstName;

  private String lastName;

  private String email;

  private Boolean emailVerified;

  private Boolean blocked;

  public User() {
  }

  private User(String id, String firstName, String lastName, String email,
    Boolean emailVerified,Boolean blocked) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.emailVerified = emailVerified;
    this.blocked = blocked;
  }

  public Boolean isBlocked(){
    return this.blocked;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Boolean getEmailVerified() {
    return emailVerified;
  }

  public void setEmailVerified(Boolean emailVerified) {
    this.emailVerified = emailVerified;
  }

  public Boolean getBlocked() {
    return blocked;
  }

  public void setBlocked(Boolean blocked) {
    this.blocked = blocked;
  }

}
