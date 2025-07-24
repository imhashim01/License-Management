package ui;

import Manager.Notificationmanager;
import Manager.Usermanager;
import Model.Notification;
import Model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Notificationpanel extends JFrame {
    private Notificationmanager notificationManager;
    private Usermanager userManager; 
    private JTable table;
    private DefaultTableModel model;

    public Notificationpanel(Notificationmanager nm, Usermanager um) {
        this.notificationManager = nm;
        this.userManager = um;

        setTitle("Notifications");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(new String[]{"ID", "User", "License ID", "Message", "Sent At"}, 0);
        table = new JTable(model);
        // Style table header
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(60, 90, 150)); // Darker blue header
        table.getTableHeader().setForeground(Color.WHITE); // White text for header
        table.setRowHeight(25); // Increase row height for better readability
        table.setFillsViewportHeight(true); // Table fills the viewport height in scrollpane

        refreshTable();

        JButton refreshBtn = createStyledButton("Refresh");
        JButton clearAllBtn = createStyledButton("Clear All Notifications");

        refreshBtn.addActionListener(e -> refreshTable());
        clearAllBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear all notifications?", "Confirm Clear", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                notificationManager.clearAllNotifications();
                refreshTable();
                JOptionPane.showMessageDialog(this, "All notifications cleared!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)); // Added gaps and center alignment
        btnPanel.setBackground(new Color(240, 248, 255)); // Light background for button panel
        btnPanel.add(refreshBtn);
        btnPanel.add(clearAllBtn);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        setVisible(true);
        SwingUtilities.updateComponentTreeUI(this); // Ensure L&F update
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

    private void refreshTable() {
        model.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (Notification n : notificationManager.getAllNotifications()) {
            String userName = "N/A";
            User user = userManager.getUserById(n.getUserId());
            if (user != null) {
                userName = user.getName();
            }
            model.addRow(new Object[]{n.getId(), userName, n.getLicenseId(), n.getMessage(), sdf.format(n.getSentAt())});
        }
    }
}