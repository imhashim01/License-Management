package Manager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import Model.Notification;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Notificationmanager {
    private final MongoCollection<Document> notificationCollection;

    public Notificationmanager(MongoCollection<Document> notificationCollection) {
        this.notificationCollection = notificationCollection;
    }

    public void addNotification(String userId, String licenseId, String message) {
        String notificationId = UUID.randomUUID().toString();
        Date sentAt = new Date();
        Document doc = new Document("id", notificationId)
                .append("userId", userId)
                .append("licenseId", licenseId)
                .append("message", message)
                .append("sentAt", sentAt);
        notificationCollection.insertOne(doc);
        System.out.println("New Notification: " + message + " for user " + userId);
    }

    public List<Notification> getNotificationsByUserId(String userId) {
        List<Notification> userNotifications = new ArrayList<>();
        for (Document doc : notificationCollection.find(Filters.eq("userId", userId))) {
            userNotifications.add(docToNotification(doc));
        }
        return userNotifications;
    }

    public List<Notification> getAllNotifications() {
        List<Notification> notifications = new ArrayList<>();
        for (Document doc : notificationCollection.find()) {
            notifications.add(docToNotification(doc));
        }
        return notifications;
    }

    public void clearAllNotifications() {
        notificationCollection.deleteMany(new Document()); // Deletes all documents in the collection
    }

    
    private Notification docToNotification(Document doc) {
        return new Notification(
                doc.getString("id"),
                doc.getString("userId"),
                doc.getString("licenseId"),
                doc.getString("message"),
                doc.getDate("sentAt")
        );
    }
}


