package model.application.dstream;

import java.util.Objects;

import org.jgrapht.graph.DefaultWeightedEdge;

import model.application.opnode.OPNode;
import model.architecture.link.Link;

public class DStream extends DefaultWeightedEdge implements Comparable<DStream> {
	
	private static final long serialVersionUID = -1128317756010407723L;
	
	private OPNode src;
	private OPNode dst;
	private double flow;	

	public DStream(final OPNode src, final OPNode dst, final double flow) {
		this.setSrc(src);
		this.setDst(dst);
		this.setFlow(flow);
		this.getDst().addFlowIn(this.getFlow());
	}
	
	public DStream(final OPNode src, final OPNode dst) {
		this(src, dst, src.getFlowOut());
	}
	
	public OPNode getSrc() {
		return this.src;
	}
	
	private void setSrc(final OPNode src) {
		this.src = src;
	}
	
	public OPNode getDst() {
		return this.dst;
	}
	
	private void setDst(final OPNode dst) {
		this.dst = dst;
	}

	public double getFlow() {
		return this.flow;
	}

	public void setFlow(final double flow) {
		this.flow = flow;
	}	
	
	public boolean isPinnableOn(Link link) {
		return this.getSrc().isPinnableOn(link.getSrc()) &&
				this.getDst().isPinnableOn(link.getDst());
	}
	
	public double getWeight() {
		return -this.getFlow();
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
		return Integer.valueOf(this.getSrc().getId()).compareTo(other.getSrc().getId());
	}
	
	public String toPrettyString() {
		String str = String.format("#stream# src:%d|dst:%d|flow:%.5f",
				this.getSrc().getId(),
				this.getDst().getId(),
				this.getFlow());
		
		return str;
	}
	
	@Override 
	public String toString() {
		String str = String.format("DStream(src:%d|dst:%d|flow:%f",
				this.getSrc().getId(),
				this.getDst().getId(),
				this.getFlow());
		
		return str;
	}
	
	@Override 
	public int hashCode() {
		return Objects.hash(this.getSrc().getId(), this.getDst().getId());
	}
	
}
