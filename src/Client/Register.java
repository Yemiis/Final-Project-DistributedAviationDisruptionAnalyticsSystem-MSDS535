/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client;

/**
 *
 * @author yemi-co
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Register extends JFrame implements ActionListener {

    JLabel heading, userLabel, passLabel, imageLabel, webLabel;
    JTextField userField;
    JPasswordField passField;
    JButton registerButton, resetButton, backButton;

    public Register() {
        setTitle("Admin Registration");
        setSize(950, 680);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(220, 224, 229));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // small web image at top-left
        ImageIcon webIcon = new ImageIcon("/home/yemi-co/NetBeansProjects/DistributedAviationDisruptionAnalyticsSystem/project-images/web.png");
        Image webImg = webIcon.getImage().getScaledInstance(70, 40, Image.SCALE_SMOOTH);
        webLabel = new JLabel(new ImageIcon(webImg));
        webLabel.setBounds(20, 15, 70, 40);
        add(webLabel);

        // heading
        heading = new JLabel("Admin Registration");
        heading.setFont(new Font("Tahoma", Font.BOLD, 24));
        heading.setBounds(340, 25, 280, 35);
        add(heading);

        // labels
        userLabel = new JLabel("Set your username:");
        userLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        userLabel.setBounds(40, 110, 180, 28);
        add(userLabel);

        passLabel = new JLabel("Set your password:");
        passLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        passLabel.setBounds(40, 165, 180, 28);
        add(passLabel);

        // text fields
        userField = new JTextField();
        userField.setFont(new Font("Tahoma", Font.PLAIN, 15));
        userField.setBounds(240, 108, 210, 32);
        add(userField);

        passField = new JPasswordField();
        passField.setFont(new Font("Tahoma", Font.PLAIN, 15));
        passField.setBounds(240, 163, 210, 32);
        add(passField);

        // register button
        ImageIcon regIcon = new ImageIcon("/home/yemi-co/NetBeansProjects/DistributedAviationDisruptionAnalyticsSystem/project-images/register.jpg");
        Image regImg = regIcon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        registerButton = new JButton("Register", new ImageIcon(regImg));
        registerButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        registerButton.setBounds(40, 235, 155, 42);
        registerButton.setFocusPainted(false);
        registerButton.setIconTextGap(10);
        registerButton.addActionListener(this);
        add(registerButton);

        // reset button
        ImageIcon resetIcon = new ImageIcon("/home/yemi-co/NetBeansProjects/DistributedAviationDisruptionAnalyticsSystem/project-images/reset.jpg");
        Image resetImg = resetIcon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        resetButton = new JButton("Reset", new ImageIcon(resetImg));
        resetButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        resetButton.setBounds(210, 235, 140, 42);
        resetButton.setFocusPainted(false);
        resetButton.setIconTextGap(10);
        resetButton.addActionListener(this);
        add(resetButton);

        // back button
        backButton = new JButton("Back");
        backButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        backButton.setBounds(365, 235, 140, 42);
        backButton.setFocusPainted(false);
        backButton.addActionListener(this);
        add(backButton);

        // main image fills lower area fully
        ImageIcon bgIcon = new ImageIcon("/home/yemi-co/NetBeansProjects/DistributedAviationDisruptionAnalyticsSystem/project-images/main.jpg");
        Image bgImg = bgIcon.getImage().getScaledInstance(950, 355, Image.SCALE_SMOOTH);
        imageLabel = new JLabel(new ImageIcon(bgImg));
        imageLabel.setBounds(0, 325, 950, 355);
        add(imageLabel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == registerButton) {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password cannot be empty.");
                return;
            }

            Conn conn = new Conn();

            if (conn.getConnection() == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed. Check MySQL password and JDBC driver.");
                return;
            }

            try {
                String checkSql = "SELECT * FROM login WHERE username = ?";
                PreparedStatement checkPs = conn.getConnection().prepareStatement(checkSql);
                checkPs.setString(1, username);
                ResultSet rs = checkPs.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Username already exists. Choose another.");
                    return;
                }

                String insertSql = "INSERT INTO login(username, password) VALUES(?, ?)";
                PreparedStatement ps = conn.getConnection().prepareStatement(insertSql);
                ps.setString(1, username);
                ps.setString(2, password);

                int inserted = ps.executeUpdate();
                if (inserted > 0) {
                    JOptionPane.showMessageDialog(this, "Registration successful.");
                    setVisible(false);
                    new Login();
                } else {
                    JOptionPane.showMessageDialog(this, "Registration failed.");
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                conn.close();
            }

        } else if (ae.getSource() == resetButton) {
            userField.setText("");
            passField.setText("");
        } else if (ae.getSource() == backButton) {
            setVisible(false);
            new Login();
        }
    }

    public static void main(String[] args) {
        new Register();
    }
}