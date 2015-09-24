package model.application;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import model.application.dstream.DataStream;
import model.application.operator.Operational;
import model.application.operator.OperationalPath;

public class Application extends DirectedSparseGraph<Operational, DataStream> {

	private static final long serialVersionUID = -6105440850975900864L;
	
	private String name;
	private String description;
	
	public Application(String name, String description) {
		super();
		this.setName(name);
		this.setDescription(description);
	}
	
	public Application(String name) {
		super();
		this.setName(name);
		this.setDescription(null);
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
		return super.getVertices().stream().filter(v -> v.isSource()).collect(Collectors.toSet());
	}
	
	public Set<Operational> getSinks() {
		return super.getVertices().stream().filter(v -> v.isSink()).collect(Collectors.toSet());
	}
	
	public Set<Operational> getPipes() {
		return super.getVertices().stream().filter(v -> v.isPipe()).collect(Collectors.toSet());
	}
		
	public boolean addStream(Operational src, Operational dst) {
		DataStream dstream = new DataStream();
		dstream.setFlow(src.getFlowOut());
		dst.addFlowIn(dstream.getFlow());
		return super.addEdge(dstream, src, dst);
	}
	
	public Set<OperationalPath> getAllOperationalPaths() {
		Set<OperationalPath> paths = new ConcurrentSkipListSet<OperationalPath>();
		
		for (Operational source : this.getSources())
			paths.addAll(this.getOperationalPaths(source));		
		
		return paths;
	}
	
	public Set<OperationalPath> getOperationalPaths(Operational source) {
		Set<OperationalPath> paths = new ConcurrentSkipListSet<OperationalPath>();
		
		//TO-DO
		
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
			   "streams:" + this.getEdges() + ")";
	}

}
