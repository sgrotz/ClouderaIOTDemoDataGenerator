package org.cloudera.demo.object;

import java.util.Date;

public class EventObject {
	
	String ID; 
	String maschine; 
	Date timestamp;
	String name;
	Object value;
	
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getMaschine() {
		return maschine;
	}
	public void setMaschine(String maschine) {
		this.maschine = maschine;
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
	
	public String toString() {
		return this.getID() + "," + this.getMaschine() + "," + this.getTimestamp() + "," + this.getName() + "," + this.getValue();
	}

}
