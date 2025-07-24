package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer; // For table cell alignment
import Manager.*;
import Model.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.border.EmptyBorder; // For padding

public class Licensepanel extends JFrame {
    private Licensemanager licenseManager;
    private Usermanager userManager;
    private Notificationmanager notificationManager;
    private JTable table;
    private DefaultTableModel model;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd"); // Consistent date format

    public Licensepanel(Licensemanager lm, Usermanager um, Notificationmanager nm) {
        this.licenseManager = lm;
        this.userManager = um;
        this.notificationManager = nm;

        setTitle("License Management");
        setSize(800, 600); // Increased size for better table visibility
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose on close so it doesn't close the whole app

        // Set a clean background color for the frame content pane
        getContentPane().setBackground(new Color(245, 245, 245)); // Light background

        model = new DefaultTableModel(new String[]{"ID", "Software", "Type", "Expiration", "Assigned To"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        table = new JTable(model);
        table.setFillsViewportHeight(true); // Fills the viewport height
        table.setRowHeight(25); // Increase row height for readability
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Consistent font
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14)); // Bold header font
        table.getTableHeader().setBackground(new Color(220, 220, 220)); // Light gray header
        table.setSelectionBackground(new Color(170, 200, 250)); // Light blue selection background
        table.setGridColor(new Color(200, 200, 200)); // Lighter grid lines

       
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        refreshTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding around table

        // --- Buttons Panel ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10)); // Center align buttons with spacing
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0)); // Padding around button panel
        buttonPanel.setBackground(new Color(245, 245, 245)); // Match background

        JButton addBtn = createStyledButton("Add License");
        JButton deleteBtn = createStyledButton("Delete License");
        JButton assignBtn = createStyledButton("Assign to User");
        JButton unassignBtn = createStyledButton("Unassign License");
        JButton editBtn = createStyledButton("Edit License"); // Added for editing existing licenses

        addBtn.addActionListener(e -> addLicenseDialog());
        deleteBtn.addActionListener(e -> deleteSelectedLicense());
        assignBtn.addActionListener(e -> assignLicense());
        unassignBtn.addActionListener(e -> unassignLicense());
        editBtn.addActionListener(e -> editLicenseDialog()); // Action listener for edit

        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(assignBtn);
        buttonPanel.add(unassignBtn);
        buttonPanel.add(editBtn); // Add edit button to panel

        
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Helper method to create consistently styled buttons (reused from Mainmenu)
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Slightly smaller font for panel buttons
        button.setBackground(new Color(60, 90, 150)); // Lighter blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18)); // Padding inside button
        button.setBorderPainted(false); // Let Nimbus handle border if any
        return button;
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (License l : licenseManager.getAllLicenses()) {
            String assignedToName = "Unassigned";
            if (l.getAssignedUserId() != null && !l.getAssignedUserId().isEmpty()) {
                User assignedUser = userManager.getUserById(l.getAssignedUserId());
                if (assignedUser != null) {
                    assignedToName = assignedUser.getName();
                }
            }
            model.addRow(new Object[]{
                l.getId(),
                l.getSoftwareName(),
                l.getLicenseType(),
                dateFormatter.format(l.getExpirationDate()), // Use dateFormatter
                assignedToName
            });
        }
    }

    private void addLicenseDialog() {
        JTextField softwareNameField = new JTextField(20);
        String[] licenseTypes = {"Perpetual", "Subscription", "Trial"};
        JComboBox<String> licenseTypeComboBox = new JComboBox<>(licenseTypes);
        JTextField licenseKeyField = new JTextField(20);
        JFormattedTextField expirationDateField = new JFormattedTextField(dateFormatter);
        expirationDateField.setColumns(20); // Ensure proper size for date field
        
        // --- Dialog Content Panel ---
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding for the dialog content
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Software Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(softwareNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("License Type:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(licenseTypeComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("License Key:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(licenseKeyField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Expiration Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(expirationDateField, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New License",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE); // Use PLAIN_MESSAGE for no icon

        if (result == JOptionPane.OK_OPTION) {
            try {
                Date expiryDate = dateFormatter.parse(expirationDateField.getText());
                License newLicense = new License(
                    java.util.UUID.randomUUID().toString(),
                    softwareNameField.getText(),
                    (String) licenseTypeComboBox.getSelectedItem(),
                    licenseKeyField.getText(),
                    expiryDate
                );
                licenseManager.addLicense(newLicense);
                JOptionPane.showMessageDialog(this, "License added successfully!");
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format or other error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedLicense() {
        int selected = table.getSelectedRow();
        if (selected >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this license?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String licenseId = model.getValueAt(selected, 0).toString();
                License licenseToDelete = licenseManager.getLicenseById(licenseId);
                if (licenseToDelete != null) {
                    // Also unassign from user if assigned
                    if (licenseToDelete.getAssignedUserId() != null && !licenseToDelete.getAssignedUserId().isEmpty()) {
                        User assignedUser = userManager.getUserById(licenseToDelete.getAssignedUserId());
                        if (assignedUser != null) {
                            assignedUser.getLicenseIds().remove(licenseId);
                           
                            userManager.updateUser(assignedUser); // Ensure user's license list is updated in DB
                        }
                    }
                    licenseManager.removeLicense(licenseId);
                    JOptionPane.showMessageDialog(this, "License deleted successfully.");
                    refreshTable();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a license to delete.");
        }
    }
    
    private void assignLicense() {
        int selectedLicenseRow = table.getSelectedRow();
        if (selectedLicenseRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a license to assign.", "No License Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String licenseId = model.getValueAt(selectedLicenseRow, 0).toString();
        License licenseToAssign = licenseManager.getLicenseById(licenseId);

        if (licenseToAssign != null && licenseToAssign.getAssignedUserId() != null) {
            JOptionPane.showMessageDialog(this, "This license is already assigned.", "License Already Assigned", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        List<User> allUsers = userManager.getAllUsers();
        if (allUsers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No users available to assign licenses to. Please add users first.", "No Users", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        User[] usersArray = allUsers.toArray(new User[0]);
        JComboBox<User> userComboBox = new JComboBox<>(usersArray);
        userComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof User) {
                    setText(((User) value).getName()); // Display user's name
                }
                return this;
            }
        });

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.add(new JLabel("Select User:"));
        panel.add(userComboBox);
        panel.setBorder(new EmptyBorder(10,10,10,10));

        int result = JOptionPane.showConfirmDialog(this, panel, "Assign License to User",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION && userComboBox.getSelectedItem() != null) {
            User selectedUser = (User) userComboBox.getSelectedItem();
            
            // Check if the license is already in the user's list (though it should be null at this point for reassignment)
            if (selectedUser.getLicenseIds().contains(licenseId)) {
                JOptionPane.showMessageDialog(this, "User already has this license assigned.", "Assignment Redundant", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            selectedUser.assignLicense(licenseId);
            licenseToAssign.setAssignedUserId(selectedUser.getUserId());

            userManager.updateUser(selectedUser); // Update user in DB
            licenseManager.updateLicense(licenseToAssign); // Update license in DB

            // Add notification for assignment
            notificationManager.addNotification(selectedUser.getUserId(), licenseId,
                "License '" + licenseToAssign.getSoftwareName() + "' has been assigned to you.");

            JOptionPane.showMessageDialog(this, "License assigned successfully to " + selectedUser.getName() + ".");
            refreshTable();
        }
    }

    private void unassignLicense() {
        int selected = table.getSelectedRow();
        if (selected >= 0) {
            String licenseId = model.getValueAt(selected, 0).toString();
            License license = licenseManager.getLicenseById(licenseId);

            if (license != null && license.getAssignedUserId() != null && !license.getAssignedUserId().isEmpty()) {
                User assignedUser = userManager.getUserById(license.getAssignedUserId());
                if (assignedUser != null) {
                    int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to unassign '" + license.getSoftwareName() + "' from " + assignedUser.getName() + "?",
                        "Confirm Unassign", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        assignedUser.getLicenseIds().remove(licenseId); // Remove license from user's list
                        license.setAssignedUserId(null); // Set license's assigned user to null

                        userManager.updateUser(assignedUser); // Update user in DB
                        licenseManager.updateLicense(license); // Update license in DB

                        // Add notification for unassignment
                        notificationManager.addNotification(assignedUser.getUserId(), licenseId,
                            "License '" + license.getSoftwareName() + "' has been unassigned from your profile.");

                        JOptionPane.showMessageDialog(this, "License unassigned successfully.");
                        refreshTable();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selected license is not assigned to any user.", "Not Assigned", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a license to unassign.", "No License Selected", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void editLicenseDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a license to edit.", "No License Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String licenseId = model.getValueAt(selectedRow, 0).toString();
        License existingLicense = licenseManager.getLicenseById(licenseId);

        if (existingLicense == null) {
            JOptionPane.showMessageDialog(this, "License not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField softwareNameField = new JTextField(existingLicense.getSoftwareName(), 20);
        String[] licenseTypes = {"Perpetual", "Subscription", "Trial"};
        JComboBox<String> licenseTypeComboBox = new JComboBox<>(licenseTypes);
        licenseTypeComboBox.setSelectedItem(existingLicense.getLicenseType());
        JTextField licenseKeyField = new JTextField(existingLicense.getLicenseKey(), 20);
        JFormattedTextField expirationDateField = new JFormattedTextField(dateFormatter);
        expirationDateField.setValue(existingLicense.getExpirationDate()); // Set existing date
        expirationDateField.setColumns(20);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Software Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(softwareNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("License Type:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(licenseTypeComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("License Key:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(licenseKeyField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Expiration Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(expirationDateField, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit License",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Date expiryDate = dateFormatter.parse(expirationDateField.getText());
                existingLicense.setSoftwareName(softwareNameField.getText()); // Assuming setters exist in License model
                existingLicense.setLicenseType((String) licenseTypeComboBox.getSelectedItem()); // Assuming setters exist
               
                existingLicense.setExpirationDate(expiryDate); // Assuming setter exists

                licenseManager.updateLicense(existingLicense);
                JOptionPane.showMessageDialog(this, "License updated successfully!");
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format or other error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}