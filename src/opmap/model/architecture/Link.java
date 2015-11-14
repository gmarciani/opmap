package opmap.model.architecture;

import java.io.Serializable;
import java.util.Objects;


public class Link implements Comparable<Link>, Serializable {

	private static final long serialVersionUID = 7389110561491378819L;
	
	private EXNode src;
	private EXNode dst;
	private double delay;
	private double bandwidth;
	private double availability;
	
	public Link(final EXNode src, final EXNode dst, final double delay, final double bandwidth, final double availability) {
		this.setSrc(src);
		this.setDst(dst);
		this.setDelay(delay);
		this.setBandwidth(bandwidth);
		this.setAvailability(availability);
	}
	
	public Link(final EXNode src, final EXNode dst) {
		this(src, dst, 0, 0, 0.0);
	}

	public EXNode getSrc() {
		return this.src;
	}

	private void setSrc(final EXNode src) {
		this.src = src;
	}

	public EXNode getDst() {
		return this.dst;
	}

	private void setDst(final EXNode dst) {
		this.dst = dst;
	}	
	
	public double getDelay() {
		return this.delay;
	}

	public void setDelay(final double delay) {
		this.delay = delay;
	}

	public double getBandwidth() {
		return this.bandwidth;
	}

	public void setBandwidth(final double bandwidth) {
		this.bandwidth = bandwidth;
	}

	public double getAvailability() {
		return this.availability;
	}

	public void setAvailability(final double availability) {
		if (availability < 0 || availability > 1)
			this.availability = 0.5;
		else
			this.availability = availability;
	}	
	
	@Override
	public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass())
			return false;
		
		Link other = (Link) obj;
		
		return this.getSrc().equals(other.getSrc()) &&
				this.getDst().equals(other.getDst());
	}
	
	@Override 
	public int compareTo(Link other) {
		return Integer.valueOf(this.getSrc().getId()).compareTo(other.getSrc().getId());
	}
	
	public String toPrettyString() {
		String str = String.format("#link# src:%d|dst:%d|delay:%.5f|bandwidth:%.5f|availability:%.5f",
				this.getSrc().getId(),
				this.getDst().getId(),
				this.getDelay(),
				this.getBandwidth(),
				this.getAvailability());
		
		return str;
	}
	
	@Override 
	public String toString() {
		String str = String.format("Link(src:%d|dst:%d|delay:%.5f|bandwidth:%.5f|availability:%.5f)",
				this.getSrc().getId(),
				this.getDst().getId(),
				this.getDelay(),
				this.getBandwidth(),
				this.getAvailability());
		
		return str;
	}
	
	@Override 
	public int hashCode() {
		return Objects.hash(this.getSrc().getId(), this.getDst().getId());
	}

}
