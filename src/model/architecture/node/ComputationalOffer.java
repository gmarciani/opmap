package model.architecture.node;

import java.io.Serializable;

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
		// TODO Auto-generated method stub
		return "";
	}

}
