package Utils; 

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class InstalledAppDetector {

    public static List<String> getInstalledApps() {
        List<String> apps = new ArrayList<>();
        try {
            // Execute Windows command to list installed apps
            Process process = Runtime.getRuntime().exec("wmic product get name");

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                // Skip empty lines and headers
                if (line.trim().isEmpty() || line.toLowerCase().contains("name")) {
                    continue;
                }
                apps.add(line.trim());
            }
            process.waitFor(); // Wait for the process to complete

        } catch (Exception e) {
            e.printStackTrace();
            // Optionally add more robust error logging or user notification
        }
        return apps;
    }

    public static void main(String[] args) {
        List<String> installedApps = getInstalledApps();
        System.out.println("Installed Applications:\n");
        for (String app : installedApps) {
            System.out.println("-" + app);
        }
    }
}