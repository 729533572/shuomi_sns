
package framework.data;

public class Account {

    public static final int NOT_SAVED = -1;

    private int id;
    private String username;
    private String password;
    private String email;
    private String token;

    public Account(int id, String username, String password) {
        this(id, username, password, null);
    }

    public Account(int id, String username, String password, String email) {
        setId(id);
        setUsername(username);
        setPassword(password);
        setEmail(email);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
