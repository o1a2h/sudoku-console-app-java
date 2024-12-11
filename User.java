public class User {
  
  String public_username;

  String username;
  String password;

  String username_hashed;
  String password_hashed;

  int score = 0;

  public User() {

  }

  public User(String username, String public_username, String password) {
    this.public_username = public_username;
    
    this.username = username;
    this.password = password;

    this.username_hashed = Cypher.SHA256(username);
    this.password_hashed = Cypher.SHA256(password);
  }

  public User(String username, String public_username, String password, int score) {
    this.public_username = public_username;
    
    this.username = username;
    this.password = password;

    this.username_hashed = Cypher.SHA256(username);
    this.password_hashed = Cypher.SHA256(password);

    this.score = score;
  }

}
