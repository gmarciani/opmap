package model.architecture.node;

import java.io.Serializable;
import java.util.Objects;

public class ComputationalOffer implements Comparable<ComputationalOffer>, Serializable {

	private static final long serialVersionUID = -9084172644716113779L;
	
	private int resources;
	private double speedup;
	private double availability;

	public ComputationalOffer(int resources, double speedup, double availability) {
		this.setResources(resources);
		this.setSpeedup(speedup);
		this.setAvailability(availability);
	}
	
	public ComputationalOffer() {
		this.setResources(0);
		this.setSpeedup(0.0);
		this.setAvailability(0.0);
	}

	public int getResources() {
		return this.resources;
	}

	public void setResources(int resources) {
		this.resources = resources;
	}

	public double getSpeedup() {
		return this.speedup;
	}

	public void setSpeedup(double speedup) {
		this.speedup = speedup;
	}

	public double getAvailability() {
		return this.availability;
	}

	public void setAvailability(double availability) {
		this.availability = availability;
	}

	@Override 
	public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass())
			return false;
		
		ComputationalOffer other = (ComputationalOffer) obj;
		
		return (this.getResources() == other.getResources() &&
				this.getSpeedup() == other.getSpeedup() &&
				this.getAvailability() == other.getAvailability());
	}

	@Override 
	public int compareTo(ComputationalOffer other) {
		return Double.valueOf(this.getSpeedup() * this.getAvailability() * this.getResources()).compareTo(
				Double.valueOf(other.getSpeedup() * other.getAvailability() * other.getResources()));
	}	
	
	@Override 
	public String toString() {
		return "ComputationalOffer(" + 
			   "resources:" + this.getResources() + ";" + 
			   "speedup:" + this.getSpeedup() + ";" + 
			   "availability:" + this.getAvailability() + ")";
	}
	
	@Override 
	public int hashCode() {
		return Objects.hash(this.getResources(), 
							this.getSpeedup(), 
							this.getAvailability());
	}

}
