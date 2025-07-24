package ui;

import javax.swing.*;
import Manager.*;
import Model.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import Utils.InstalledAppDetector; // Added import for InstalledAppDetector

public class Reportpanel extends JFrame {
    private Licensemanager licenseManager;
    private Usermanager userManager;
    private JTextArea reportArea;

    public Reportpanel(Licensemanager lm, Usermanager um) {
        this.licenseManager = lm;
        this.userManager = um;

        setTitle("Reports");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        reportArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        // *** ADD THESE TWO LINES FOR VERTICAL DISPLAY ***
        reportArea.setLineWrap(true); // Enables line wrapping
        reportArea.setWrapStyleWord(true); // Wraps at word boundaries

        JButton usageBtn = createStyledButton("License Usage Report");
        JButton expiryBtn = createStyledButton("License Expiry Report");
        JButton userLicenseBtn = createStyledButton("User License Report");
        JButton reminderBtn = createStyledButton("Send Expiry Reminders");
        JButton installedAppsBtn = createStyledButton("Show Installed Apps");

        usageBtn.addActionListener(e -> showUsageReport());
        expiryBtn.addActionListener(e -> showExpiryReport());
        userLicenseBtn.addActionListener(e -> showUserLicenseReport());
        reminderBtn.addActionListener(e -> sendReminders());
        installedAppsBtn.addActionListener(e -> showInstalledApplications());

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.add(usageBtn);
        buttonPanel.add(expiryBtn);
        buttonPanel.add(userLicenseBtn);
        buttonPanel.add(reminderBtn);
        buttonPanel.add(installedAppsBtn);

        setLayout(new BorderLayout());
        add(new JScrollPane(reportArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.WEST);

        setVisible(true);
    }

    private void showUsageReport() {
        StringBuilder sb = new StringBuilder("License Usage Report:\n\n");
        List<License> allLicenses = licenseManager.getAllLicenses();
        if (allLicenses.isEmpty()) {
            sb.append("No licenses available.");
        } else {
            for (License l : allLicenses) {
                String assignedTo = "Unassigned";
                if (l.getAssignedUserId() != null && !l.getAssignedUserId().isEmpty()) {
                    User user = userManager.getUserById(l.getAssignedUserId());
                    if (user != null) {
                        assignedTo = user.getName();
                    }
                }
                sb.append("- Software: ").append(l.getSoftwareName())
                  .append(", Type: ").append(l.getLicenseType())
                  .append(", Assigned To: ").append(assignedTo).append("\n");
            }
        }
        reportArea.setText(sb.toString());
        reportArea.setCaretPosition(0);
    }

    private void showExpiryReport() {
        List<License> expiring = licenseManager.getExpiringLicenses(30);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sb = new StringBuilder("Licenses Expiring Soon (30 days):\n\n");
        if (expiring.isEmpty()) {
            sb.append("No licenses expiring within 30 days.");
        } else {
            for (License l : expiring) {
                sb.append("- ").append(l.getSoftwareName()).append(" expires on ").append(sdf.format(l.getExpirationDate())).append("\n");
            }
        }
        reportArea.setText(sb.toString());
        reportArea.setCaretPosition(0);
    }

    private void showUserLicenseReport() {
        StringBuilder sb = new StringBuilder("User License Report:\n\n");
        List<User> allUsers = userManager.getAllUsers();
        if (allUsers.isEmpty()) {
            sb.append("No users available to report licenses.");
        } else {
            for (User u : allUsers) {
                sb.append(u.getLicenseProfile(licenseManager.getAllLicenses())).append("\n");
            }
        }
        reportArea.setText(sb.toString());
        reportArea.setCaretPosition(0);
    }

    private void sendReminders() {
        licenseManager.sendExpiryReminders(userManager.getAllUsers());
        JOptionPane.showMessageDialog(this, "Expiry reminders sent (check console for details).");
    }

    private void showInstalledApplications() {
        List<String> installedApps = InstalledAppDetector.getInstalledApps();
        StringBuilder sb = new StringBuilder("Installed Applications:\n\n");
        if (installedApps.isEmpty()) {
            sb.append("No installed applications found or an error occurred. (Note: This feature works on Windows only and requires WMIC.)");
        } else {
            for (String app : installedApps) {
                sb.append("- ").append(app).append("\n");
            }
        }
        reportArea.setText(sb.toString());
        reportArea.setCaretPosition(0);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(60, 90, 150));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(80, 110, 180));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(60, 90, 150));
            }
        });
        return button;
    }
}