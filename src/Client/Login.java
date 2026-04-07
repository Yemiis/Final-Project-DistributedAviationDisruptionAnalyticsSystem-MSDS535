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

public class Login extends JFrame implements ActionListener {

    JLabel heading, userLabel, passLabel, imageLabel, webLabel;
    JTextField userField;
    JPasswordField passField;
    JButton loginButton, registerButton, resetButton;

    public Login() {
        setTitle("Admin Login");
        setSize(950, 680);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(220, 224, 229));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // small web image at top-left
        ImageIcon webIcon = new ImageIcon("/home/yemi-co/NetBeansProjects/DistributedAviationDisruptionAnalyticsSystem/project-images/web.png");
        Image webImg = webIcon.getImage().getScaledInstance(40, 25, Image.SCALE_SMOOTH);
        webLabel = new JLabel(new ImageIcon(webImg));
        webLabel.setBounds(10, 5, 32, 20);
        add(webLabel);

        // heading
        //heading = new JLabel("Admin Login");
        //heading.setFont(new Font("Tahoma", Font.BOLD, 24));
        //heading.setBounds(385, 25, 220, 35);
        //add(heading);

        // labels
        userLabel = new JLabel("Enter Your Username:");
        userLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        userLabel.setBounds(40, 110, 190, 28);
        add(userLabel);

        passLabel = new JLabel("Enter Your Password:");
        passLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        passLabel.setBounds(40, 165, 190, 28);
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

        // login button
        ImageIcon lgIcon = new ImageIcon("/home/yemi-co/NetBeansProjects/DistributedAviationDisruptionAnalyticsSystem/project-images/login.png");
        Image lgImg = lgIcon.getImage().getScaledInstance(23, 23, Image.SCALE_SMOOTH);
        loginButton = new JButton("Login", new ImageIcon(lgImg));
        loginButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        loginButton.setBounds(40, 235, 140, 42);
        loginButton.setFocusPainted(false);
        loginButton.setIconTextGap(10);
        loginButton.addActionListener(this);
        add(loginButton);

        // register button
        ImageIcon regIcon = new ImageIcon("/home/yemi-co/NetBeansProjects/DistributedAviationDisruptionAnalyticsSystem/project-images/register.jpg");
        Image regImg = regIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        registerButton = new JButton("Register", new ImageIcon(regImg));
        registerButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        registerButton.setBounds(195, 235, 155, 42);
        registerButton.setFocusPainted(false);
        registerButton.setIconTextGap(10);
        registerButton.addActionListener(this);
        add(registerButton);

        // reset button
        ImageIcon resetIcon = new ImageIcon("/home/yemi-co/NetBeansProjects/DistributedAviationDisruptionAnalyticsSystem/project-images/reset.jpg");
        Image resetImg = resetIcon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        resetButton = new JButton("Reset", new ImageIcon(resetImg));
        resetButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        resetButton.setBounds(365, 235, 140, 42);
        resetButton.setFocusPainted(false);
        resetButton.setIconTextGap(10);
        resetButton.addActionListener(this);
        add(resetButton);

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
        if (ae.getSource() == loginButton) {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter username and password.");
                return;
            }

            Conn conn = new Conn();

            if (conn.getConnection() == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed. Check MySQL password and JDBC driver.");
                return;
            }

            try {
                String sql = "SELECT * FROM login WHERE username = ? AND password = ?";
                PreparedStatement ps = conn.getConnection().prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login successful.");
                    setVisible(false);
                    new AviationClientApp();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid login details.");
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                conn.close();
            }

        } else if (ae.getSource() == registerButton) {
            setVisible(false);
            new Register();
        } else if (ae.getSource() == resetButton) {
            userField.setText("");
            passField.setText("");
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}