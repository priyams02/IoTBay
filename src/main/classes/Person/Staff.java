package classes.person;

import java.io.serializable;

public class Staff extends User implements serializable{
    private String role;

    public Staff(String firstName, String lastName, String password, String email) {
        super(firstName, lastName, password, email, UserType.STAFF);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String x) {
        role = x;
    }

}
