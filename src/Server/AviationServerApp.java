/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

/**
 *
 * @author yemi-co
 */

import java.io.*;
import java.net.*;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AviationServerApp {

    private static final int PORT = 10001;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Aviation Server started on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress());
                new Thread(new ClientHandler(socket)).start();
            }

        } catch (Exception e) {
            System.out.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (DataInputStream dis = new DataInputStream(socket.getInputStream());
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {

            String command = dis.readUTF();
            System.out.println("Received command: " + command);

            if ("UPLOAD".equalsIgnoreCase(command)) {
                handleUpload(dis, dos);
            } else if ("ANALYZE".equalsIgnoreCase(command)) {
                handleAnalyze(dos);
            } else {
                dos.writeUTF("Unknown command.");
            }

        } catch (Exception e) {
            System.out.println("Client handler error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception ignored) {
            }
        }
    }

    private void handleUpload(DataInputStream dis, DataOutputStream dos) throws Exception {
        String fileName = dis.readUTF();
        long fileSize = dis.readLong();

        File uploadDir = new File("Dataset");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        File outputFile = new File(uploadDir, fileName);

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[4096];
            long remaining = fileSize;
            int bytesRead;

            while (remaining > 0 &&
                    (bytesRead = dis.read(buffer, 0, (int) Math.min(buffer.length, remaining))) != -1) {
                fos.write(buffer, 0, bytesRead);
                remaining -= bytesRead;
            }
        }

        try {
            String tableName = fileName.replace(".csv", "").trim();
            importCsvIntoDatabase(outputFile, tableName);

            String msg = "File uploaded and inserted into database successfully: " + fileName;
            System.out.println(msg);
            dos.writeUTF(msg);

        } catch (Exception e) {
            String errorMsg = "Upload/import failed: " + e.getMessage();
            System.out.println(errorMsg);
            e.printStackTrace();
            dos.writeUTF(errorMsg);
        }
    }

    private void importCsvIntoDatabase(File file, String tableName) throws Exception {
        Conn conn = new Conn();

        if (conn.getConnection() == null) {
            throw new Exception("Server database connection failed. Check MySQL driver and password.");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                throw new Exception("CSV file is empty.");
            }

            String[] headers = headerLine.split(",");

            conn.getConnection().createStatement().executeUpdate("TRUNCATE TABLE " + tableName);

            StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
            for (int i = 0; i < headers.length; i++) {
                sql.append(headers[i].trim());
                if (i < headers.length - 1) sql.append(", ");
            }
            sql.append(") VALUES (");
            for (int i = 0; i < headers.length; i++) {
                sql.append("?");
                if (i < headers.length - 1) sql.append(", ");
            }
            sql.append(")");

            PreparedStatement ps = conn.getConnection().prepareStatement(sql.toString());

            String line;
            int rowCount = 0;

            while ((line = br.readLine()) != null) {
                String[] values = parseCsvLine(line);

                for (int i = 0; i < headers.length; i++) {
                    String column = headers[i].trim();
                    String value = i < values.length ? values[i].trim() : "";
                    setPreparedValue(ps, i + 1, tableName, column, value);
                }

                ps.executeUpdate();
                rowCount++;
            }

            System.out.println("Inserted " + rowCount + " rows into table: " + tableName);

        } finally {
            conn.close();
        }
    }

    private void setPreparedValue(PreparedStatement ps, int index, String tableName, String column, String value) throws Exception {
        if (value == null || value.isEmpty()) {
            ps.setString(index, null);
            return;
        }

        if ("flight_cancellations".equals(tableName) && "date".equals(column)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy");
            LocalDate localDate = LocalDate.parse(value, formatter);
            ps.setDate(index, Date.valueOf(localDate));
            return;
        }

        if (column.equals("estimated_daily_loss_usd") ||
            column.equals("additional_distance_km") ||
            column.equals("additional_fuel_cost_usd")) {
            ps.setDouble(index, Double.parseDouble(value));
            return;
        }

        if (column.equals("cancelled_flights") ||
            column.equals("rerouted_flights") ||
            column.equals("passengers_impacted") ||
            column.equals("flights_cancelled") ||
            column.equals("flights_delayed") ||
            column.equals("flights_diverted") ||
            column.equals("delay_minutes")) {
            ps.setInt(index, Integer.parseInt(value));
            return;
        }

        ps.setString(index, value);
    }

    private String[] parseCsvLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    private void handleAnalyze(DataOutputStream dos) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("python3", "Python/aviation_analysis.py");
        pb.redirectErrorStream(true);

        Process process = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("[PYTHON] " + line);
        }

        int exitCode = process.waitFor();

        File reportFile = new File("Reports/aviation_report.pdf");

        if (exitCode == 0 && reportFile.exists()) {
            dos.writeUTF("OK");
            dos.writeUTF(reportFile.getName());
            dos.writeLong(reportFile.length());

            try (FileInputStream fis = new FileInputStream(reportFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                }
            }
            dos.flush();
            System.out.println("PDF report sent to client successfully.");
        } else {
            dos.writeUTF("ERROR");
            dos.writeUTF("Python analysis failed or report file not found.");
        }
    }
}
