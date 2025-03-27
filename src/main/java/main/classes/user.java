package classes;

import java.io.serializable;

/*
Base class for a person or user involved in IOTbay

 */

public class User implements serializable {
    private static int totalUsers = 0;

    public final int ID;
    private String firstName;
    private String lastName;
    private String password;
    private String email;

    //    private Address address
    private UserType type;
    /*
    Default constructor for a user
    fn
    ln
    password
    email
    t type
     */
    public User(String firstName,String lastName,String password,String email,UserType type){
        ID= totalUsers++;

        firstName = firstName;
        lastName = lastName;
        password = password;
        email = email;
        type = type;
    }

    //    Getter and setter methods
    public String getFirstname(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail(){
        return email;
    }
    public UserType getType(){
        return type;
    }
    public enum UserType {
        CUSTOMER,
        STAFF
    }
}


