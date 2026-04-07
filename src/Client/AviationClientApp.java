/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client;

/**
 *
 * @author yemi-co
 * Main client GUI used to upload CSV files and request Python analysis.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


public class AviationClientApp extends JFrame implements ActionListener {

    JButton uploadButton, analyzeButton, closeButton;
    JTextArea logArea;

    private static final String SERVER_HOST = "127.0.0.1";
    private static final int SERVER_PORT = 10001;

    public AviationClientApp() {
        setTitle("Aviation Data Client");
        setSize(700, 450);
        setLocation(350, 170);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        JLabel heading = new JLabel("Aviation Data Client");
//        heading.setFont(new Font("Tahoma", Font.BOLD, 15));
//        heading.setBounds(240, 20, 250, 30);
//        add(heading);

        uploadButton = new JButton("Upload CSV Dataset");
        uploadButton.setBounds(90, 90, 200, 40);
        uploadButton.addActionListener(this);
        add(uploadButton);

        analyzeButton = new JButton("Analyze");
        analyzeButton.setBounds(390, 90, 180, 40);
        analyzeButton.addActionListener(this);
        add(analyzeButton);

        closeButton = new JButton("Close");
        closeButton.setBounds(260, 150, 150, 40);
        closeButton.addActionListener(this);
        add(closeButton);

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane sp = new JScrollPane(logArea);
        sp.setBounds(60, 220, 580, 140);
        add(sp);

        setVisible(true);
    }

    private void log(String msg) {
        logArea.append(msg + "\n");
    }

    private void uploadCsvFile() {
        JFileChooser chooser = new JFileChooser(new File("Dataset"));
        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            if (!file.getName().toLowerCase().endsWith(".csv")) {
                JOptionPane.showMessageDialog(this, "Please select a CSV file.");
                return;
            }

            try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
                 DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                 DataInputStream dis = new DataInputStream(socket.getInputStream());
                 FileInputStream fis = new FileInputStream(file)) {

                dos.writeUTF("UPLOAD");
                dos.writeUTF(file.getName());
                dos.writeLong(file.length());

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                }
                dos.flush();

                String response = dis.readUTF();
                log(response);
                JOptionPane.showMessageDialog(this, response);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Upload failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void requestAnalysis() {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             DataInputStream dis = new DataInputStream(socket.getInputStream())) {

            dos.writeUTF("ANALYZE");
            dos.flush();

            String status = dis.readUTF();

            if ("OK".equals(status)) {
                String reportName = dis.readUTF();
                long fileSize = dis.readLong();

                File reportsDir = new File("Reports");
                if (!reportsDir.exists()) {
                    reportsDir.mkdirs();
                }

                File outputFile = new File(reportsDir, reportName);

                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[4096];
                    long remaining = fileSize;
                    int bytesRead;

                    while (remaining > 0 &&
                            (bytesRead = dis.read(buffer, 0, (int)Math.min(buffer.length, remaining))) != -1) {
                        fos.write(buffer, 0, bytesRead);
                        remaining -= bytesRead;
                    }
                }

                log("Report received successfully: " + outputFile.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Report downloaded to:\n" + outputFile.getAbsolutePath());

                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(outputFile);
                }

            } else {
                String errorMsg = dis.readUTF();
                JOptionPane.showMessageDialog(this, "Analysis failed: " + errorMsg);
                log("Analysis failed: " + errorMsg);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Analyze failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == uploadButton) {
            uploadCsvFile();
        } else if (ae.getSource() == analyzeButton) {
            requestAnalysis();
        } else if (ae.getSource() == closeButton) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new AviationClientApp();
    }
}