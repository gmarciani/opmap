package model.architecture.link;

import java.io.Serializable;

import model.architecture.node.ComputationalOffer;

public class LogicalLink implements Comparable<ComputationalOffer>, Serializable {

	private static final long serialVersionUID = 7389110561491378819L;
	
	private double delay;
	private double bandwidth;
	private double availability;

	public LogicalLink(double delay, double bandwidth, double availability) {
		this.setDelay(delay);
		this.setBandwidth(bandwidth);
		this.setAvailability(availability);
	}

	public double getDelay() {
		return this.delay;
	}

	public void setDelay(double delay) {
		this.delay = delay;
	}

	public double getBandwidth() {
		return this.bandwidth;
	}

	public void setBandwidth(double bandwidth) {
		this.bandwidth = bandwidth;
	}

	public double getAvailability() {
		return this.availability;
	}

	public void setAvailability(double availability) {
		if (availability < 0 || availability > 1)
			this.availability = 0.5;
		else
			this.availability = availability;
	}
	

	@Override public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override public int compareTo(ComputationalOffer other) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override public String toString() {
		return "LogicalLink(delay:" + this.getDelay() + ";" + 
						   "bandwidth:" + this.getBandwidth() + ";" + 
						   "availability:" + this.getAvailability() + ")";
	}

}
