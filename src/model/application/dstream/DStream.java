package model.application.dstream;

import java.io.Serializable;
import java.util.Objects;

import model.application.operator.OPNode;
import model.architecture.link.Link;

public class DStream implements Comparable<DStream>, Serializable {
	
	private static final long serialVersionUID = -5368252928484810293L;
	
	private OPNode src;
	private OPNode dst;
	private long flow;	

	public DStream(OPNode src, OPNode dst, long flow) {
		this.setSrc(src);
		this.setDst(dst);
		this.setFlow(flow);
		this.getDst().addFlowIn(this.getFlow());
	}
	
	public DStream(OPNode src, OPNode dst) {
		this(src, dst, src.getFlowOut());
	}
	
	public OPNode getSrc() {
		return this.src;
	}
	
	private void setSrc(OPNode src) {
		this.src = src;
	}
	
	public OPNode getDst() {
		return this.dst;
	}
	
	private void setDst(OPNode dst) {
		this.dst = dst;
	}

	public long getFlow() {
		return this.flow;
	}

	public void setFlow(long flow) {
		this.flow = flow;
	}	
	
	public boolean isPinnable(Link link) {
		return this.getSrc().isPinnable(link.getSrc()) &&
				this.getDst().isPinnable(link.getDst());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass())
			return false;
		
		DStream other = (DStream) obj;
		
		return this.getSrc().equals(other.getSrc()) &&
				this.getDst().equals(other.getDst());
	}
	
	@Override 
	public int compareTo(DStream other) {
		return Long.valueOf(this.getFlow()).compareTo(Long.valueOf(other.getFlow()));
	}
	
	public String toPrettyString() {
		String str = "#stream# ";
		
		str += "src:" + this.getSrc().getId() + "|";
		str += "dst:" + this.getDst().getId() + "|";
		str += "flow:" + this.getFlow();
		
		return str;
	}
	
	@Override 
	public String toString() {
		return "DStream(" + 
			   "src:" + this.getSrc().getId() + ";" +
			   "dst:" + this.getDst().getId() + ";" +
			   "flow:" + this.getFlow() + ")";
	}
	
	@Override 
	public int hashCode() {
		return Objects.hash(this.getSrc().getId(), this.getDst().getId());
	}
	
}
