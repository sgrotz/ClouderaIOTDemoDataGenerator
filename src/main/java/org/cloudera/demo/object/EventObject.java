package org.cloudera.demo.object;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EventObject {
	
	String ID; 
	String machine;
	String host;
	Date timestamp;
	String name;
	Object value;
	
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getMachine() {
		return machine;
	}
	public void setMachine(String machine) {
		this.machine = machine;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	} 
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String toString() {
		return this.getID() + "," + this.getMachine() + ","+ this.getHost() + "," + this.getCurrentDateAsUniqueID() + "," + this.getName() + "," + this.getValue();
	}
	
	private String getCurrentDateAsUniqueID() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return  sdf.format(cal.getTime()); 
	}

}
