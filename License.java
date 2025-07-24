
package Model;

import java.io.Serializable;
import java.util.Date;

public class License implements Serializable {
    private String id;
    private String softwareName;
    private String licenseType;
    private String licenseKey;
    private Date expirationDate;
    private String assignedUserId;

    public License(String id, String softwareName, String licenseType, String licenseKey, Date expirationDate) {
        this.id = id;
        this.softwareName = softwareName;
        this.licenseType = licenseType;
        this.licenseKey = licenseKey;
        this.expirationDate = expirationDate;
    }

    public String getId() { 
        return id; 
    }
    public String getSoftwareName() { 
        return softwareName; 
    }
    public String getLicenseType() { 
        return licenseType;
     }
    public String getLicenseKey() { 
        return licenseKey;
     }
    public Date getExpirationDate() { 
        return expirationDate;
     }
    public String getAssignedUserId() { 
        return assignedUserId;
 }

    public void setAssignedUserId(String userId) {
        this.assignedUserId = userId;
    }

    public String toString() {
        return softwareName + " (" + licenseType + ")";
    }

    public void setSoftwareName(String text) {
        
        throw new UnsupportedOperationException("Unimplemented method 'setSoftwareName'");
    }

    public void setLicenseType(String selectedItem) {
       
        throw new UnsupportedOperationException("Unimplemented method 'setLicenseType'");
    }

    public void setExpirationDate(Date expiryDate) {
      
        throw new UnsupportedOperationException("Unimplemented method 'setExpirationDate'");
    }
}

