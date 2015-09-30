package model.application;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.application.dstream.DataStream;
import model.application.operator.Operational;
import model.application.operator.OperationalPath;

public class Application extends DirectedAcyclicGraph<Operational, DataStream> {

	private static final long serialVersionUID = -6105440850975900864L;
	
	private String name;
	private String description;
	
	public Application(String name, String description) {
		super(DataStream.class);
		this.setName(name);
		this.setDescription(description);
	}
	
	public Application(String name) {
		this(name, null);
	}
	
	public Application() {
		this(null, null);
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Set<Operational> getSources() {
		return super.vertexSet().stream().filter(v -> v.isSource()).collect(Collectors.toSet());
	}
	
	public Set<Operational> getSinks() {
		return super.vertexSet().stream().filter(v -> v.isSink()).collect(Collectors.toSet());
	}
	
	public Set<Operational> getPipes() {
		return super.vertexSet().stream().filter(v -> v.isPipe()).collect(Collectors.toSet());
	}
	
	public boolean addOperational(Operational opnode) {
		return super.addVertex(opnode);
	}
		
	public boolean addStream(Operational src, Operational dst) {
		DataStream dstream = new DataStream();
		dstream.setFlow(src.getFlowOut());
		dst.addFlowIn(dstream.getFlow());
		super.addVertex(src);
		super.addVertex(dst);
		try {
			return super.addDagEdge(src, dst, dstream);
		} catch (org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException exc) {
			return false;
		}
	}
	
	public Set<OperationalPath> getAllOperationalPaths() {
		Set<OperationalPath> paths = new HashSet<OperationalPath>();
		
		for (Operational source : this.getSources())
			paths.addAll(this.getOperationalPaths(source));
		
		return paths;
	}
	
	public Set<OperationalPath> getOperationalPaths(Operational source) {
		Set<OperationalPath> paths = new HashSet<OperationalPath>();
		
		for (Operational sink : this.getSinks()) {
			List<DataStream> pathEdge = DijkstraShortestPath.findPathBetween(this, source, sink);
			if (pathEdge == null)
				continue;
			OperationalPath path = new OperationalPath();
			Iterator<DataStream> iter = pathEdge.iterator();			
			while(iter.hasNext()) {
				DataStream dstream = iter.next();
				path.add(this.getEdgeSource(dstream));
				if (!iter.hasNext())
					path.add(this.getEdgeTarget(dstream));
			}	
			paths.add(path);
		}			
		
		return paths;
	}
	
	public static Application readJSON(String json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Application app = mapper.readValue(json, Application.class);
		return app;		
	}
	
	public static String writeJSON(Application app) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(app);	
		return json;
	}
	
	@Override public String toString() {
		return "Application(" + 
			   "name:" + this.getName() + ";" + 
			   "description:" + this.getDescription() + ";" + 
			   "sources:" + this.getSources() + ";" + 
			   "sinks:" + this.getSinks() + ";" +
			   "pipes:" + this.getPipes() + ";" +
			   "streams:" + this.edgeSet() + ")";
	}

}
