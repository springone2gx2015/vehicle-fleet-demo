
## Binding to Spring Cloud Eureka and Config Server
cf bind-service fleet-location-actuator-ui eureka
cf bind-service fleet-location-actuator-ui configserver

## Pushing to CF
cf push fleet-location-actuator-ui -p PATHTO.jar -n actuator

