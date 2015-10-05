package model.application;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.alg.BellmanFordShortestPath;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.application.dstream.DStream;
import model.application.opnode.OPNode;
import model.application.opnode.OPPath;

public class Application extends DirectedAcyclicGraph<OPNode, DStream> {

	private static final long serialVersionUID = 7253751791492037927L;
	
	private String name;
	private String description;
	
	public Application(String name, String description) {
		super(DStream.class);
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
	
	public Set<OPNode> getSources() {
		return super.vertexSet().stream().filter(v -> v.isSource()).collect(Collectors.toSet());
	}
	
	public Set<OPNode> getSinks() {
		return super.vertexSet().stream().filter(v -> v.isSink()).collect(Collectors.toSet());
	}
	
	public Set<OPNode> getPipes() {
		return super.vertexSet().stream().filter(v -> v.isPipe()).collect(Collectors.toSet());
	}
	
	public boolean addOperational(OPNode opnode) {
		return super.addVertex(opnode);
	}
		
	public boolean addStream(OPNode src, OPNode dst) {
		DStream dstream = new DStream(src, dst);
		if (!super.vertexSet().contains(dstream.getSrc()))
			super.addVertex(dstream.getSrc());
		if (!super.vertexSet().contains(dstream.getDst()))
			super.addVertex(dstream.getDst());	
		try {
			if (super.addDagEdge(dstream.getSrc(), dstream.getDst(), dstream)) {
				super.setEdgeWeight(dstream, dstream.getWeight());
				return true;
			}
			return false;
		} catch (CycleFoundException exc) {
			return false;
		}
	}
	
	public Set<OPPath> getAllOperationalPaths() {
		Set<OPPath> paths = new HashSet<OPPath>();
		
		for (OPNode source : this.getSources())
			paths.addAll(this.getOperationalPaths(source));
		
		return paths;
	}
	
	public Set<OPPath> getOperationalPaths(OPNode srcnode) {
		Set<OPPath> paths = new HashSet<OPPath>();
		
		for (OPNode snknode : this.getSinks()) {
			List<DStream> pathEdge = this.findWorstPath(srcnode, snknode);
			if (pathEdge == null)
				continue;
			OPPath path = new OPPath(pathEdge);
			paths.add(path);
		}			
		
		return paths;
	}
	
	protected List<DStream> findWorstPath(OPNode srcnode, OPNode snknode) {
		return BellmanFordShortestPath.findPathBetween(this, srcnode, snknode);
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
	
	public static boolean isConsistent(final Application app) {
		ConnectivityInspector<OPNode, DStream> inspector = new ConnectivityInspector<OPNode, DStream>(app);
		
		for (OPNode srcnode : app.getSources())
			for (OPNode snknode : app.getSinks())
				if (!inspector.pathExists(srcnode, snknode))
					return false;
		return true;
	}
	
	public String toPrettyString() {
		String str = "# Application #\n";
		
		str += "name: " + this.getName() + "\n";
		str += "desc: " + this.getDescription() + "\n";
		str += "nodes:\n";
		
		for (OPNode opnode : this.vertexSet().stream().sorted().collect(Collectors.toList()))
			str += "\t" + opnode.toPrettyString() + "\n";
		
		str += "edges:\n";
		
		for (DStream dstream : this.edgeSet().stream().sorted().collect(Collectors.toList()))
			str += "\t" + dstream.toPrettyString() + "\n";			
		
		return str;
	}
	
	@Override 
	public String toString() {
		return "Application(" + 
			   "name:" + this.getName() + ";" + 
			   "description:" + this.getDescription() + ";" + 
			   "sources:" + this.getSources() + ";" + 
			   "sinks:" + this.getSinks() + ";" +
			   "pipes:" + this.getPipes() + ";" +
			   "streams:" + this.edgeSet() + ")";
	}

}
