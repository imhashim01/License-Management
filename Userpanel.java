package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Manager.*;
import Model.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class Userpanel extends JFrame {
    private Licensemanager licenseManager;
    private Usermanager userManager;
    private Notificationmanager notificationManager;
    private JTable table;
    private DefaultTableModel model;

    public Userpanel(Licensemanager lm, Usermanager um, Notificationmanager nm) {
        this.licenseManager = lm;
        this.userManager = um;
        this.notificationManager = nm;

        setTitle("User Management");
        setSize(700, 400); // Keep increased width for "Assigned Licenses" column
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Table setup
        model = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Assigned Licenses"}, 0);
        table = new JTable(model);
        // Make table headers also look better with Nimbus
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(60, 90, 150)); // Darker blue header
        table.getTableHeader().setForeground(Color.WHITE); // White text for header
        table.setRowHeight(25); // Increase row height for better readability
        table.setFillsViewportHeight(true); // Table fills the viewport height in scrollpane


        refreshTable();

        // Buttons using the new styling method
        JButton addBtn = createStyledButton("Add User");
        JButton deleteBtn = createStyledButton("Delete User");
        JButton viewProfileBtn = createStyledButton("View License Profile");
        JButton updateBtn = createStyledButton("Update User");

        // Action Listeners
        addBtn.addActionListener(e -> addUserDialog());
        deleteBtn.addActionListener(e -> deleteSelectedUser());
        viewProfileBtn.addActionListener(e -> viewLicenseProfile());
        updateBtn.addActionListener(e -> updateUserDialog());

        // Button Panel setup - explicitly use FlowLayout for horizontal arrangement
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)); // Center alignment, 10px horizontal/vertical gap
        btnPanel.setBackground(new Color(240, 248, 255)); // Light background for button panel
        btnPanel.add(addBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(viewProfileBtn);
        btnPanel.add(updateBtn);

        // Frame Layout
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        setVisible(true);

        // Force UI update to ensure L&F is applied to all components
        SwingUtilities.updateComponentTreeUI(this);
    }

    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(60, 90, 150)); // Dark blue background
        button.setForeground(Color.WHITE); // White text
        button.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Bold font
        button.setFocusPainted(false); // Remove focus border
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover

        // Add hover effect (optional, but enhances attractiveness)
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

    private void refreshTable() {
        model.setRowCount(0);
        for (User u : userManager.getAllUsers()) {
            int licenseCount = u.getLicenseIds().size();
            model.addRow(new Object[]{u.getUserId(), u.getName(), u.getEmail(), licenseCount});
        }
    }

    private void addUserDialog() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField imagePathField = new JTextField();
        imagePathField.setEditable(false);
        JButton browseBtn = new JButton("Browse");

        browseBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // Ensure only files can be selected
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selected = chooser.getSelectedFile();
                imagePathField.setText(selected.getAbsolutePath());
            }
        });

        JPanel panel = new JPanel(new GridLayout(4, 3, 5, 5)); // Added gaps
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding for dialog content
        panel.add(new JLabel("User ID:")); panel.add(idField); panel.add(new JLabel(""));
        panel.add(new JLabel("Name:")); panel.add(nameField); panel.add(new JLabel(""));
        panel.add(new JLabel("Email:")); panel.add(emailField); panel.add(new JLabel(""));
        panel.add(new JLabel("Image Path:")); panel.add(imagePathField); panel.add(browseBtn);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add User", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE); // Use 'this' as parent
        if (result == JOptionPane.OK_OPTION) {
            // Basic validation
            if (idField.getText().isEmpty() || nameField.getText().isEmpty() || emailField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields (except Image Path) must be filled.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Check for duplicate ID
            if (userManager.getUserById(idField.getText()) != null) {
                JOptionPane.showMessageDialog(this, "User with this ID already exists. Please use a unique ID.", "Duplicate ID", JOptionPane.WARNING_MESSAGE);
                return;
            }
            User user = new User(idField.getText(), nameField.getText(), emailField.getText(), imagePathField.getText());
            userManager.addUser(user);
            refreshTable();
            JOptionPane.showMessageDialog(this, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteSelectedUser() {
        int selected = table.getSelectedRow();
        if (selected >= 0) {
            String userId = model.getValueAt(selected, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete user: " + model.getValueAt(selected, 1).toString() + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                userManager.removeUser(userId);
                refreshTable();
                JOptionPane.showMessageDialog(this, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a user to delete.");
        }
    }

    private void viewLicenseProfile() {
        int selected = table.getSelectedRow();
        if (selected >= 0) {
            String userId = model.getValueAt(selected, 0).toString();
            User user = userManager.getUserById(userId);
            if (user != null) {
                List<License> allLicenses = licenseManager.getAllLicenses();
                new Licenseprofiledialog(this, user, allLicenses);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a user to view their license profile.");
        }
    }

    private void updateUserDialog() {
        int selected = table.getSelectedRow();
        if (selected >= 0) {
            String userId = model.getValueAt(selected, 0).toString();
            User currentUser = userManager.getUserById(userId);

            if (currentUser != null) {
                JTextField nameField = new JTextField(currentUser.getName());
                JTextField emailField = new JTextField(currentUser.getEmail());
                JTextField imagePathField = new JTextField(currentUser.getimagepath());
                imagePathField.setEditable(false);
                JButton browseBtn = new JButton("Browse");

                browseBtn.addActionListener(e -> {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    int result = chooser.showOpenDialog(this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = chooser.getSelectedFile();
                        imagePathField.setText(selectedFile.getAbsolutePath());
                    }
                });

                JPanel panel = new JPanel(new GridLayout(3, 3, 5, 5)); // Added gaps
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding for dialog content
                panel.add(new JLabel("Name:")); panel.add(nameField); panel.add(new JLabel(""));
                panel.add(new JLabel("Email:")); panel.add(emailField); panel.add(new JLabel(""));
                panel.add(new JLabel("Image Path:")); panel.add(imagePathField); panel.add(browseBtn);

                int result = JOptionPane.showConfirmDialog(this, panel, "Update User", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    if (nameField.getText().isEmpty() || emailField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Name and Email fields must be filled.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    User updatedUser = new User(userId, nameField.getText(), emailField.getText(), imagePathField.getText());
                    userManager.updateUser(updatedUser);
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a user to update.");
        }
    }
}