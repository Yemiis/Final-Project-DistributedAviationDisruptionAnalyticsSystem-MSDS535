CREATE DATABASE IF NOT EXISTS aviationDB;
USE aviationDB;

CREATE TABLE IF NOT EXISTS login (
    username VARCHAR(60) PRIMARY KEY,
    password VARCHAR(60) NOT NULL
);

CREATE TABLE IF NOT EXISTS airline_losses_estimate (
    airline VARCHAR(100),
    country VARCHAR(100),
    estimated_daily_loss_usd DOUBLE,
    cancelled_flights INT,
    rerouted_flights INT,
    passengers_impacted INT
);

CREATE TABLE IF NOT EXISTS airport_disruptions (
    airport VARCHAR(100),
    country VARCHAR(100),
    flights_cancelled INT,
    flights_delayed INT,
    flights_diverted INT,
    runway_status VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS airspace_closures (
    country VARCHAR(100),
    region VARCHAR(100),
    closure_reason VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS flight_cancellations (
    date DATE,
    airport VARCHAR(100),
    airline VARCHAR(100),
    origin VARCHAR(100),
    destination VARCHAR(100),
    cancellation_reason VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS flight_reroutes (
    flight_id VARCHAR(100),
    airline VARCHAR(100),
    original_route VARCHAR(255),
    diverted_route VARCHAR(255),
    additional_distance_km DOUBLE,
    additional_fuel_cost_usd DOUBLE,
    delay_minutes INT
);
