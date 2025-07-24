package Utils;

import Model.License;
import Model.Notification;
import Model.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbUtil { 

    // Method to save all data to MongoDB
    public static void saveData(MongoDatabase database, List<User> users, List<License> licenses, List<Notification> notifications) {
        // Clear existing data (optional, but good for full sync)
        database.getCollection("users").drop();
        database.getCollection("licenses").drop();
        database.getCollection("notifications").drop();

        // Save Users
        MongoCollection<Document> usersCollection = database.getCollection("users");
        for (User user : users) {
            Document userDoc = new Document("_id", user.getUserId())
                    .append("name", user.getName())
                    .append("email", user.getEmail())
                    .append("imagepath", user.getimagepath())
                    .append("licenseIds", user.getLicenseIds()); // Store list of license IDs
            usersCollection.insertOne(userDoc);
        }

        // Save Licenses
        MongoCollection<Document> licensesCollection = database.getCollection("licenses");
        for (License license : licenses) {
            Document licenseDoc = new Document("_id", license.getId())
                    .append("softwareName", license.getSoftwareName())
                    .append("licenseType", license.getLicenseType())
                    .append("licenseKey", license.getLicenseKey())
                    .append("expirationDate", license.getExpirationDate())
                    .append("assignedUserId", license.getAssignedUserId()); // Store assigned user ID
            licensesCollection.insertOne(licenseDoc);
        }

        // Save Notifications
        MongoCollection<Document> notificationsCollection = database.getCollection("notifications");
        for (Notification notification : notifications) {
            Document notificationDoc = new Document("_id", notification.getId())
                    .append("userId", notification.getUserId())
                    .append("licenseId", notification.getLicenseId())
                    .append("message", notification.getMessage())
                    .append("sentAt", notification.getSentAt());
            notificationsCollection.insertOne(notificationDoc);
        }

        System.out.println("Data saved to MongoDB successfully.");
    }

    // Method to load all data from MongoDB
    public static Object[] loadData(MongoDatabase database) {
        List<User> users = new ArrayList<>();
        List<License> licenses = new ArrayList<>();
        List<Notification> notifications = new ArrayList<>();

        // Load Users
        MongoCollection<Document> usersCollection = database.getCollection("users");
        for (Document doc : usersCollection.find()) {
            User user = new User(
                    doc.getString("_id"),
                    doc.getString("name"),
                    doc.getString("email"),
                    doc.getString("imagepath")
            );

            List<String> licenseIds = doc.getList("licenseIds", String.class);
            if (licenseIds != null) {
                for (String licenseId : licenseIds) {
                    user.assignLicense(licenseId);
                }
            }
            users.add(user);
        }

        // Load Licenses
        MongoCollection<Document> licensesCollection = database.getCollection("licenses");
        for (Document doc : licensesCollection.find()) {
            License license = new License(
                    doc.getString("_id"),
                    doc.getString("softwareName"),
                    doc.getString("licenseType"),
                    doc.getString("licenseKey"),
                    doc.getDate("expirationDate")
            );
            license.setAssignedUserId(doc.getString("assignedUserId")); // Set assigned user ID
            licenses.add(license);
        }

        // Load Notifications
        MongoCollection<Document> notificationsCollection = database.getCollection("notifications");
        for (Document doc : notificationsCollection.find()) {
            Notification notification = new Notification(
                    doc.getString("_id"),
                    doc.getString("userId"),
                    doc.getString("licenseId"),
                    doc.getString("message"),
                    doc.getDate("sentAt")
            );
            notifications.add(notification);
        }

        System.out.println("Data loaded from MongoDB successfully.");
        return new Object[]{users, licenses, notifications};
    }
}
