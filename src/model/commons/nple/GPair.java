package model.commons.nple;

import java.io.Serializable;
import java.util.Objects;

public class GPair<X extends Comparable<X>, Y extends Comparable<Y>> 
	implements Comparable<GPair<X, Y>>, Serializable  {
	
	private static final long serialVersionUID = 5700402704202573780L;
	
	private X x;
	private Y y;

	public GPair(X x, Y y) {
		this.x = x;
		this.y = y;
	}
	
	public X getX() {
		return this.x;
	}
	
	public void setX(X x) {
		this.x = x;
	}
	
	public Y getY() {
		return this.y;
	}
	
	public void setY(Y y) {
		this.y = y;
	}
	
	@Override public String toString() {
		return "(" + String.valueOf(this.getX()) + "," + String.valueOf(this.getY()) + ")";
	}

	@Override 
	public int compareTo(GPair<X, Y> other) {
		int byX = this.getX().compareTo(other.getX());
		int byY = this.getY().compareTo(other.getY());		
		if (byX == 0)
			return byY;		
		return byX;
	}
	
	@Override public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass())
			return false;
		
		GPair<?, ?> other = (GPair<?, ?>) obj;
		
		return (this.getX().equals(other.getX())
				&& this.getY().equals(other.getY()));
	}
	
	@Override public int hashCode() {
		return Objects.hash(this.getX(), this.getY());
	}

}
