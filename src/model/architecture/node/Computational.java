package model.architecture.node;

import java.io.Serializable;
import java.util.Objects;

public class Computational implements Comparable<Computational>, Serializable {
	
	private static final long serialVersionUID = 4664378299375410058L;
	
	private int id;
	private String name;
	private int resources;
	private double speedup;
	private double availability;

	public Computational(int id, String name, int resources, double speedup, double availability) {
		this.setId(id);
		this.setName(name);
		this.setResources(resources);
		this.setSpeedup(speedup);
		this.setAvailability(availability);
	}
	
	public int getId() {
		return this.id;
	}

	private void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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
		
		Computational other = (Computational) obj;
		
		return (this.getId() == other.getId());
	}

	@Override 
	public int compareTo(Computational other) {
		return Double.valueOf(this.getResources() * this.getSpeedup() * this.getAvailability()).compareTo(
				Double.valueOf(other.getResources() * other.getSpeedup() * other.getAvailability()));
	}	
	
	@Override public String toString() {
		return "ComputationalNode(" +
				"id:" + this.getId() + ";" + 
				"name:" + this.getName() + ";" + 
				"resources:" + this.getResources() + ";" + 
				"speedup:" + this.getSpeedup() + ";" + 
				"availability:" + this.getAvailability() + ")";
	}
	
	@Override 
	public int hashCode() {
		return Objects.hash(this.getId());
	}

}
