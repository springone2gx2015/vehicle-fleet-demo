package fleet.location.actuator.ui

import groovy.json.JsonSlurper
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.discovery.DiscoveryClient
import static org.springframework.http.HttpStatus.*

class ServiceController {

    static responseFormats = ['html','json', 'xml']

    DiscoveryClient discoveryClient

    def index() {
        respond services: discoveryClient.services.collect() {
            discoveryClient.getInstances(it)
        }.flatten().collect({ ServiceInstance si ->
            [
                    id: si.serviceId,
                    host: si.host,
                    port: si.port
            ]
        }
        )
    }

    def show(String id) {
        def instances = discoveryClient.getInstances(id)
        if(instances) {

            def si = instances[0]
            def slurper = new JsonSlurper()
            def env = ((Map)slurper.parse(new URL("${si.uri}/env")))
            respond(service: [
                    id: si.serviceId,
                    uri: "/${si.serviceId.toLowerCase()}/env",
                    host: si.host,
                    port: si.port,
                    env: env
            ])
        }
        else {
            render status: NOT_FOUND
        }
    }
}
