#!/bin/bash
SCRIPT_DIR=`dirname $0`

. $SCRIPT_DIR/parseYml.sh

eval $(parse_yaml $SCRIPT_DIR/services.yml)

for service in $services
do
	name="${service}_name"
	target=${!name}
	pid=`jps | grep $target | awk '{print $1}'`	 
	if [[ !  -z  $pid ]]; then
		echo "stopping $service"
		kill $pid
	fi
done

