package model.architecture;

import java.io.IOException;

import org.jgrapht.graph.DefaultDirectedGraph;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.architecture.link.LogicalLink;
import model.architecture.node.Computational;

public class Architecture extends DefaultDirectedGraph<Computational, LogicalLink> {

	private static final long serialVersionUID = 7470862065393196611L;
	
	private String name;
	private String description;
	
	public Architecture(String name, String description) {
		super(LogicalLink.class);
		this.setName(name);
		this.setDescription(description);	
	}
	
	public Architecture(String name) {
		super(LogicalLink.class);
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
	
	public static Architecture readJSON(String json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Architecture arc = mapper.readValue(json, Architecture.class);
		return arc;		
	}
	
	public static String writeJSON(Architecture arc) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(arc);	
		return json;
	}
	
	@Override public String toString() {
		return "Architecture(" + 
			   "name:" + this.getName() + ";" + 
			   "description:" + this.getDescription() + ";" + 
			   "nodes:" +  this.vertexSet() + ";" + 
			   "links:" + this.edgeSet() + ")";
	}
	
}
