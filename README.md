# Final Project: Distributed Aviation Disruption Analytics System

## Course

**MSDS 535 -- Further Mainstream Programming Languages for Data
Science**

**Due:** April 12, 2026 -- 07:00 PM US Central Time\
Submit by pushing your project repository to **GitHub**.

------------------------------------------------------------------------

# Project Overview

In this final project, students will design and implement a
**distributed client--server data analytics system** for analyzing
aviation disruption datasets.

The system integrates:

-   Graphical user interface
-   Network communication
-   Database storage
-   Data analytics
-   Automated report generation

Students will upload aviation disruption datasets (**CSV format**)
through a **Java Swing GUI client**. The server receives the dataset,
stores it in a **MySQL database**, and triggers a **Python analytics
engine** that performs statistical analysis and visualization.

The analytics engine generates charts and produces a **professional PDF
analytics report**.

This project simulates a **real-world analytics pipeline** integrating
data ingestion, distributed processing, database management, and
automated reporting.

------------------------------------------------------------------------

# Learning Outcomes (ABET Student Outcomes)

This project addresses the following **ABET Student Outcomes**.

## SO 1.3 -- Select Tools and Methods to Address a Data Science Problem

Students will integrate appropriate technologies:

-   Java Swing GUI
-   TCP Socket Programming
-   MySQL Database
-   Python Analytics
-   Pandas and Matplotlib
-   FPDF PDF generation

Students demonstrate their ability to **select and combine tools to
solve a real-world aviation analytics problem**.

------------------------------------------------------------------------

## SO 2.1 -- Design a Data Science Solution

Students will design a **complete data science pipeline**:

1.  Data ingestion from CSV datasets
2.  Network-based data transfer
3.  Database storage and management
4.  Data preprocessing and cleaning
5.  Analytical processing
6.  Visualization and reporting

The final system must produce a structured **Aviation Disruption
Analytics Report**.

------------------------------------------------------------------------

## SO 6.2 -- Understanding Big Data Programming Paradigms

Students demonstrate knowledge of **data pipeline architectures** by
implementing:

-   Distributed client--server communication
-   Database-driven analytics
-   Data pipeline workflow
-   Automated reporting

------------------------------------------------------------------------

# System Architecture

Client Layer (Java Swing GUI)

↓

Network Layer (TCP Socket Communication)

↓

Server Layer (Java Server)

↓

Data Layer (MySQL Database)

↓

Analytics Layer (Python Data Analytics)

↓

Output Layer (PDF Analytics Report)

------------------------------------------------------------------------

# System Components

## 1. Java Client Application

Functions:

-   Upload CSV datasets
-   Send datasets to server
-   Request analytics execution
-   Receive generated PDF report

Technologies:

-   Java Swing
-   Socket Programming
-   File Handling

------------------------------------------------------------------------

## 2. Java Server Application

Responsibilities:

-   Accept socket connections
-   Receive CSV files
-   Parse datasets
-   Insert records into MySQL
-   Execute Python analytics
-   Return generated report

Technologies:

-   Java
-   JDBC
-   Multithreading
-   Socket Programming

------------------------------------------------------------------------

## 3. MySQL Database

Database name: **aviationDB**

Tables: The database contains the following tables:

1. login
2. airline_losses_estimate
3. airport_disruptions
4. airspace_closures
5. flight_cancellations
6. flight_reroutes

For details about **aviationDB** database schema, [Read database_schema.md](./database_schema.md).

------------------------------------------------------------------------

# Project Structure and Step-by-Step Implementation

* For details about project structure, [Read project_structure.md](./project_structure.md).
* For details about step-by-step AviationServerApp, AviationClientApp implementation , [Read Snapshots.md](./Snapshots.md).

------------------------------------------------------------------------

# Python Data Analytics Engine

Libraries:

-   pandas
-   matplotlib
-   mysql-connector
-   fpdf

Analytics tasks:

1.  Top airlines with cancellations
2.  Airports with highest disruptions
3.  Top cancellation reasons
4.  Average reroute delay
5.  Estimated airline financial losses

Charts generated:

-   Cancellations by Airline
-   Cancellations by Airport
-   Top Cancellation Reasons

------------------------------------------------------------------------

# Dataset

Dataset title:

**Global Civil Aviation Disruption 2026 -- Iran--US Conflict Impact
Dataset**

The dataset documents aviation disruptions following the **February 28,
2026 U.S. airstrikes on Iranian facilities**.

Dataset coverage includes:

-   Airspace closures across 25+ countries
-   Flight disruptions for 35+ airlines
-   Financial loss estimates
-   Airport operational disruptions

Dataset (CSV files) are available [here](./Dataset).

------------------------------------------------------------------------

# CSV Files Provided

1. airline_losses_estimate.csv
2. airport_disruptions.csv
3. airspace_closures.csv
4. flight_cancellations.csv
5. flight_reroutes.csv

For details about the CSV files, [Read CSV_files_details.md](./csv_files_details.md). CSV

------------------------------------------------------------------------

# Academic Integrity

Students must develop their own implementations. Collaboration is allowed for discussion, but submitted code must be original.

------------------------------------------------------------------------

# Real-World Relevance

This project reflects real data systems used in:

- Aviation analytics platforms
- Airline operations centers
- Transportation disruption monitoring
- Data-driven decision support systems

Students completing this project gain experience in **distributed systems, data analytics pipelines, and automated reporting architectures used in industry.**

------------------------------------------------------------------------

# Evaluation Criteria (Aligned with ABET Outcomes)

 | Component                      | ABET Outcome   | Related Files (to be submitted)   | Points |
 | ------------------------------ | -------------- | --------------------------------- | -------- |
 | Admin Login GUI                | SO 1.3         | [Login.java](./Client/Login.java), [Register.java](./Client/Register.java)         | 15 |
 | Aviation Data Client GUI       | SO 1.3         | [AviationClientApp.java](./Client/AviationClientApp.java)            | 15 |
 | Server Socket Implementation   | SO 6.2         | [AviationServerApp.java](./Server/AviationServerApp.java)            | 15 |
 | Database Integration           | SO 2.1         | [my_aviationDB.sql](./Database/my_aviationDB.sql) / [JDBC code](./Server/Conn.java)            | 15 |
 | Python Data Analytics          | SO 2.1         | [aviation_analysis.py](./Python/aviation_analysis.py)              | 20 |
 | PDF Report Generation          | SO 1.3         | [aviation_report.pdf](./Reports/aviation_report.pdf)               | 10 |
 | Project Demonstration          | SO 6.2         | Live Demo                         | 10 |

**Total: 100 Points**

------------------------------------------------------------------------

# Submission Requirements

Students must submit:

* Java Source Code
1. [Login.java](./Client/Login.java)
2. [Register.java](./Client/Register.java)
3. [AviationClientApp.java](./Client/AviationClientApp.java)
4. [AviationServerApp.java](./Server/AviationServerApp.java)
5. [Conn.java](./Server/Conn.java)
   
* Python Analytics Script
1. [aviation_analysis.py](./Python/aviation_analysis.py)

* SQL Code
1. [my_aviationDB.sql](./Database/my_aviationDB.sql)

* Generated PDF Report
1. [aviation_report.pdf](./Reports/aviation_report.pdf)

------------------------------------------------------------------------

# Important

Students must **schedule a Zoom meeting with the instructor before April 12, 2026** to demonstrate the final project.
Failure to demonstrate the project will result in **0 points** for the
final project.
