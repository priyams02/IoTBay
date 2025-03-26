import java.io.Serializable;

public class admin implements Serializable {
    private String username;
    private String password;
    private String role;

    // No-argument constructor
    public admin() {}

    // Getter and Setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and Setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and Setter for role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Method to check if user is admin
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(this.role);
    }
}
