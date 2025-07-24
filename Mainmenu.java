package ui;

import javax.swing.*;
import java.awt.*;
import Manager.*;
import javax.swing.border.EmptyBorder; 

public class Mainmenu extends JFrame {

    private Licensemanager licenseManager;
    private Usermanager userManager;
    private Notificationmanager notificationManager;

    public Mainmenu(Licensemanager lm, Usermanager um, Notificationmanager nm) {
        this.licenseManager = lm;
        this.userManager = um;
        this.notificationManager = nm;

        setTitle("Tech License Manager - Main Menu");
        setSize(500, 450); // Slightly larger for better spacing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Set a clean background color for the frame content pane
        getContentPane().setBackground(new Color(240, 240, 240)); // Light gray background

       
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 150, 136)); // A nice blue for the header
        headerPanel.setBorder(new EmptyBorder(15, 0, 15, 0)); // Padding around the header
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center align header content

        JLabel titleLabel = new JLabel("Welcome to License Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Modern font, bold, larger size
        titleLabel.setForeground(Color.WHITE); // White text for contrast
        headerPanel.add(titleLabel);

        // --- Buttons Panel ---
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS)); // Stack buttons vertically
        buttonsPanel.setBorder(new EmptyBorder(20, 50, 20, 50)); // Padding around the buttons panel
        buttonsPanel.setBackground(new Color(240, 240, 240)); // Match frame background

        // Create buttons and apply common styling
        JButton btnLicense = createStyledButton("License Management");
        JButton btnUser = createStyledButton("User Management");
        JButton btnReport = createStyledButton("Reports");
        JButton btnNotifications = createStyledButton("Notifications");
        JButton btnSettings = createStyledButton("Settings");
        JButton btnExit = createStyledButton("Exit");

        // Add action listeners
        btnLicense.addActionListener(e -> new Licensepanel(licenseManager, userManager, notificationManager).setVisible(true));
        btnUser.addActionListener(e -> new Userpanel(licenseManager, userManager, notificationManager).setVisible(true));
        btnReport.addActionListener(e -> new Reportpanel(licenseManager, userManager).setVisible(true));
        btnNotifications.addActionListener(e -> new Notificationpanel(notificationManager, userManager).setVisible(true));
        btnSettings.addActionListener(e -> new Settingspanel().setVisible(true));
        btnExit.addActionListener(e -> System.exit(0));

        // Add buttons to the panel with alignment and spacing
        buttonsPanel.add(Box.createVerticalGlue()); // Pushes buttons to center
        buttonsPanel.add(btnLicense);
        buttonsPanel.add(Box.createVerticalStrut(15)); // Spacing between buttons
        buttonsPanel.add(btnUser);
        buttonsPanel.add(Box.createVerticalStrut(15));
        buttonsPanel.add(btnReport);
        buttonsPanel.add(Box.createVerticalStrut(15));
        buttonsPanel.add(btnNotifications);
        buttonsPanel.add(Box.createVerticalStrut(15));
        buttonsPanel.add(btnSettings);
        buttonsPanel.add(Box.createVerticalStrut(15));
        buttonsPanel.add(btnExit);
        buttonsPanel.add(Box.createVerticalGlue()); // Pushes buttons to center

        // Set maximum size for buttons to ensure consistent width in BoxLayout
        Dimension buttonSize = new Dimension(300, 100); // Wider and taller buttons
        btnLicense.setMaximumSize(buttonSize);
        btnUser.setMaximumSize(buttonSize);
        btnReport.setMaximumSize(buttonSize);
        btnNotifications.setMaximumSize(buttonSize);
        btnSettings.setMaximumSize(buttonSize);
        btnExit.setMaximumSize(buttonSize);

        // Align buttons to center horizontally within the BoxLayout
        btnLicense.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnReport.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnNotifications.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSettings.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExit.setAlignmentX(Component.CENTER_ALIGNMENT);


        // Add panels to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Helper method to create consistently styled buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 18)); // Modern font, good size
        button.setBackground(new Color(0, 150, 136)); // Lighter blue for buttons
        button.setForeground(Color.WHITE); // White text
        button.setFocusPainted(false); // Remove focus border for cleaner look
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding inside button
        button.setBorderPainted(false); // Let Nimbus handle border
       
        return button;
    }
}