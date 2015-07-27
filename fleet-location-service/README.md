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
