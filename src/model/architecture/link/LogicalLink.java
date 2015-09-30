package model.architecture.link;

import java.io.Serializable;
import java.util.Objects;

import model.architecture.node.Computational;


public class LogicalLink implements Comparable<LogicalLink>, Serializable {

	private static final long serialVersionUID = 7389110561491378819L;
	
	private Computational src;
	private Computational dst;
	private double delay;
	private double bandwidth;
	private double availability;
	
	public LogicalLink(Computational src, Computational dst, double delay, double bandwidth, double availability) {
		this.setSrc(src);
		this.setDst(dst);
		this.setDelay(delay);
		this.setBandwidth(bandwidth);
		this.setAvailability(availability);
	}
	
	public LogicalLink(Computational src, Computational dst) {
		this(src, dst, 0.0, 0.0, 0.0);
	}

	public Computational getSrc() {
		return this.src;
	}

	private void setSrc(Computational src) {
		this.src = src;
	}

	public Computational getDst() {
		return this.dst;
	}

	private void setDst(Computational dst) {
		this.dst = dst;
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
	
	@Override
	public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass())
			return false;
		
		LogicalLink other = (LogicalLink) obj;
		
		return this.getSrc().equals(other.getSrc()) &&
				this.getDst().equals(other.getDst());
	}
	
	@Override 
	public int compareTo(LogicalLink other) {
		return Double.valueOf(this.getBandwidth() * this.getAvailability() / this.getDelay()).compareTo(
				Double.valueOf(other.getBandwidth() * other.getAvailability() / other.getDelay()));
	}
	
	@Override 
	public String toString() {
		return "LogicalLink(" + 
			   "src:" + this.getSrc().getId() + ";" +
			   "dst:" + this.getDst().getId() + ";" +
			   "delay:" + this.getDelay() + ";" + 
			   "bandwidth:" + this.getBandwidth() + ";" + 
			   "availability:" + this.getAvailability() + ")";
	}
	
	@Override 
	public int hashCode() {
		return Objects.hash(this.getSrc().getId(),
				this.getDst().getId(),
				this.getDelay(), 
				this.getBandwidth(), 
				this.getAvailability());
	}

}
