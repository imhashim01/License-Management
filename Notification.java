package Model;

import java.io.Serializable;
import java.util.Date;

public class Notification implements Serializable {
    private String id;
    private String userId;
    private String licenseId;
    private String message;
    private Date sentAt;

    public Notification(String id, String userId, String licenseId, String message, Date sentAt) {
        this.id = id;
        this.userId = userId;
        this.licenseId = licenseId;
        this.message = message;
        this.sentAt = sentAt;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public String getMessage() {
        return message;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }
}
