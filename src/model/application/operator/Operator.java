package model.application.operator;

import java.io.Serializable;
import java.util.Objects;

public class Operator implements Comparable<Operator>, Serializable {

	private static final long serialVersionUID = -6984676892694584174L;
	
	private OperatorType type;
	private String name;
	private Transformation transformation;

	public Operator(OperatorType type, String name, Transformation transformation) {
		this.type = type;
		this.name = name;
		this.transformation = transformation;
	}

	public OperatorType getType() {
		return this.type;
	}

	public String getName() {
		return this.name;
	}
	
	public Transformation getTransformation() {
		return this.transformation;
	}
	
	public long getFlowOut(long flowIn) {
		return this.getTransformation().apply(flowIn);
	}
	
	@Override public String toString() {
		return "Operator(" + 
			   "type:" + this.getType() + ";" + 
			   "name:" + this.getName() + ";" + 
			   "flowTransformation:" + this.getTransformation() + ")";
	}	
	
	@Override public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass())
			return false;
		
		Operator other = (Operator) obj;
		
		return (this.getType() == other.getType() &&
				this.getName().equals(other.getName()) &&
				this.getTransformation().equals(other.getTransformation()));
	}

	@Override public int compareTo(Operator other) {
		return this.getTransformation().compareTo(other.getTransformation());
	}	
	
	@Override public int hashCode() {
		return Objects.hash(this.getType(),
							this.getName(),
							this.getTransformation());
	}

}
