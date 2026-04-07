import os
import pandas as pd
import pymysql
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
from fpdf import FPDF

# -------------------------------------------------
# STUDENT NAME
# -------------------------------------------------
YOUR_NAME = "OPEYEMI OMOTOSHO"

# -------------------------------------------------
# PATH SETUP
# -------------------------------------------------
CURRENT_DIR = os.path.dirname(os.path.abspath(__file__))
PROJECT_ROOT = os.path.abspath(os.path.join(CURRENT_DIR, ".."))
REPORTS_DIR = os.path.join(PROJECT_ROOT, "Reports")

os.makedirs(REPORTS_DIR, exist_ok=True)

REPORT_PATH = os.path.join(REPORTS_DIR, "aviation_report.pdf")
CHART1_PATH = os.path.join(REPORTS_DIR, "chart_cancellations_by_airline.png")
CHART2_PATH = os.path.join(REPORTS_DIR, "chart_cancellations_by_airport.png")
CHART3_PATH = os.path.join(REPORTS_DIR, "chart_cancellation_reasons.png")

# -------------------------------------------------
# HELPER FUNCTION
# -------------------------------------------------
def save_bar_chart(series, title, xlabel, ylabel, output_path):
    plt.figure(figsize=(10, 6))
    series.plot(kind="bar")
    plt.title(title)
    plt.xlabel(xlabel)
    plt.ylabel(ylabel)
    plt.tight_layout()
    plt.savefig(output_path)
    plt.close()
    print(f"Saved chart: {output_path}")

# -------------------------------------------------
# MAIN
# -------------------------------------------------
def main():
    conn = None

    try:
        conn = pymysql.connect(
            host="localhost",
            user="root",
            password="LOQ23!wel",
            database="aviationDB",
            charset="utf8mb4"
        )

        print("Connected to MySQL successfully.")

        # -------------------------------------------------
        # LOAD DATA FROM DATABASE
        # -------------------------------------------------
        df_losses = pd.read_sql("SELECT * FROM airline_losses_estimate", conn)
        df_airports = pd.read_sql("SELECT * FROM airport_disruptions", conn)
        df_closures = pd.read_sql("SELECT * FROM airspace_closures", conn)
        df_cancellations = pd.read_sql("SELECT * FROM flight_cancellations", conn)
        df_reroutes = pd.read_sql("SELECT * FROM flight_reroutes", conn)

        print("Loaded all tables from aviationDB.")

        # Convert selected numeric columns safely
        numeric_columns_losses = [
            "estimated_daily_loss_usd",
            "cancelled_flights",
            "rerouted_flights",
            "passengers_impacted"
        ]
        for col in numeric_columns_losses:
            df_losses[col] = pd.to_numeric(df_losses[col], errors="coerce")

        numeric_columns_airports = [
            "flights_cancelled",
            "flights_delayed",
            "flights_diverted"
        ]
        for col in numeric_columns_airports:
            df_airports[col] = pd.to_numeric(df_airports[col], errors="coerce")

        numeric_columns_reroutes = [
            "additional_distance_km",
            "additional_fuel_cost_usd",
            "delay_minutes"
        ]
        for col in numeric_columns_reroutes:
            df_reroutes[col] = pd.to_numeric(df_reroutes[col], errors="coerce")

        # -------------------------------------------------
        # ANALYSIS 1: Cancellations by Airline
        # -------------------------------------------------
        cancellations_by_airline = (
            df_cancellations.groupby("airline")
            .size()
            .sort_values(ascending=False)
        )

        top_airline_name = cancellations_by_airline.idxmax() if not cancellations_by_airline.empty else "N/A"
        top_airline_value = int(cancellations_by_airline.max()) if not cancellations_by_airline.empty else 0

        save_bar_chart(
            cancellations_by_airline,
            "Cancellations by Airline",
            "Airline",
            "Number of Cancellations",
            CHART1_PATH
        )

        # -------------------------------------------------
        # ANALYSIS 2: Cancellations by Airport
        # -------------------------------------------------
        cancellations_by_airport = (
            df_cancellations.groupby("airport")
            .size()
            .sort_values(ascending=False)
        )

        top_airport_name = cancellations_by_airport.idxmax() if not cancellations_by_airport.empty else "N/A"
        top_airport_value = int(cancellations_by_airport.max()) if not cancellations_by_airport.empty else 0

        save_bar_chart(
            cancellations_by_airport,
            "Cancellations by Airport",
            "Airport",
            "Number of Cancellations",
            CHART2_PATH
        )

        # -------------------------------------------------
        # ANALYSIS 3: Top Cancellation Reasons
        # -------------------------------------------------
        top_cancellation_reasons = (
            df_cancellations["cancellation_reason"]
            .value_counts()
            .sort_values(ascending=False)
        )

        top_reason_name = top_cancellation_reasons.idxmax() if not top_cancellation_reasons.empty else "N/A"
        top_reason_value = int(top_cancellation_reasons.max()) if not top_cancellation_reasons.empty else 0

        save_bar_chart(
            top_cancellation_reasons,
            "Top Cancellation Reasons",
            "Reason",
            "Count",
            CHART3_PATH
        )

        # -------------------------------------------------
        # ANALYSIS 4: Average Reroute Delay
        # -------------------------------------------------
        average_reroute_delay = df_reroutes["delay_minutes"].mean()
        if pd.isna(average_reroute_delay):
            average_reroute_delay = 0.0

        # -------------------------------------------------
        # ANALYSIS 5: Total Airline Loss
        # -------------------------------------------------
        total_airline_loss = df_losses["estimated_daily_loss_usd"].sum()
        if pd.isna(total_airline_loss):
            total_airline_loss = 0.0

        # -------------------------------------------------
        # EXTRA SUMMARY VALUES
        # -------------------------------------------------
        total_cancelled_flights = len(df_cancellations)
        total_rerouted_flights = len(df_reroutes)
        total_airspace_closures = len(df_closures)
        total_airports_disrupted = len(df_airports)

        # -------------------------------------------------
        # CREATE PDF REPORT
        # -------------------------------------------------
        pdf = FPDF()
        pdf.set_auto_page_break(auto=True, margin=15)

        # Cover page
        pdf.add_page()
        pdf.set_font("Arial", "B", 16)
        pdf.cell(0, 10, "Aviation Disruption Analytics Report", ln=True, align="C")

        pdf.ln(8)
        pdf.set_font("Arial", "", 12)
        pdf.cell(0, 10, f"Prepared by: {YOUR_NAME}", ln=True)
        pdf.cell(0, 10, "System: Distributed Aviation Disruption Analytics System", ln=True)
        pdf.cell(0, 10, "Database: aviationDB", ln=True)

        pdf.ln(5)
        pdf.multi_cell(
            0,
            8,
            "This report summarizes aviation disruption analytics generated from "
            "uploaded CSV datasets. The workflow includes client upload, server-side "
            "database storage, Python analytics, chart generation, and automated PDF reporting."
        )

        # Summary page
        pdf.add_page()
        pdf.set_font("Arial", "B", 14)
        pdf.cell(0, 10, "Executive Summary", ln=True)

        pdf.set_font("Arial", "", 12)
        pdf.ln(4)
        pdf.cell(0, 10, f"Total cancelled flights records: {total_cancelled_flights}", ln=True)
        pdf.cell(0, 10, f"Total rerouted flights records: {total_rerouted_flights}", ln=True)
        pdf.cell(0, 10, f"Total airspace closure records: {total_airspace_closures}", ln=True)
        pdf.cell(0, 10, f"Total airport disruption records: {total_airports_disrupted}", ln=True)
        pdf.cell(0, 10, f"Top airline by cancellations: {top_airline_name} ({top_airline_value})", ln=True)
        pdf.cell(0, 10, f"Top airport by cancellations: {top_airport_name} ({top_airport_value})", ln=True)
        pdf.multi_cell(0, 10, f"Top cancellation reason: {top_reason_name} ({top_reason_value})")
        pdf.cell(0, 10, f"Average reroute delay: {average_reroute_delay:.2f} minutes", ln=True)
        pdf.cell(0, 10, f"Total estimated airline daily loss: ${total_airline_loss:,.2f}", ln=True)

        # Analysis details page
        pdf.add_page()
        pdf.set_font("Arial", "B", 14)
        pdf.cell(0, 10, "Detailed Findings", ln=True)

        pdf.set_font("Arial", "", 12)
        pdf.ln(5)
        pdf.multi_cell(0, 8, f"1. The airline with the most flight cancellations is {top_airline_name} with {top_airline_value} cancellations.")
        pdf.multi_cell(0, 8, f"2. The airport with the most cancellations is {top_airport_name} with {top_airport_value} cancellations.")
        pdf.multi_cell(0, 8, f"3. The most common cancellation reason is '{top_reason_name}', appearing {top_reason_value} times.")
        pdf.multi_cell(0, 8, f"4. The average delay caused by rerouting is {average_reroute_delay:.2f} minutes.")
        pdf.multi_cell(0, 8, f"5. The total estimated airline financial loss per day is ${total_airline_loss:,.2f}.")

        # Charts pages
        for title, img_path in [
            ("Chart 1: Cancellations by Airline", CHART1_PATH),
            ("Chart 2: Cancellations by Airport", CHART2_PATH),
            ("Chart 3: Top Cancellation Reasons", CHART3_PATH),
        ]:
            if os.path.exists(img_path):
                pdf.add_page()
                pdf.set_font("Arial", "B", 14)
                pdf.cell(0, 10, title, ln=True)
                pdf.ln(5)
                pdf.image(img_path, x=10, y=30, w=190)

        pdf.output(REPORT_PATH)
        print(f"PDF report generated successfully at: {REPORT_PATH}")

    except Exception as e:
        print(f"Python analysis failed: {e}")
        raise

    finally:
        if conn is not None:
            conn.close()
            print("Database connection closed.")

if __name__ == "__main__":
    main()
