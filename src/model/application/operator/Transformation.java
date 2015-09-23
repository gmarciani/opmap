package model.application.operator;

import java.io.Serializable;
import java.util.Objects;

public class Transformation implements Comparable<Transformation>, Serializable {
	
	private static final long serialVersionUID = 3001077141926343107L;
	
	private TransformationType type;
	private double value;

	public Transformation(TransformationType type, double value) {
		this.type = type;
		this.value = value;
	}

	public TransformationType getType() {
		return this.type;
	}

	public double getValue() {
		return this.value;
	}
	
	public long apply(long x) {
		switch (this.getType()) {
		case EQUAL:
			return Math.round(this.getValue());
		case RATIO:
			return Math.round(x * this.getValue());
		default:
			return x;
		}
	}
	
	@Override public String toString() {
		return "Transformation(" + 
			   "type:" + this.getType() + ";" + 
			   "value:" + this.getValue() + ")";
	}
	
	@Override public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass())
			return false;
		
		Transformation other = (Transformation) obj;
		
		return (this.getType() == other.getType() &&
				this.getValue() == other.getValue());
	}
	
	@Override public int compareTo(Transformation other) {
		int byType = this.getType().compareTo(other.getType());
		int byValue = Double.valueOf(this.getValue()).compareTo(Double.valueOf(other.getValue()));
		
		if (byType == 0)
			return byValue;
		else
			return -2;
	}
	
	@Override public int hashCode() {
		return Objects.hash(this.getType(),
				            this.getValue());
	}

}
