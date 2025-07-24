package Manager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import Model.License;
import Model.User;
import java.util.*;
import java.util.stream.Collectors;

public class Licensemanager {
    private MongoCollection<Document> licenseCollection;
    private Notificationmanager notificationManager; // Dependency for notifications

    public Licensemanager(MongoCollection<Document> licenseCollection, Notificationmanager notificationManager) {
        this.licenseCollection = licenseCollection;
        this.notificationManager = notificationManager;
    }

    public void addLicense(License license) {
        Document doc = new Document("id", license.getId())
                .append("softwareName", license.getSoftwareName())
                .append("licenseType", license.getLicenseType())
                .append("licenseKey", license.getLicenseKey())
                .append("expirationDate", license.getExpirationDate())
                .append("assignedUserId", license.getAssignedUserId());
        licenseCollection.insertOne(doc);
    }

    public void removeLicense(String id) {
        licenseCollection.deleteOne(Filters.eq("id", id));
    }

    public List<License> getAllLicenses() {
        List<License> licenses = new ArrayList<>();
        for (Document doc : licenseCollection.find()) {
            licenses.add(docToLicense(doc));
        }
        return licenses;
    }

    public License getLicenseById(String id) {
        Document doc = licenseCollection.find(Filters.eq("id", id)).first();
        return doc != null ? docToLicense(doc) : null;
    }

    public List<License> getExpiringLicenses(int days) {
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_YEAR, days);
        Date future = cal.getTime();

        List<License> expiringLicenses = new ArrayList<>();
        for (Document doc : licenseCollection.find()) {
            License license = docToLicense(doc);
            if (license.getExpirationDate() != null &&
                license.getExpirationDate().after(today) &&
                license.getExpirationDate().before(future)) {
                expiringLicenses.add(license);
            }
        }
        return expiringLicenses;
    }

    public void sendExpiryReminders(List<User> users) {
        List<License> expiring = getExpiringLicenses(7); 
        for (License license : expiring) {
            if (license.getAssignedUserId() != null) {
                for (User user : users) { 
                    if (user.getUserId().equals(license.getAssignedUserId())) {
                        String message = "License '" + license.getSoftwareName() + "' for " + user.getName() + " expires on " + license.getExpirationDate();
                        notificationManager.addNotification(user.getUserId(), license.getId(), message);
                        System.out.println("[Reminder] " + message); // For console logging
                        break;
                    }
                }
            }
        }
    }

    public void updateLicense(License license) {
        Document doc = new Document("id", license.getId())
                .append("softwareName", license.getSoftwareName())
                .append("licenseType", license.getLicenseType())
                .append("licenseKey", license.getLicenseKey())
                .append("expirationDate", license.getExpirationDate())
                .append("assignedUserId", license.getAssignedUserId());
        licenseCollection.replaceOne(Filters.eq("id", license.getId()), doc);
    }

    // Helper method to convert Document to License object
    private License docToLicense(Document doc) {
        License license = new License(
                doc.getString("id"),
                doc.getString("softwareName"),
                doc.getString("licenseType"),
                doc.getString("licenseKey"),
                doc.getDate("expirationDate")
        );
        license.setAssignedUserId(doc.getString("assignedUserId"));
        return license;
    }
}

