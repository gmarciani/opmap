package model.application.operator;

import java.util.ArrayList;
import java.util.Objects;

public class OperationalPath extends ArrayList<Operational> implements Comparable<OperationalPath> {

	private static final long serialVersionUID = 3178541927310072502L;
	
	private double response;

	public OperationalPath() {
		super();
		this.setResponse(0.0);
	}

	public double getResponse() {
		return this.response;
	}

	public void setResponse(double response) {
		this.response = response;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass())
			return false;
		
		OperationalPath other = (OperationalPath) obj;
		
		return (this.getResponse() == other.getResponse() &&
				super.toArray().equals(other.toArray()));
	}
	
	@Override
	public int compareTo(OperationalPath other) {
		return Double.valueOf(this.getResponse()).compareTo(
				Double.valueOf(other.getResponse()));
	}
	
	@Override
	public String toString() {
		return "OperationalPath(" + 
				"response:" + this.getResponse() + ";" + 
				"nodes:" + super.toString() + ")";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getResponse(), 
							super.toArray());
	}

}
