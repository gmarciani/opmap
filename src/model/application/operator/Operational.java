package model.application.operator;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import model.application.operator.Role;

public class Operational implements Comparable<Operational>, Serializable {

	private static final long serialVersionUID = -8266724889784176862L;
	
	private long id;
	private Role role;
	private String name;
	private Function<Long, Long> transformation;
	private int resources;	
	private double speed;	
	private long flowIn;
	private long flowOut;
	private Set<Long> pinnables;			

	public Operational(long id, Role role, String name, Function<Long, Long> transformation, 
						   int resources, double speed, Set<Long> pinnables) {
		this.setId(id);
		this.setRole(role);
		this.setName(name);
		this.setTransformation(transformation);		
		this.setResources(resources);
		this.setSpeed(speed);
		this.setPinnables(pinnables);
		
		if (this.isSource())
			this.setFlowOut(this.getTransformation().apply(new Long(0)));
		else if (this.isSink())
			this.setFlowOut(0);
	}
	
	public long getId() {
		return this.id;
	}
	
	private void setId(long id) {
		this.id = id;
	}

	public Role getRole() {
		return this.role;
	}
	
	private void setRole(Role role) {
		this.role = role;
	}

	public String getName() {
		return this.name;
	}
	
	private void setName(String name) {
		this.name = name;
	}
	
	public Function<Long, Long> getTransformation() {
		return this.transformation;
	}
	
	private void setTransformation(Function<Long, Long> transformation) {
		this.transformation = transformation;
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
		this.flowOut = this.transformation.apply(this.flowIn);
	}

	public long getFlowOut() {
		return this.flowOut;
	}

	public void setFlowOut(long flowOut) {
		this.flowOut = flowOut;
	}
	
	public Set<Long> getPinnables() {
		return this.pinnables;
	}

	private void setPinnables(Set<Long> pinnables) {
		if (this.pinnables == null)
			this.pinnables = new LinkedHashSet<Long>();
		else if (!this.pinnables.isEmpty())
			this.pinnables.clear();
		this.pinnables.addAll(pinnables);
	}
	
	public void addPinnables(Set<Long> pinnables) {
		this.pinnables.addAll(pinnables);
	}
	
	public void addPinnable(Long nodeId) {
		this.getPinnables().add(nodeId);
	}
	
	public boolean isSource() {
		return this.getRole().equals(Role.SRC);
	}

	public boolean isSink() {
		return this.getRole().equals(Role.SNK);
	}	
	
	public boolean isPipe() {
		return this.getRole().equals(Role.PIP);
	}	
	
	public void addFlowIn(long flow) {
		this.setFlowIn(this.getFlowIn() + flow);
	}
	
	@Override 
	public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass())
			return false;
		
		Operational other = (Operational) obj;
		
		return (this.getId() == other.getId());
	}
	
	@Override 
	public int compareTo(Operational other) {
		return Double.valueOf(this.getSpeed()).compareTo(other.getSpeed());
	}	
	
	@Override 
	public String toString() {
		return "OperationalNode(" +
			   "id:" + this.getId() + ";" + 
			   "operator:" + this.getName() + "; " +
			   "role:" + this.getRole() + "; " +
			   "resources:" + this.getResources() + ";" + 
			   "speed:" + this.getSpeed() + ";" + 
			   "flowIn:" + this.getFlowIn() + ";" + 
			   "flowOut:" + this.getFlowOut() + ")";
	}
	
	@Override 
	public int hashCode() {
		return Objects.hash(this.getId());
	}	

}