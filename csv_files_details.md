# CSV Files Provided

## airline_losses_estimate.csv

 | Field                      | Description |
 | -------------------------- | -------------------------------- |
 | airline                    | Airline name  |
 | country                    | Airline home country |
 | estimated_daily_loss_usd   | Estimated daily financial loss |
 | cancelled_flights          | Cancelled flights |
 | rerouted_flights           | Rerouted flights |
 | passengers_impacted        | Passengers affected |

## airport_disruptions.csv

 | Field              |  Description |
 | ------------------- | ------------------- |
 | airport             | Airport name |
 | country             | Airport country |
 | flights_cancelled   | Cancelled flights |
 | flights_delayed     | Delayed flights |
 | flights_diverted    | Diverted flights |
 | runway_status       | Runway status |

## airspace_closures.csv

 | Field            | Description |
 | ---------------- | ----------------- |
 | country          | Country |
 | region           | Affected region |
 | closure_reason   | Reason |

## flight_cancellations.csv

 | Field                 | Description |
 | --------------------- | --------------------- |
 | date                  | Cancellation date | 
 | airport               | Airport |
 | airline               | Airline |
 | origin                | Origin airport |
 | destination           | Destination airport |
 | cancellation_reason   | Reason |

## flight_reroutes.csv

 | Field                      | Description |
 | -------------------------- | ------------------- |
 | flight_id                  | Flight identifier |
 | airline                    | Airline |
 | original_route             | Planned route |
 | diverted_route             | Rerouted path |
 | additional_distance_km     | Extra distance |
 | additional_fuel_cost_usd   | Additional cost |
 | delay_minutes              | Delay time |

------------------------------------------------------------------------
