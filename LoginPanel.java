package ui;

import Manager.AuthManager;
import Manager.Licensemanager; 
import Manager.Usermanager;
import Manager.Notificationmanager;
import Model.User;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import javax.swing.border.EmptyBorder;

public class LoginPanel extends JFrame {
    private AuthManager authManager;
    private Licensemanager licenseManager;
    private Usermanager userManager;
    private Notificationmanager notificationManager;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public LoginPanel(AuthManager am, Licensemanager lm, Usermanager um, Notificationmanager nm) {
        this.authManager = am;
        this.licenseManager = lm;
        this.userManager = um;
        this.notificationManager = nm;

        setTitle("Tech License Manager - Login");
        setSize(400, 280);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255)); // Light background

        // --- Header ---
        JLabel loginHeader = new JLabel("Login to License Manager", SwingConstants.CENTER);
        loginHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        loginHeader.setForeground(new Color(60, 90, 150)); // Dark blue text
        mainPanel.add(loginHeader, BorderLayout.NORTH);

        // --- Input Panel ---
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(240, 248, 255)); // Match main panel background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        usernameField = new JTextField(15);
        inputPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        passwordField = new JPasswordField(15);
        inputPanel.add(passwordField, gbc);

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // --- Message Label ---
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mainPanel.add(messageLabel, BorderLayout.SOUTH);

        add(mainPanel);

        // --- Buttons Panel (will be at the bottom of the content pane) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton loginButton = createStyledButton("Login");
        JButton registerButton = createStyledButton("Register New User"); // Option to register (for admin to add users)
        JButton exitButton = createStyledButton("Exit");

        loginButton.addActionListener(e -> performLogin());
        registerButton.addActionListener(e -> showRegisterDialog());
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(exitButton);

        // Add buttonPanel to the frame's content pane directly, not mainPanel
        // to keep it separate at the bottom.
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);


        setVisible(true);
        SwingUtilities.updateComponentTreeUI(this); // Apply L&F
    }

   
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(60, 90, 150)); // Dark blue background
        button.setForeground(Color.WHITE); // White text
        button.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Bold font
        button.setFocusPainted(false); // Remove focus border
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(80, 110, 180)); // Lighter blue on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(60, 90, 150)); // Original color on exit
            }
        });
        return button;
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword()); // Get password as char array and convert to String

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Username and password cannot be empty.");
            return;
        }

        User authenticatedUser = authManager.authenticateUser(username, password);

        if (authenticatedUser != null) {
            messageLabel.setText(""); // Clear any previous error message
            JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + authenticatedUser.getName() + "!", "Login Success", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close login panel

            // Launch the main application UI (Welcomepage, which then goes to Mainmenu)
            new Welcomepage(licenseManager, userManager, notificationManager, authManager);

            // Here you could also save the authenticatedUser to a static context
            // or pass it to Mainmenu to control access based on role.
            // Example: ApplicationContext.setLoggedInUser(authenticatedUser);
        } else {
            messageLabel.setText("Invalid username or password.");
            passwordField.setText(""); // Clear password field on failure
        }
    }

    private void showRegisterDialog() {
        JTextField newUserIdField = new JTextField(15);
        JTextField newNameField = new JTextField(15);
        JTextField newEmailField = new JTextField(15);
        JPasswordField newPasswordField = new JPasswordField(15);
        JPasswordField confirmPasswordField = new JPasswordField(15);
        JTextField newImagePathField = new JTextField(15);
        newImagePathField.setEditable(false);
        JButton browseImageBtn = new JButton("Browse");

        browseImageBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                newImagePathField.setText(selectedFile.getAbsolutePath());
            }
        });

        String[] roles = {"standard", "admin"}; // Define available roles
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.setSelectedItem("standard"); // Default role

        JPanel registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; registerPanel.add(new JLabel("User ID (Username):"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; registerPanel.add(newUserIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; registerPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; registerPanel.add(newNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; registerPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; registerPanel.add(newEmailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; registerPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; registerPanel.add(newPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; registerPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; registerPanel.add(confirmPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; registerPanel.add(new JLabel("Image Path:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; registerPanel.add(newImagePathField, gbc);
        gbc.gridx = 2; gbc.gridy = 5; registerPanel.add(browseImageBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 6; registerPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6; registerPanel.add(roleComboBox, gbc);


        int option = JOptionPane.showConfirmDialog(this, registerPanel, "Register New User", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String userId = newUserIdField.getText();
            String name = newNameField.getText();
            String email = newEmailField.getText();
            String password = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String imagePath = newImagePathField.getText();
            String role = (String) roleComboBox.getSelectedItem();

            if (userId.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields marked with an asterisk (*) must be filled.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (authManager.registerNewUser(userId, name, email, imagePath, password, role)) {
                JOptionPane.showMessageDialog(this, "User registered successfully!", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to register user. User ID might already exist.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}