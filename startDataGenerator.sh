#!/bin/bash

echo "*** Starting DataGenerator ***"
java -server -cp "target/*:target/libs/*:target/classes/*" -Xms1g -Xmx1g org.cloudera.demo.DataGenerator $1 $2 $3 $4 $5 $6 $7 $8 $9
