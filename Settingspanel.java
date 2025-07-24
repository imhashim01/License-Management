package ui;

import javax.swing.*;
import java.awt.*;

public class Settingspanel extends JFrame {
    public Settingspanel() {
        setTitle("Application Settings");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255)); 

        JLabel messageLabel = new JLabel("Settings will go here!", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Serif", Font.BOLD, 20));
        messageLabel.setForeground(new Color(60, 90, 150)); // Dark blue text
        panel.add(messageLabel, BorderLayout.CENTER);

        JButton closeButton = createStyledButton("Close"); // Using the styled button
        closeButton.addActionListener(e -> dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255)); // Match panel background
        buttonPanel.add(closeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
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
}