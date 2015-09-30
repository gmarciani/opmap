package model.application.dstream;

import java.io.Serializable;
import java.util.Objects;

import model.application.operator.Operational;
import model.architecture.link.LogicalLink;

public class DataStream implements Comparable<DataStream>, Serializable {
	
	private static final long serialVersionUID = -5368252928484810293L;
	
	private Operational src;
	private Operational dst;
	private long flow;	

	public DataStream(Operational src, Operational dst, long flow) {
		this.setSrc(src);
		this.setDst(dst);
		this.setFlow(flow);
		this.getDst().addFlowIn(this.getFlow());
	}
	
	public DataStream(Operational src, Operational dst) {
		this(src, dst, src.getFlowOut());
	}
	
	public Operational getSrc() {
		return this.src;
	}
	
	private void setSrc(Operational src) {
		this.src = src;
	}
	
	public Operational getDst() {
		return this.dst;
	}
	
	private void setDst(Operational dst) {
		this.dst = dst;
	}

	public long getFlow() {
		return this.flow;
	}

	public void setFlow(long flow) {
		this.flow = flow;
	}	
	
	public boolean isPinnable(LogicalLink link) {
		return this.getSrc().isPinnable(link.getSrc()) &&
				this.getDst().isPinnable(link.getDst());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass())
			return false;
		
		DataStream other = (DataStream) obj;
		
		return this.getSrc().equals(other.getSrc()) &&
				this.getDst().equals(other.getDst());
	}
	
	@Override 
	public int compareTo(DataStream other) {
		return Long.valueOf(this.getFlow()).compareTo(Long.valueOf(other.getFlow()));
	}
	
	@Override 
	public String toString() {
		return "DataStream(" + 
			   "src:" + this.getSrc().getId() + ";" +
			   "dst:" + this.getDst().getId() + ";" +
			   "flow:" + this.getFlow() + ")";
	}
	
	@Override 
	public int hashCode() {
		return Objects.hash(this.getFlow());
	}
	
}
