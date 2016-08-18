package org.cloudera.demo.object;

public class MessageElement {
	
	private String name;  
	private Double criticalThreshold;
	private Double lowestThreshold; 
	private Double minimum;
	private Double maximum;
	private Double unit;
	private Double volatility;
	
	
	
	
	public Double getVolatility() {
		return volatility;
	}
	public void setVolatility(Double volatility) {
		this.volatility = volatility;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getCriticalThreshold() {
		return criticalThreshold;
	}
	public void setCriticalThreshold(Double criticalThreshold) {
		this.criticalThreshold = criticalThreshold;
	}
	public Double getLowestThreshold() {
		return lowestThreshold;
	}
	public void setLowestThreshold(Double lowestThreshold) {
		this.lowestThreshold = lowestThreshold;
	}
	public Double getMinimum() {
		return minimum;
	}
	public void setMinimum(Double minimum) {
		this.minimum = minimum;
	}
	public Double getMaximum() {
		return maximum;
	}
	public void setMaximum(Double maximum) {
		this.maximum = maximum;
	}
	public Double getUnit() {
		return unit;
	}
	public void setUnit(Double unit) {
		this.unit = unit;
	}
	
	
		
}
