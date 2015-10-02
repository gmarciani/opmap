package model.commons;

import java.io.Serializable;
import java.util.Objects;

public class Pair<X1 extends Comparable<X1>, X2 extends Comparable<X2>> implements Serializable {

	private static final long serialVersionUID = 4803770078086427961L;

	private final X1 x1;
	private final X2 x2;

	public Pair(X1 x1, X2 x2) {
		this.x1 = x1;
		this.x2 = x2;
	}
	
	public X1 getX1() {
		return this.x1;
	}
	
	public X2 getX2() {
		return this.x2;
	}
	
	public String toPrettyString() {
		return "(" + this.getX1() + "," + this.getX2() + ")";
	}
	
	@Override 
	public String toString() {
		return "Pair(" + 
				"x1:" + this.getX1() + ";" + 
				"x2:" + this.getX2() + ")";
	}
	
	@Override 
	public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass())
			return false;
		
		Pair<?, ?> other = (Pair<?, ?>) obj;
		
		return this.getX1().equals(other.getX1()) &&
			   this.getX2().equals(other.getX2());
	}
	
	@Override 
	public int hashCode() {
		return Objects.hash(this.getX1(), this.getX2());
	}

}
