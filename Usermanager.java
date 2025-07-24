package Manager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates; // Still needed for potential partial updates
import org.bson.Document;
import Model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.security.MessageDigest; // For simple hashing (INSECURE)
import java.security.NoSuchAlgorithmException; // For hashing (INSECURE)
import java.util.Base64; // For encoding hash (INSECURE)

public class Usermanager {
    private final MongoCollection<Document> userCollection;

    public Usermanager(MongoCollection<Document> userCollection) {
        this.userCollection = userCollection;
    }
    
    private String hashPassword(String plainTextPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256"); 
            byte[] hashedBytes = md.digest(plainTextPassword.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hashing algorithm not found: " + e.getMessage());
            return null; 
        }
    }

    public boolean checkPassword(String plainTextPassword, String hashedPassword) {
        
        if (hashedPassword == null) return false;
        return hashPassword(plainTextPassword).equals(hashedPassword);
    }
   

    public void addUser(User user) {
        // Hash password before storing
        String hashedPassword = hashPassword(user.getPasswordHash()); 
        user.setPasswordHash(hashedPassword);

        Document doc = new Document("userId", user.getUserId())
                .append("name", user.getName())
                .append("email", user.getEmail())
                .append("imagepath", user.getimagepath())
                .append("passwordHash", user.getPasswordHash()) 
                .append("role", user.getRole())                 
                .append("licenseIds", user.getLicenseIds());
        userCollection.insertOne(doc);
    }

    public void removeUser(String id) {
        userCollection.deleteOne(Filters.eq("userId", id));
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        for (Document doc : userCollection.find()) {
            users.add(docToUser(doc));
        }
        return users;
    }

    public User getUserById(String id) { // This method is crucial for AuthManager
        Document doc = userCollection.find(Filters.eq("userId", id)).first();
        return doc != null ? docToUser(doc) : null;
    }

    public void updateUser(User updatedUser) {
        
        Document doc = new Document("userId", updatedUser.getUserId())
                .append("name", updatedUser.getName())
                .append("email", updatedUser.getEmail())
                .append("imagepath", updatedUser.getimagepath())
                .append("passwordHash", updatedUser.getPasswordHash()) 
                .append("role", updatedUser.getRole())
                .append("licenseIds", updatedUser.getLicenseIds());
        userCollection.replaceOne(Filters.eq("userId", updatedUser.getUserId()), doc);
    }

    // Helper method to convert Document to User object
    private User docToUser(Document doc) {
        User user = new User(
                doc.getString("userId"),
                doc.getString("name"),
                doc.getString("email"),
                doc.getString("imagepath"),
                doc.getString("passwordHash"), 
                doc.getString("role")          
        );
        
        List<String> licenseIds = doc.getList("licenseIds", String.class);
        if (licenseIds != null) {
            for (String licenseId : licenseIds) {
                user.assignLicense(licenseId); 
            }
        }
        return user;
    }
}