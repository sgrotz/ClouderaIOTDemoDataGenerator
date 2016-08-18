# ClouderaIOTDemoDataGenerator
IOT DEMO Data Generator

This is a simple Data Generator for sending machine data to a kafka topic. 

To use it, download it and build it using Maven. Make sure that you update the Kafka version number in the pom.xml file. I am using 0.9.0.0 at the time of writing. 
run: mvn clean package

followed by: 
./startDataGenerator.sh -m M1,M2,M3,M4 -p 800 -j

The command line arguments are: 
* m indicates the machines to simulate (e.g M1,M2,M3 would simulate 3 different machines). 
* p adds a pause after each event message
* j will force the messages to be in JSON (otherwise it is comma separated)
* c will set a maxCount of how many messages to send
* k = Hostname or IP Address of the kafka server e.g 52.59.131.190:9092
* t = topic name
