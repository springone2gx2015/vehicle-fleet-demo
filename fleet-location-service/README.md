# Fleet Location Service

## Demo Initialization

You can do bulk uploads of location data by POSTing to the `/fleet` endpoint. E.g.

```
$ curl -H "Content-Type: application/json" localhost:9000/fleet -d @fleet.json
```

where `fleet.json` is a file containing a JSON array of `Location` objects.
There's a small sample for testing in `src/test/resources`. You can also find
a complete fleet at
http://assets.springone2gx2015.s3.amazonaws.com/fleet/fleet.json
(available for anonymous download).

## Deploying to Cloud Foundry

This app deploys nicely to Cloud Foundry and looks for a config server (via its 
`bootstrap.yml`) via a service called "configserver" with credentials containing a 
URI.

If you want to use a persistent database, bind the app to a mysql service (the jar is included 
already). Remember to set `spring.jpa.hibernate.ddl-auto=create-drop` for the first startup, and 
unset it subsequently (you can POST to the `/env` and `/restart` endpoints as well to get that
without too much mess).