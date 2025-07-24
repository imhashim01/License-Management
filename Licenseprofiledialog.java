package ui;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import Model.*;

public class Licenseprofiledialog extends JDialog {
    public Licenseprofiledialog(JFrame parent, User user, List<License> allLicenses) {
        super(parent, "License Profile for " + user.getName(), true);
        setSize(500, 400);
        setLayout(new BorderLayout());

        StringBuilder profileText = new StringBuilder();
        profileText.append("User: ").append(user.getName()).append("\n");
        profileText.append("Email: ").append(user.getEmail()).append("\n\n");

        List<String> licenseIds = user.getLicenseIds();
        if (licenseIds.isEmpty()) {
            profileText.append("No licenses assigned.\n");
        } else {
            profileText.append("Assigned Licenses:\n");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (String licenseId : licenseIds) {
                for (License l : allLicenses) {
                    if (l.getId().equals(licenseId)) {
                        profileText.append("- Software: ").append(l.getSoftwareName()).append("\n")
                                    .append("  Type: ").append(l.getLicenseType()).append("\n")
                                    .append("  Key: ").append(l.getLicenseKey()).append("\n")
                                    .append("  Expiry: ").append(sdf.format(l.getExpirationDate())).append("\n\n");
                    }
                }
            }
        }

        if (user.getimagepath() != null && !user.getimagepath().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(user.getimagepath());
                Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                JLabel picLabel = new JLabel(new ImageIcon(scaled));
                picLabel.setBorder(BorderFactory.createTitledBorder("Profile Picture"));
                add(picLabel, BorderLayout.WEST);
            } catch (Exception e) {
                System.out.println("Could not load image: " + e.getMessage());
            }
        }

        JTextArea textArea = new JTextArea(profileText.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JButton close = new JButton("Close");
        close.addActionListener(e -> dispose());
        add(close, BorderLayout.SOUTH);

        setLocationRelativeTo(parent);
        setVisible(true);
}
}