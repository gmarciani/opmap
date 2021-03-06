package opmap.model.architecture;

import java.io.Serializable;
import java.util.Objects;

public class EXNode implements Comparable<EXNode>, Serializable {
	
	private static final long serialVersionUID = 4664378299375410058L;
	
	private int id;
	private String name;
	private int resources;
	private double speedup;
	private double availability;

	public EXNode(final int id, final String name, final int resources, final double speedup, final double availability) {
		this.setId(id);
		this.setName(name);
		this.setResources(resources);
		this.setSpeedup(speedup);
		this.setAvailability(availability);
	}
	
	public int getId() {
		return this.id;
	}

	private void setId(final int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getResources() {
		return this.resources;
	}

	public void setResources(final int resources) {
		this.resources = resources;
	}

	public double getSpeedup() {
		return this.speedup;
	}

	public void setSpeedup(final double speedup) {
		this.speedup = speedup;
	}

	public double getAvailability() {
		return this.availability;
	}

	public void setAvailability(final double availability) {
		this.availability = availability;
	}	
	
	@Override 
	public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass())
			return false;
		
		EXNode other = (EXNode) obj;
		
		return (this.getId() == other.getId());
	}

	@Override 
	public int compareTo(EXNode other) {
		return Integer.valueOf(this.getId()).compareTo(other.getId());
	}	
	
	public String toPrettyString() {
		String str = String.format("#exnode# id:%d|name:%s|resources:%d|speedup:%.5f|availability:%.5f",
				this.getId(),
				this.getName(),
				this.getResources(),
				this.getSpeedup(),
				this.getAvailability());
		
		return str;	
	}
	
	@Override 
	public String toString() {
		String str = String.format("EXNode(id:%d|name:%s|resources:%d|speedup:%f|availability:%f)",
				this.getId(),
				this.getName(),
				this.getResources(),
				this.getSpeedup(),
				this.getAvailability());
		
		return str;
	}
	
	@Override 
	public int hashCode() {
		return Objects.hash(this.getId());
	}

}
