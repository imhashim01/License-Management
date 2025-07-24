package Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String userId;
    private String name;
    private String email;
    private String imagepath;
    private String passwordHash; 
    private String role;        
    private List<String> licenseIds;

    
    public User(String userId, String name, String email, String imagepath, String passwordHash, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.imagepath = imagepath;
        this.passwordHash = passwordHash;
        this.role = role;
        this.licenseIds = new ArrayList<>();
    }

   
    public User(String userId, String name, String email, String imagepath) {
        this(userId, name, email, imagepath, null, "standard"); // Default to no password hash and "standard" role
    }

    public String getUserId() {
        return userId;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public List<String> getLicenseIds() {
        return licenseIds;
    }

    public String getimagepath() {
        return imagepath;
    }
    public void setimagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public void assignLicense(String licenseId) {
        if (!licenseIds.contains(licenseId)) { // Prevent duplicates
            licenseIds.add(licenseId);
        }
    }

    public String getLicenseProfile(List<Model.License> allLicenses) {
        StringBuilder sb = new StringBuilder();
        sb.append("Licenses for ").append(name).append(" (Role: ").append(role).append("): \n");
        if (licenseIds.isEmpty()) {
            sb.append("  No licenses assigned.\n");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (String licenseId : licenseIds) {
                for (Model.License l : allLicenses) {
                    if (l.getId().equals(licenseId)) {
                        sb.append("- Software: ").append(l.getSoftwareName()).append("\n")
                          .append("  Type: ").append(l.getLicenseType()).append("\n")
                          .append("  Key: ").append(l.getLicenseKey()).append("\n")
                          .append("  Expiry: ").append(sdf.format(l.getExpirationDate())).append("\n\n");
                        break;
                    }
                }
            }
        }
        return sb.toString();
    }
}