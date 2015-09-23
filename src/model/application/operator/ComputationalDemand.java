package model.application.operator;

import java.io.Serializable;
import java.util.Objects;

public class ComputationalDemand implements Comparable<ComputationalDemand>, Serializable {
	
	private static final long serialVersionUID = -3108873032557661439L;
	
	private int resources;	
	private double speed;	
	private long flowIn;
	private long flowOut;

	public ComputationalDemand(int resources, double speed, long flowIn, long flowOut) {
		this.setResources(resources);
		this.setSpeed(speed);
		this.setFlowIn(flowIn);
		this.setFlowOut(flowOut);
	}
	
	public ComputationalDemand(int resources, double speed) {
		this.setResources(resources);
		this.setSpeed(speed);
		this.setFlowIn(0);
		this.setFlowOut(0);
	}

	public int getResources() {
		return this.resources;
	}

	public void setResources(int resources) {
		this.resources = resources;
	}

	public double getSpeed() {
		return this.speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public long getFlowIn() {
		return this.flowIn;
	}

	public void setFlowIn(long flowIn) {
		this.flowIn = flowIn;
	}

	public long getFlowOut() {
		return this.flowOut;
	}

	public void setFlowOut(long flowOut) {
		this.flowOut = flowOut;
	}
	
	@Override public String toString() {
		return "ComputationalDemand(" + 
			   "resources:" + this.getResources() + ";" + 
			   "speed:" + this.getSpeed() + ";" + 
			   "flowIn:" + this.getFlowIn() + ";" + 
			   "flowOut:" + this.getFlowOut() + ")";
	}	
	
	@Override public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass())
			return false;
		
		ComputationalDemand other = (ComputationalDemand) obj;
		
		return (this.getResources() == other.getResources() &&
				this.getSpeed() == other.getSpeed() &&
				this.getFlowIn() == other.getFlowIn() &&
				this.getFlowOut() == other.getFlowOut());
	}
	
	@Override public int compareTo(ComputationalDemand other) {
		if (this.getResources() > other.getResources())
			return 1;
		else if (this.getResources() < other.getResources()) 
			return -1;
		return 0;
	}
	
	@Override public int hashCode() {
		return Objects.hash(this.getResources(),
				            this.getSpeed(),
				            this.getFlowIn(),
				            this.getFlowOut());
	}

}
