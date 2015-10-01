package model.commons;

import java.io.Serializable;
import java.util.Objects;

public class Quadruple<X1 extends Comparable<X1>, X2 extends Comparable<X2>, X3 extends Comparable<X3>, X4 extends Comparable<X4>> 
	implements Serializable {

	private static final long serialVersionUID = -6828072546564797505L;

	private final X1 x1;
	private final X2 x2;
	private final X3 x3;
	private final X4 x4;

	public Quadruple(X1 x1, X2 x2, X3 x3, X4 x4) {
		this.x1 = x1;
		this.x2 = x2;
		this.x3 = x3;
		this.x4 = x4;
	}
	
	public X1 getX1() {
		return this.x1;
	}
	
	public X2 getX2() {
		return this.x2;
	}
	
	public X3 getX3() {
		return this.x3;
	}

	public X4 getX4() {
		return this.x4;
	}

	@Override 
	public String toString() {
		return "(" + this.getX1() + "," + this.getX2() + "," + this.getX3() + "," + this.getX4() + ")";
	}
	
	@Override 
	public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass())
			return false;
		
		Quadruple<?, ?, ?, ?> other = (Quadruple<?, ?, ?, ?>) obj;
		
		return this.getX1().equals(other.getX1()) &&
			   this.getX2().equals(other.getX2()) &&
			   this.getX3().equals(other.getX3()) &&
			   this.getX4().equals(other.getX4());
	}
	
	@Override 
	public int hashCode() {
		return Objects.hash(this.getX1(), this.getX2(), this.getX3(), this.getX4());
	}

}
