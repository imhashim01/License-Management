package Manager;

import Model.User;
import Manager.Usermanager; 

public class AuthManager {
    private final Usermanager userManager; 

    public AuthManager(Usermanager userManager) { 
        this.userManager = userManager;
    }

   
    public User authenticateUser(String username, String password) {
        try {
            User user = userManager.getUserById(username); // Assuming userId is used as username for simplicity

            if (user != null && user.getPasswordHash() != null) {
                boolean passwordMatches = userManager.checkPassword(password, user.getPasswordHash());

                if (passwordMatches) {
                    System.out.println("User '" + username + "' authenticated successfully.");
                    return user;
                } else {
                    System.out.println("Authentication failed for user '" + username + "': Invalid password.");
                    return null;
                }
            } else {
                System.out.println("Authentication failed for user '" + username + "': User not found or no password set.");
                return null; // User not found or no password hash stored
            }
        } catch (Exception e) { // General catch for unexpected issues
            System.err.println("An unexpected error occurred during authentication for user '" + username + "': " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Registers a new user.
     * @param userId The unique ID for the new user.
     * @param name The user's name.
     * @param email The user's email.
     * @param imagePath The path to the user's profile image.
     * @param plainTextPassword The plain text password for the new user.
     * @param role The role of the new user (e.g., "admin", "standard").
     * @return true if registration is successful, false otherwise.
     */
    public boolean registerNewUser(String userId, String name, String email, String imagePath, String plainTextPassword, String role) {
        try {
            if (userManager.getUserById(userId) != null) {
                System.out.println("Registration failed: User ID '" + userId + "' already exists.");
                return false; // User ID already exists
            }

            // UserManager's addUser now handles hashing internally before saving
            User newUser = new User(userId, name, email, imagePath, plainTextPassword, role);
            userManager.addUser(newUser); // This will hash the password within UserManager
            System.out.println("User '" + userId + "' registered successfully.");
            return true;
        } catch (Exception e) { // General catch for unexpected issues during registration
            System.err.println("An unexpected error occurred during user registration for user '" + userId + "': " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
