package model.application.opnode;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import model.application.dstream.DStream;

public class OPPath extends ArrayList<DStream> {

	private static final long serialVersionUID = 3178541927310072502L;
	
	public OPPath(final List<DStream> list) {
		super(list);
	}
	
	public OPPath(final Deque<DStream> queue) {
		super(queue);
	}
	
	public OPPath(final DStream[] list) {
		super();
		for (DStream dstream : list)
			super.add(dstream);
	}

	public OPPath() {
		super();
	}
	
	public List<OPNode> getNodes() {
		List<OPNode> nodes = new ArrayList<OPNode>();
		
		Iterator<DStream> iter = super.iterator();			
		while(iter.hasNext()) {
			DStream dstream = iter.next();
			nodes.add(dstream.getSrc());
			if (!iter.hasNext())
				nodes.add(dstream.getDst());
		}	

		return nodes;
	}
	
	public OPNode getSrc() {
		return super.get(0).getSrc();
	}
	
	public OPNode getDst() {
		return super.get(super.size() - 1).getDst();
	}
	
	public String toPrettyString() {
		String str = "#oppath# ";
		str = String.format("#oppath# src:%d|dst%d|opnodes:%s|streams:%s", 
				this.getSrc().getId(),
				this.getDst().getId(),
				this.getNodes().stream().map(opnode -> Integer.valueOf(opnode.getId()).toString()).collect(Collectors.joining(",")),
				super.stream().map(stream -> "(" + stream.getSrc().getId() + "," + stream.getDst().getId() + ")").collect(Collectors.joining(",")));
		
		return str;
	}
	
	@Override
	public String toString() {
		String str = String.format("OPPath(streams:",
				super.stream().map(stream -> "(" + stream.getSrc().getId() + "," + stream.getDst().getId() + ")").collect(Collectors.joining(",")));
		
		return str;
	}

}
