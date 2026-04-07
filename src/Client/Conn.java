/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client;

/**
 * 
 * @author yemi-co
 */

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conn {

    private static final String URL =
            "jdbc:mysql://localhost:3306/aviationDB?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "LOQ23!wel";   // change this password to the mysql password

    public Connection c;
    public Statement s;

    public Conn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(URL, USER, PASSWORD);
            s = c.createStatement();
            System.out.println("Client database connected successfully.");
        } catch (Exception e) {
            c = null;
            s = null;
            System.out.println("Client database connection error: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Client DB connection failed:\n" + e.getMessage());
        }
    }

    public Connection getConnection() {
        return c;
    }

    public void close() {
        try {
            if (s != null) s.close();
            if (c != null) c.close();
        } catch (SQLException e) {
            System.out.println("Client close connection error: " + e.getMessage());
        }
    }
}
