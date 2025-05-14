package uts.isd.model.Person;

import java.io.Serializable;

public class Staff extends User implements Serializable{
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