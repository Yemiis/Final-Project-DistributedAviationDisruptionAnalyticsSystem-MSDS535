# Recommended Project Folder Structure

Students should organize their GitHub repository using the following
structure:

    Final-Project-26S/
    │
    ├── Client/
    │   ├── Login.java
    │   ├── Register.java
    |   ├── Conn.java
    │   └── AviationClientApp.java
    │
    ├── Server/
    │   ├── AviationServerApp.java
    |   └── Conn.java
    │
    ├── Python/
    │   └── aviation_analysis.py
    │
    ├── Database/
    │   └── my_aviationDB.sql
    │
    ├── Dataset/
    │   ├── airline_losses_estimate.csv
    │   ├── airport_disruptions.csv
    │   ├── airspace_closures.csv
    │   ├── flight_cancellations.csv
    │   └── flight_reroutes.csv
    │
    ├── Reports/
    │   └── aviation_report.pdf
    │
    ├── images/
    │   ├── login.png
    │   ├── LoginUI.png
    │   ├── Register.png
    │   ├── ADC.png
    │   └── ADC1.png
    │
    └── README.md
    └── database_schema.md
    └── csv_files_details.md
    └── Sanpshots.md

## Description

**Client/**\
Contains Java GUI applications used by the administrator to upload
datasets and request analysis.

**Server/**\
Contains the Java socket server responsible for receiving CSV datasets,
inserting them into the database, and triggering the Python analytics
engine.

**Python/**\
Contains the Python analytics script used to perform data analysis and
generate charts and the PDF report.

**Database/**\
Contains SQL scripts required to create database tables.

**Datasets/**\
Contains aviation disruption datasets provided to students.

**Reports/**\
Contains generated PDF reports.

**images/**\
Contains screenshots used in the project documentation.
