Service Location Simulator
==========================

* KML Integration
* Generic Messaging Integration

Obtaining of Directions:

* Google Directions Api
  - Direction input (From + To address) can be provided via API
  - Direction input can be loaded from an input file

## Requirements

* Java 8
* Maven

### Optional

If you like to update the `fixture.json` file, you may want to use the Google Directions Api:

* https://console.developers.google.com/project

Please ensure that you set your Google API key for the Google Directions API. E.g. you can set the relevant property in the `application.yml` file:

```
gpsSimmulator:
  googleApiKey: 1234567
```

You can also provide the key via the command line:

	$ gpsSimmulator_googleApiKey=1234567 java -jar target/gps-vehicle-simulator-1.x.x.BUILD-SNAPSHOT.jar

So you can build the project e.g. using:

	$ mvn clean package -DgpsSimmulator.googleApiKey=1234567

There are a few tests which are using the Google Directions Api. These are disabled by default. In order to enabled them, provide the following system property:

* mvn clean package -Duse-google-directions=true

## Important Configuration Files

### directions.json

Contains the **from** and **to** addresses. This file is **NOT** used during actual run-time but is used when creating the contents for `fixture.json`.

### fixture.json

This is the main simulator fixture. Contains simulator settings (e.g. the speed pf the vehicle) as well as the actual path to be driven. If you like to update the file hit the the Json endpoint: `http://localhost:8080/api/fixture` and copy the result to `fixture.json`.

## Building

	$ mvn clean package

## Execution

	$ java -jar target/gps-vehicle-simulator-1.x.x.BUILD-SNAPSHOT.jar

Start the Washington DC simulation by opening:

	http://localhost:8080/api/dc

This will create 8 vehicles driving with 50 km/h through Washington DC. Some of these vehicle have different vehicle statuses. Some of the vehicles will fall into an error state after 60 seconds.

For a quick visualization, open the `gps.kml` file in Google Earth. You should see 3 place-marks in the Washington DC area.

## REST Api

* GET http://localhost:8080/api/dc (Loads directions from JSON file and Google Directions API))
* GET http://localhost:8080/api/status
* GET http://localhost:8080/api/cancel (Cancels all vehicles)
* GET http://localhost:8080/api/fixture (Creates content for `fixture.json`)
* GET http://localhost:8080/api/directions
* GET http://localhost:8080/api/kml/{instanceId} (used by `gps.kml`)
