# geolocation_system
Utilizing Kafka to demonstrate POC.

## Prerequisite

- Java 21
- Docker


### Kafka Topics used

| topic name                    | usage                                                                        |
|-------------------------------|------------------------------------------------------------------------------|
| poc-location-data-collection  | to collect data from vehicle's GPS module                                    |
| poc-live-location-data        | collected data and processed it to check whether the car is within the fence.|
| poc-update-external-device    | sending car's live location to external device if car is out of range        |


## Architecture

![image](https://github.com/user-attachments/assets/f2f95049-a6e0-4e9f-8329-4a986ee767eb)
