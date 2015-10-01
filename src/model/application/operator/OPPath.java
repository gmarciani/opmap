package model.application.operator;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import model.application.dstream.DStream;

public class OPPath extends ArrayList<DStream> {

	private static final long serialVersionUID = 3178541927310072502L;
	
	public OPPath(List<DStream> list) {
		super(list);
	}
	
	public OPPath(Deque<DStream> queue) {
		super(queue);
	}
	
	public OPPath(DStream[] list) {
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
	
	public String toPrettyString() {
		String str = "#oppath# ";
		
		if (super.isEmpty()) {
			str += "empty";
			return str;
		}
		
		str += "streams:";
		
		Iterator<DStream> iter = super.iterator();
		
		while (iter.hasNext()) {
			DStream dstream = iter.next();
			str += "(" + dstream.getSrc().getId() + "," + dstream.getDst().getId() + ")";
			if (iter.hasNext())
				str += "|";
		}
		
		return str;
	}
	
	@Override
	public String toString() {
		String str = "OPPath(" + 
					 "streams:";
		
		Iterator<DStream> iter = super.iterator();
		
		while (iter.hasNext()) {
			DStream dstream = iter.next();
			str += "(" + dstream.getSrc().getId() + "," + dstream.getDst().getId() + ")";
			if (iter.hasNext())
				str += ", ";
			else
				str += ")";
		}
		
		return str;
	}

}
