# Part 1: Admin Login

## 1. Creating login Table 

Here, you need to create **aviationDB** database and **login** table with the following columns:

- username - Variable-length string with maximum 60 characters
- password - Variable-length string with maximum 60 characters 

The **login** table will be similar to the following:

![login](images/login.png)

## 2. Creating Admin Login User Interface
You need to create User Login Interface for Admin. The interface should contain textfield, passwordField, and also Login, Register and Reset Buttons as similar to the following:  

![LoginUI](images/LoginUI.png)

- textField: This field can be used to enter username of Admin user.
- passwordField: This field can be used to enter password of Admin user.
- Login: This button can be used to log into Admin account and open **Aviation Data Client** interface.
- Register: This button can be used to open Register interface for registering Admin user.
- Reset: This button can be used to clear the text in textField and passwordField.

Note: You need to write your code in Login.java file

## 3. Creating Registration User Interface
You need to create Registration User Interface for Admin. The interface should contain textfield, passwordField, and also Register and Reset Buttons as similar to the following:

![Register](images/Register.png)

- textField: This field can be used to set username of Admin user.
- passwordField: This field can be used to set password of Admin user.
- Register: This button can be used to register Admin user. The username and password should be written into login table as an action of this button.
- Reset: This button can be used to clear the text in textField and passwordField.

Note: You need to write your code in Register.java file.

## 4. Creating User Interface for Aviation Data Client 
You need to create User Interface for Aviation Data Client. The interface should contain **Upload CSV Dataset**, **Python Analyzer**, and **Close** buttons as shown below:

![ADC](images/ADC.png)


![ADC1](images/ADC1.png)

- Upload CSV Dataset: This button can be used to send CSV files (airline_losses_estimate.csv, airport_disruptions.csv, airspace_closures.csv, flight_cancellations.csv, and flight_reroutes.csv) from client to server. On receiving CSV files, the server should store data into **aviationDB** database automatically in the tables **airline_losses_estimate**, **airport_disruptions**, **airspace_closures**, **flight_cancellations**, and **flight_reroutes** respectively.    
- Python Analyzer: On clicking this button, the client will send the request to the server for the report in pdf file (aviation_report.pdf). The server will run the python script (aviation_analysis.py) from the java code for generating and sending the report back to the client.   
- Close: On clicking this button, Aviation Data Client interface should be closed.

Note: You need to write your code for Aviation Data Client interface in AviationClientApp.java file.

## 5. Creating Aviation Server

### 1. Configure MySQL Database
- Install MySQL
- Create a database (e.g., `aviationDB`)
- Update database connection in `Conn.java`:
```java
Connection c = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/aviationDB", "username", "password");
```

---
### 2. Configure MySQL Database Server Setup (Core Networking)

* Create a Java server application using ServerSocket

* Configure the server to run on port 10001

* Implement continuous listening using an infinite loop (while(true))

* Accept incoming client connections using:
```java
serverSocket.accept();
```

* Handle multiple clients concurrently using multithreading

* Create a ClientHandler class implementing Runnable

* Start a new thread for each client

---

### 3. Client Request Handling

* Read client input using:

    * BufferedReader + InputStreamReader

* Process command-based communication

* Support at least two commands:

    * "UPLOAD" → for dataset upload

    * "ANALYZE" → for running analytics and returning results
---

### 3. CSV Upload Functionality

* Receive dataset from client in CSV format

* Read:

    * File name

    * Header row

    * Data rows

* Dynamically generate table name from file name

    * Remove .csv extension

---

### 4. Database Integration (MySQL via JDBC)

* Establish database connection using a helper class (Conn)

* Dynamically create a table using CSV header:

    * All columns should be VARCHAR(100)

* Construct SQL query:

    * CREATE TABLE IF NOT EXISTS

* Insert records using:

    * PreparedStatement (to prevent SQL injection)

* Loop through all rows and store them in the database

---
### 5. Dynamic Query Construction

* Build SQL queries programmatically:

    * CREATE TABLE query from CSV headers

    * INSERT INTO query with placeholders (?)

* Map CSV values to query parameters using:

    * ps.setString()

---
### 6. Data Processing Pipeline

* After upload, allow client to request analysis

* Maintain workflow:

    * Upload → Store → Analyze → Report

---
### 7. Python Integration for Analytics

* Execute external Python script using:

    * ProcessBuilder

* Script file:

    * aviation_analysis.py

* Capture and display Python console output in Java

* Wait for execution using:

    * process.waitFor()

---

### 8. Report Generation and Transfer

* Ensure Python script generates:

    * aviation_report.pdf

* Read PDF file using:

    * FileInputStream

* Send file to client via:

    * DataOutputStream

* Implement buffered file transfer:

    * Use byte array (byte[4096])

---

### 9. File Transmission to Client

* Stream PDF data over socket

* Ensure:

    * Complete file transfer

    * Proper closing of streams

---

### 10. Error Handling and Logging

* Use try-catch blocks for:

    * Networking

    * Database

    * File operations

* Print meaningful logs:

    * Server status

    * Dataset processing

    * Python execution output

---

Note: You need to write your code in AviationServerApp.java file.

---

# Important

Students must **schedule a Zoom meeting with the instructor before April 12, 2026** to demonstrate the final project.
Failure to demonstrate the project will result in **0 points** for the final project.
