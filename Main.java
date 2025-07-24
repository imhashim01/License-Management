import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javax.swing.UIManager;
import javax.swing.SwingUtilities; 

import org.bson.Document; 

import ui.LoginPanel;
import Manager.AuthManager; 
import Manager.Licensemanager;
import Manager.Usermanager;
import Manager.Notificationmanager;
import ui.Mainmenu;
import ui.Welcomepage;

public class Main {
    public static void main(String[] args) {
        String connectionString = "mongodb+srv://hafizmuhammadhashim05:1l3g2s4rabP6Bikf@cluster0.xhbfszv.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();

        // Establish MongoDB connection and initialize application components
        try {
            MongoClient mongoClient = MongoClients.create(settings);
            // Get the specific database for your application
            // Replace "LicenseManagerDB" with your desired database name if different.
            MongoDatabase database = mongoClient.getDatabase("LicenseManagerDB");

            MongoCollection<Document> licenseCollection = database.getCollection("licenses");
            MongoCollection<Document> userCollection = database.getCollection("users");
            MongoCollection<Document> notificationCollection = database.getCollection("notifications");

            Notificationmanager notificationManager = new Notificationmanager(notificationCollection);
            Licensemanager licenseManager = new Licensemanager(licenseCollection, notificationManager);
            Usermanager userManager = new Usermanager(userCollection);
             AuthManager authManager = new AuthManager(userManager);

            
            new LoginPanel(authManager, licenseManager, userManager, notificationManager);

            
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (mongoClient != null) {
                    mongoClient.close();
                    System.out.println("MongoDB client closed gracefully.");
                }
            }));

        } catch (MongoException e) {
            System.err.println("Error connecting to MongoDB or initializing managers: " + e.getMessage());
            e.printStackTrace(); 
            System.exit(1);
        }
    }
} 