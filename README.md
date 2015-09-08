Vehicle Fleet
=============

Demo system for RentMe fleet of connected rental trucks. Each truck in
our fleet sends us telemetry data updates, including location, heading
and various internal indicators, including whether of not a service is
required. The user can browse all the vehicle locations via a zoomable
map with an inset containing all the vehicle details. Vehicles that
need a service are shown in orange or red depending on the urgency of
the repairs. Service stations can be shown on the map by selecting a
menu item.

## Architecture

![Architecture Diagram](https://raw.githubusercontent.com/springone2gx2015/vehicle-fleet-demo/master/architecture.png)

## Building from Source

This application uses a [Maven][]-based build system. Please ensure
Java 8 is installed and use the wrapper script (`mvnw` in the root of
the project).

### Prerequisites

* [Git][]
* [JDK 8][]
* [MongoDB][]
* [RabbitMQ][]

> NOTE: You can run the middleware in docker containers using
> docker-compose (works best on a Linux host because otherwise you
> need to configure the host because it isn't localhost with
> boot2docker).

### Check out sources

	$ git clone https://github.com/springone2gx2015/vehicle-fleet-demo.git
    $ cd vehicle-fleet-demo

### Compile, test and build all jars

	$ ./mvnw clean install

## Running

Each of the projects has a jar file in its `target/` directory. Run it
with `java -jar`. There are `platform` services (in `platform/*`) that
are needed if you run the application in the "default" profile. You
can run locally without them if you use the "test" profile
(e.g. `--spring.profiles.active=test` on the command line). In Cloud
Foundry the platform services are provided by Spring Cloud Services
(available as a tile in PCF).

### Locally

| Module                     | Dashboard URL                   |
|----------------------------|---------------------------------|
| Eureka                     | http://localhost:8761/          |
| Confgiserver               | http://localhost:8888/          |
| fleet-location-service     | http://localhost:9000/          |
| service-location-service   | http://localhost:9001/          |
| fleet-location-simulator   | http://localhost:9005/          |
| fleet-location-ingest      | http://localhost:9006/          |
| fleet-location-updater     | http://localhost:9007/          |
| RabbitMQ                   | http://localhost:15672/         |
| MongoDB                    | http://localhost:27017/         |
| Dashboard                  | http://localhost:8080/          |

In order to run the entire application locally please execute the following steps:

Please ensure that you have running:

* Rabbit - for the simulator, ingest and updater components
* Mongo - for the service-location-service

> NOTE: the dashboard UI can run on it's own with just the
> `fleet-location-service` and `service-location-service` (if you run
> in a "test" profile) and with those 2 plus eureka (in the "default"
> profile).

**Start Eureka**

	$ java -jar platform/eureka/target/fleet-eureka-server-0.0.1-SNAPSHOT.jar

**fleet-location-simulator**

	$ java -jar fleet-location-simulator/target/fleet-location-simulator-1.0.0.BUILD-SNAPSHOT.jar

**fleet-location-ingest**

	$ java -jar fleet-location-ingest/target/fleet-location-ingest-1.0.0.BUILD-SNAPSHOT.jar

**fleet-location-updater**

	$ java -jar fleet-location-updater/target/fleet-location-updater-1.0.0.BUILD-SNAPSHOT.jar

**fleet-location-service**

	$ java -jar fleet-location-service/target/fleet-location-service-1.0.0.BUILD-SNAPSHOT.jar
	$ wget http://assets.springone2gx2015.s3.amazonaws.com/fleet/fleet.json
	$ curl -H "Content-Type: application/json" localhost:9000/fleet -d @fleet.json

**service-location-service**

	$ java -jar fleet-location-service/target/fleet-location-service-1.0.0.BUILD-SNAPSHOT.jar
	$ wget http://assets.springone2gx2015.s3.amazonaws.com/fleet/serviceLocations.json
	$ curl -H "Content-Type: application/json" localhost:9001/bulk/serviceLocations -d @serviceLocations.json

**dashboard**

	$ java -jar dashboard/target/dashboard-1.0.0.BUILD-SNAPSHOT.jar

If you go to the Eureka Dashboard, you should see all services registered and running:

http://localhost:8761/

Please ensure all services started successfully. Next, start the simulation using
the `service-location-simulator` application, either through the simple UI at http://localhost:9005/
or by executing:

	$ curl http://localhost:9005/api/dc

You should now be able to see the moving vehicles inside the main `Dashboard` application:

http://localhost:8080/

[Git]: https://help.github.com/articles/set-up-git/
[JDK 8]: http://www.oracle.com/technetwork/java/javase/downloads
[Maven]: https://maven.apache.org/
[MongoDB]: https://www.mongodb.org/
[RabbitMQ]: https://www.rabbitmq.com/
