package model.application.operator;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

public class Operator implements Serializable {

	private static final long serialVersionUID = -6984676892694584174L;
	
	private OperatorRole role;
	private String name;
	private Function<Long, Long> transformation;

	public Operator(OperatorRole role, String name, Function<Long, Long> transformation) {
		this.setRole(role);
		this.setName(name);
		this.setTransformation(transformation);
	}
	
	public Operator(OperatorRole role, String name) {
		this.setRole(role);
		this.setName(name);
		this.setTransformation(x -> x);
	}

	public OperatorRole getRole() {
		return this.role;
	}
	
	private void setRole(OperatorRole role) {
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
	
	public long getFlowOut(long flowIn) {
		return this.getTransformation().apply(flowIn);
	}	
	
	@Override 
	public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass())
			return false;
		
		Operator other = (Operator) obj;
		
		return (this.getRole() == other.getRole() &&
				this.getName().equals(other.getName()) &&
				this.getTransformation().equals(other.getTransformation()));
	}
	
	@Override 
	public String toString() {
		return "Operator(" + 
			   "type:" + this.getRole() + ";" + 
			   "name:" + this.getName() + ";" + 
			   "transformation:" + this.getTransformation() + ")";
	}		
	
	@Override public int hashCode() {
		return Objects.hash(this.getRole(),
							this.getName(),
							this.getTransformation());
	}

}
