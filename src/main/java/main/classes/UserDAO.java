package main.classes;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    // In-memory list to store users (will lose memory if server stops/resets)
    private static List<User> users = new ArrayList<>();

    // Add a new user
    public static void addUser(User user) {
        users.add(user);
    }

    // Find a user by email (maybe good idea to change username to just email)
    public static User findUserByEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }
}
