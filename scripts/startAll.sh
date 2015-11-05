#!/bin/bash
SCRIPT_DIR=`dirname $0`
PROJECT_HOME=$SCRIPT_DIR/..
LOGS_DIR=$PROJECT_HOME/logs

MONGODB_PORT=27017
MONGODB_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_HOST=localhost

function check_mongo_running {
	local response=`curl -is http://$MONGODB_HOST:$MONGODB_PORT`
	 if ! [[ $response == *"200 OK"* ]]; then
 		echo "mongodb is not running on $MONGODB_HOST:$MONGODB_PORT"
 		exit 1
	 fi
}

function check_rabbitmq_running {
	local response=`curl -is http://$RABBITMQ_HOST:$RABBITMQ_PORT`
	 if ! [[ $response == *"AMQP"* ]]; then
 		echo "rabbitmq is not running on $RABBITMQ_HOST:$RABBITMQ_PORT"
 		exit 1
	 fi
}


function health_check {
  local url="http://localhost:$2"
  local health=`curl -is $url`
  local retry_count=0
  while ! [[ $health == *"200 OK"* ]]; do
	echo  -ne "waiting for $1 to start on $url ... [$retry_count]\r"
    sleep 1
    health=`curl -is $url`
    retry_count=$[retry_count + 1]
    if [ "$retry_count" -gt  "240" ]; then
  	  echo "timed out waiting for service $1 to start @ $url"
  	  exit 1
    fi
  done
  echo "$1 started on $url"
}


check_mongo_running
check_rabbitmq_running

mkdir -p $LOGS_DIR

. $SCRIPT_DIR/parseYml.sh

eval $(parse_yaml $SCRIPT_DIR/services.yml)

for service in $services
do
	port="${service}_port"
	name="${service}_name"
	jar="${!name}-${version}.jar" 
	target=`find .. -name $jar`
	if [[ ! -z $target ]]; then
		pid=`jps | grep $jar | awk '{print $1}'`	 
		if [[  -z  $pid ]]; then
			
			logfile=${jar%.jar}
			echo "starting $target, output -> $LOGS_DIR/$logfile.log"
			java -jar $target>$LOGS_DIR/$logfile.log 2>&1 &
			health_check ${!name} ${!port}
		else
			echo "$jar is already running"
		fi
		
	else
		echo "$jar not found. Did you run ./mvnw clean install ?"
		exit 1
	fi
done

echo "Loading data..."
# Load data
if ! [ -e "fleet.json" ]; then 
	wget http://assets.springone2gx2015.s3.amazonaws.com/fleet/fleet.json
fi
curl -s -X POST http://localhost:$fleet_location_service_port/purge
curl -sH "Content-Type: application/json" http://localhost:$fleet_location_service_port/fleet -d @fleet.json

if ! [ -e "serviceLocations.json" ]; then 
	wget http://assets.springone2gx2015.s3.amazonaws.com/fleet/serviceLocations.json
fi
curl -s -X POST http://localhost:$service_location_service_port/purge
curl -sH "Content-Type: application/json" http://localhost:$service_location_service_port/bulk/serviceLocations -d @serviceLocations.json

echo "Starting simulator..."
# Start the simulator
curl -s http://localhost:$fleet_location_simulator_port/api/cancel > /dev/null
curl -s http://localhost:$fleet_location_simulator_port/api/dc > /dev/null
echo "**** Vehicle Fleet Demo is running on http://localhost:$dashboard_port"


