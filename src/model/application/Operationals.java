package model.application;

import java.util.Iterator;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;

import model.application.operator.OperationalNode;

public class Operationals extends ConcurrentSkipListSet<OperationalNode> {

	private static final long serialVersionUID = 8867010805568271165L;

	public Operationals() {
		super();
	}
	
	public OperationalNode getById(long id) {
		OperationalNode curr;
		
		Iterator<OperationalNode> iter = this.iterator();
		
		while (iter.hasNext()) {
			curr = iter.next();
			if (curr.getId() == id)
				break;
		}
		
		return null;
	}
	
	public NavigableSet<OperationalNode> getSources() {
		NavigableSet<OperationalNode> sources = new ConcurrentSkipListSet<OperationalNode>();
		OperationalNode curr;
		
		Iterator<OperationalNode> iter = this.iterator();
		
		while (iter.hasNext()) {
			curr = iter.next();
			if (curr.isSource())
				sources.add(curr);
		}			
		
		return sources;
	}
	
	public NavigableSet<OperationalNode> getSinks() {
		NavigableSet<OperationalNode> sinks = new ConcurrentSkipListSet<OperationalNode>();
		OperationalNode curr;

		Iterator<OperationalNode> iter = this.iterator();
		
		while (iter.hasNext()) {
			curr = iter.next();
			if (curr.isSink())
				sinks.add(curr);
		}			
		
		return sinks;
	}
	
	public NavigableSet<OperationalNode> getPipes() {
		NavigableSet<OperationalNode> pipes = new ConcurrentSkipListSet<OperationalNode>();
		OperationalNode curr;

		Iterator<OperationalNode> iter = this.iterator();
		
		while (iter.hasNext()) {
			curr = iter.next();
			if (curr.isPipe())
				pipes.add(curr);
		}			
		
		return pipes;
	}

}
