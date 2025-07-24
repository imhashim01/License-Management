package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Manager.Licensemanager;
import Manager.Notificationmanager;
import Manager.Usermanager;
import Manager.AuthManager; 
import java.net.URL;

public class Welcomepage extends JFrame {

    private Licensemanager licenseManager;
    private Usermanager userManager;
    private Notificationmanager notificationManager;
    private AuthManager authManager; 

    
    public Welcomepage(Licensemanager lm, Usermanager um, Notificationmanager nm, AuthManager am) {
        this.licenseManager = lm;
        this.userManager = um;
        this.notificationManager = nm;
        this.authManager = am;

        setTitle("Welcome to Tech License App");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(230, 240, 250));
        panel.setBorder(BorderFactory.createLineBorder(new Color(60, 90, 150), 3));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel imageLabel = new JLabel();
        try {
            URL imageUrl = getClass().getResource("/images/welcome_icon.png");
            if (imageUrl == null) {
                imageUrl = new URL("file:./images/welcome_icon.png");
            }
            ImageIcon originalIcon = new ImageIcon(imageUrl);
            Image scaledImage = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.err.println("Error loading image for Welcomepage: " + e.getMessage());
            imageLabel.setText("(Image Missing)");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
            imageLabel.setPreferredSize(new Dimension(120, 120));
            imageLabel.setBorder(BorderFactory.createLineBorder(Color.RED));
        }
        panel.add(imageLabel, gbc);

        JLabel welcomeLabel = new JLabel("Welcome to Tech License App");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(25, 75, 125));
        gbc.gridy = 1;
        panel.add(welcomeLabel, gbc);

        JLabel statusLabel = new JLabel("Loading managers...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setForeground(new Color(100, 100, 100));
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 10, 20, 10);
        panel.add(statusLabel, gbc);

        add(panel);
        setVisible(true);

        javax.swing.Timer timer = new javax.swing.Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the welcome page
                // Pass all managers to Mainmenu
                new Mainmenu(licenseManager, userManager, notificationManager); 
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}