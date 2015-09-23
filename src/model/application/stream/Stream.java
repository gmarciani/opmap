package model.application.stream;

import java.io.Serializable;
import java.util.Objects;

import model.commons.nple.GPair;

public class Stream implements Comparable<Stream>, Serializable {
	
	private static final long serialVersionUID = -5368252928484810293L;
	
	private GPair<Long, Long> edge;
	private long flow;	

	public Stream(long src, long dst, long flow) {
		this.setSrc(src);
		this.setDst(dst);
		this.setFlow(flow);
	}
	
	public long getSrc() {
		return this.edge.getX();
	}

	public void setSrc(long src) {
		this.edge.setX(src);;
	}

	public long getDst() {
		return this.edge.getY();
	}

	public void setDst(long dst) {
		this.edge.setY(dst);
	}

	public long getFlow() {
		return this.flow;
	}

	public void setFlow(long flow) {
		this.flow = flow;
	}	
	
	@Override public String toString() {
		return "DataStream(" + 
			   "src:" + this.getSrc() + ";" + 
			   "dst:" + this.getDst() + ";" + 
			   "flow:" + this.getFlow() + ")";
	}
	
	@Override public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass())
			return false;
		
		Stream other = (Stream) obj;
		
		return (this.getSrc() == other.getSrc() &&
				this.getDst() == other.getDst() &&
				this.getFlow() == other.getFlow());		
	}
	
	@Override public int compareTo(Stream other) {
		return Long.valueOf(this.getFlow()).compareTo(Long.valueOf(other.getFlow()));
	}
	
	@Override public int hashCode() {
		return Objects.hash(this.getSrc(),
							this.getDst());
	}

	
}
