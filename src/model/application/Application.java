package model.application;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import model.application.operator.OperationalNode;
import model.application.stream.Stream;

public class Application implements Serializable {

	private static final long serialVersionUID = -6105440850975900864L;
	
	private String name;
	private Operationals operationals;
	private Streams streams;
	protected DirectedAcyclicGraph<Long, Stream> graph;
	
	public Application(String name) {
		this.name = name;
		this.operationals = new Operationals();
		this.streams = new Streams();
		this.graph = new DirectedAcyclicGraph<Long, Stream>(Stream.class);
	}
	
	public String getName() {
		return this.name;
	}
	
	public Operationals getOperationals() {
		return this.operationals;
	}
	
	public Streams getStreams() {
		return this.streams;
	}
	
	public boolean addOperational(OperationalNode node) {
		boolean result;
		
		if (result = this.getOperationals().add(node)) {
			if (!(result = this.graph.addVertex(node.getId())))
				this.getOperationals().remove(node);
		}
		
		return result;
	}
	
	public boolean addStream(long src, long dst) {
		boolean result;		
		
		OperationalNode srcNode = this.getOperationals().getById(src);
		OperationalNode dstNode = this.getOperationals().getById(dst);
		
		Stream stream = new Stream(src, dst, srcNode.getComputationalDemand().getFlowOut());
		
		
		
		result = this.streams.add(stream);
		
		this.graph.addEdge(srcNode, dstNode, stream);
		
		return false;
	}
	
	public Set<OperationalPath> getAllOperationalPaths() {
		Set<OperationalPath> paths = new ConcurrentSkipListSet<OperationalPath>();
		
		for (OperationalNode source : this.getOperationals().getSources()) {
			for (OperationalNode sink : this.getOperationals().getSinks()) {
				paths.addAll(this.getOperationalPaths(source, sink));
			}
		}
		
		return paths;
	}
	
	public Set<OperationalPath> getOperationalPaths(OperationalNode source, OperationalNode sink) {
		Set<OperationalPath> paths = new ConcurrentSkipListSet<OperationalPath>();
		
		//TO-DO
		
		return paths;
	}	
	
	@Override public String toString() {
		return "Application(" + 
			   "name:" + this.getName() + ";" + 
			   "operationals:" + this.getOperationals() + ";" + 
			   "sources:" + this.getOperationals().getSources() + ";" + 
			   "sinks:" + this.getOperationals().getSinks() + ";" +
			   "pipes:" + this.getOperationals().getPipes() + ";" +
			   "datastreams:" + this.getStreams() + ";" + 
			   "graph:" + super.toString() + ")";
	}

}
