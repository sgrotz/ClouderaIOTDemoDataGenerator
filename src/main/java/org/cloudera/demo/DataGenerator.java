package org.cloudera.demo;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.cloudera.demo.object.EventObject;
import org.cloudera.demo.object.MessageElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class DataGenerator {

	Logger logger = LoggerFactory.getLogger(getClass());
	static long pause = 500;
	static String[] listOfMachines = {"M1", "M2", "M3"};
	static int maxCount = 100;
	static String kafkaServer="localhost:9092";
	static String topicName = "iotDemoEvents";
	static boolean sendAsJSON = false;
	static ArrayList<MessageElement> me;
	HashMap<String, Double> lastValues = new HashMap<String, Double>();
	
	
	public DataGenerator (String name, long pause, int maxCount, ArrayList<MessageElement> Messages) {
		System.out.println("Thread for " + name + " started");
		
        //Configure the Producer
        Properties configProperties = new Properties();
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaServer);
        configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");

        System.out.println("Trying to connect to Kafka Broker ...");
        Producer producer =  new KafkaProducer<String, String>(configProperties);
        
		
		for (int i = 0; i < maxCount; i++) {
			logger.debug("Sending message " + i);
			
			for (int k = 0; k< Messages.size(); k++) {
				EventObject e = new EventObject();
				MessageElement m = Messages.get(k);
				
				String ID = this.getCurrentDateAsUniqueID(name);
					e.setID(ID);
					e.setMachine(name);
					e.setTimestamp(this.getCurrentDate());
					e.setName(m.getName());
					
					if (lastValues.get(m.getName()) == null) {
						// If there is no last value, use the average
						Double value = (m.getMaximum() + m.getMinimum()) / 2;
						e.setValue(value);
						lastValues.put(m.getName(), value);
					} else {
						// 1 is up - 2 is down
						int u0d = this.getRandom(1,2);
						
						// Make sure to only go up or down by a certain volatility
						Double diff = m.getUnit() * this.getRandom(1, (int) m.getVolatility().doubleValue());
						Double lv = lastValues.get(m.getName());
						Double nv; 
						
						// 1 = up - 2 is down
						if (u0d == 1) {
							nv = lv + diff;
						} else {
							nv = lv - diff;
						}
						e.setValue(nv);
						
					}
					
					String content = e.toString();
				
					if (sendAsJSON) {
						ObjectMapper mapper = new ObjectMapper();
						
						try {
							content = mapper.writeValueAsString(e);
						} catch (JsonProcessingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

				
				logger.debug("Message Content String is: " + content);
				
				// Publish message to Kafka Broker
		        ProducerRecord<String, String> rec = new ProducerRecord<String, String>(topicName, content);
		        producer.send(rec);
			}
	        
	        logger.info("Successfully sent message " + i);
	        
	        try {
	        	logger.trace("Sleeping for " + pause + " msecs");
				Thread.sleep(pause);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

        producer.close();
		
		System.out.println("Thread " + name + " finished");
		
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			Options options = new Options();
			options.addOption("m", true, "List of machine names to generate events for (default is: M1, M2, M3");
			options.addOption("k", true, "Hostname or IP Address of the kafka server e.g 52.59.131.190:9092");
			options.addOption("p", true, "Pause factor (in msecs) - after each event, pause for x msecs (default is: 500)");
			options.addOption("x", true, "Set a max count of events to create (default: unlimited)");
			options.addOption("t", true, "Name of the topic to publish the messages to.");
			options.addOption("j", false, "If set, the content will be JSON encoded (default: false - csv separated)");
			options.addOption("h", false, "Print the command line help");
						
			CommandLineParser parser = new BasicParser();
			CommandLine cmd = parser.parse( options, args);
	
			if (cmd.hasOption("h")) {
				System.out.println("*** Missing arguments: ***");
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp( "runDataGenerator.sh", options );
				System.exit(0);
			}
				
			// Set the pause factor
			if (cmd.getOptionValue("p") != null) {
				pause = Long.valueOf(cmd.getOptionValue("p"));
			} 
			
			// Set the list of machines
			if (cmd.getOptionValue("m") != null)  {
				String machineList = cmd.getOptionValue("m");
				listOfMachines = machineList.split(",");
			} else {
				// If no machine names are provided - add 3 host specific machine names
				String hostname = InetAddress.getLocalHost().getHostName();
				String machineNames = hostname + "-M1," + hostname + "-M2, " + hostname + "-M3";
				listOfMachines = machineNames.split(",");
			}
			
			// Set the kafka server
			if (cmd.getOptionValue("k") != null)  {
				kafkaServer = cmd.getOptionValue("k");
			}
			
			// Specify the topic name
			if (cmd.getOptionValue("t") != null)  {
				topicName = cmd.getOptionValue("t");
			}
			
			if (cmd.hasOption("j"))  {
				sendAsJSON = true;
			}

			// Set the maxCount
			if (cmd.getOptionValue("x") != null) {
				maxCount = Integer.valueOf(cmd.getOptionValue("x"));
			} 
			
		
			for (int y =0; y < listOfMachines.length; y++ ) {
				System.out.println(listOfMachines[y].toString());	
			}
			
			CompositeConfiguration config = new CompositeConfiguration();
			
			//config.addConfiguration(new SystemConfiguration());
		    try {
				config.addConfiguration(new PropertiesConfiguration("events.properties"));
				ArrayList<String> eventNames = (ArrayList<String>) config.getProperty("event.names");
				
				me = new ArrayList<MessageElement>(eventNames.size());
				
				for (int p = 0; p < eventNames.size(); p++) {
					MessageElement m = new MessageElement();
						m.setMinimum(Double.valueOf((String) config.getProperty(eventNames.get(p) + ".min")));
						m.setMaximum(Double.valueOf((String) config.getProperty(eventNames.get(p) + ".max")));
						m.setCriticalThreshold(Double.valueOf((String) config.getProperty(eventNames.get(p) + ".critical")));
						m.setName(eventNames.get(p));
						m.setLowestThreshold(Double.valueOf((String) config.getProperty(eventNames.get(p) + ".lowest")));
						m.setUnit(Double.valueOf((String) config.getProperty(eventNames.get(p) + ".unit")));
						m.setVolatility(Double.valueOf((String) config.getProperty(eventNames.get(p) + ".volatility")));	
					me.add(m);
				}
				
			
				int threadCount = listOfMachines.length;
				
				System.out.println("********************************");
				System.out.println("* Running new DataGenerator ");
				System.out.println("* Starting a total of " + threadCount + " threads");
				System.out.println("* Setting pause factor to " + pause);
				System.out.println("* Setting maxCount (per machine) to " + (maxCount *2));
				System.out.println("* Pausing publishing for " + pause + " msecs, after each published event");
				System.out.println("* Publishing to Kafka Server " + kafkaServer + " on topic "+ topicName);
				System.out.println("********************************");
				
				Thread[] threads = new Thread[threadCount];
				for (int i = 0; i < threadCount; i++ ){
					final String machine = listOfMachines[i];
		
					threads[i] = new Thread() {
						public void run() {				
							new DataGenerator(machine, pause, maxCount, me);
						}
					};
					
					threads[i].setName("DataGenerator for " + machine);
					threads[i].start();
				}
				
				for (int i = 0; i < threads.length; i++) {
					threads[i].join();
				}
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
		} catch (Exception e) {
			
		}
		
		System.out.println("* Finished DataGenerator ");
		System.exit(0);

	}
	
	
	private String getUniqueID() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String randomID = String.valueOf(Math.random()*10);
		return randomID.replace(".", "").toString() + sdf.format(cal.getTime()); 
	}
	
	private String getUniqueIntID() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmssSSS");
		return sdf.format(cal.getTime());
	}
	
	private String getCurrentDateAsUniqueID(String name) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return name + sdf.format(cal.getTime()); 
	}

	private Date getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return cal.getTime(); 
	}
	
	private int getRandom(int start, int max) {
		Random generator = new Random(); 
		return generator.nextInt(max) + start;
	}
	
}
