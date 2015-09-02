# Config Server Sample

Run this project as a Spring Boot app, e.g. import into IDE and run
main method, or use Maven: 

```
$ mvn spring-boot:run
```

or

```
$ mvn package
$ java -jar target/*.jar
```

It will start up on port 8888 and serve configuration data from
"https://github.com/springone2gx2015/vehicle-fleet-demo" (in the
subdirectory `config-repo`):

## Resources

| Path             | Description  |
|------------------|--------------|
| /{app}/{profile} | Configuration data for app in Spring profile (comma-separated).|
| /{app}/{profile}/{label} | Add a git label |

