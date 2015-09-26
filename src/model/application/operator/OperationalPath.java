package model.application.operator;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class OperationalPath extends ArrayList<Operational> {

	private static final long serialVersionUID = 3178541927310072502L;
	
	private double response;
	
	public OperationalPath(List<Operational> list) {
		super(list);
		this.setResponse(0.0);
	}
	
	public OperationalPath(Deque<Operational> queue) {
		super(queue);
		this.setResponse(0.0);
	}
	
	public OperationalPath(Operational[] list) {
		super();
		for (Operational opnode : list)
			super.add(opnode);
		this.setResponse(0.0);
	}

	public OperationalPath() {
		super();
		this.setResponse(0.0);
	}

	public double getResponse() {
		return this.response;
	}

	public void setResponse(double response) {
		this.response = response;
	}
	
	@Override
	public String toString() {
		String str = "OperationalPath(" + 
					 "response:" + this.getResponse() + ";" + 
					 "nodes:";
		
		Iterator<Operational> iter = super.iterator();
		
		while (iter.hasNext()) {
			str += iter.next().getId();
			if (iter.hasNext())
				str += ", ";
		}
		
		return str;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getResponse(), 
							super.toArray());
	}

}
