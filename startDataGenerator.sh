#!/bin/bash

echo "*** Starting DataGenerator ***"
java -server -cp "target/*:target/libs/*:target/classes/*" -Dlog4j.configuration=target/classes/log4j.properties -Xms2g -Xmx2g org.cloudera.demo.DataGenerator $1 $2 $3 $4 $5 $6 $7 $8 $9