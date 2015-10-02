package model.architecture;

import java.io.IOException;
import java.util.stream.Collectors;

import org.jgrapht.graph.DefaultDirectedGraph;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.architecture.exnode.EXNode;
import model.architecture.link.Link;

public class Architecture extends DefaultDirectedGraph<EXNode, Link> {

	private static final long serialVersionUID = 7470862065393196611L;
	
	private String name;
	private String description;
	
	public Architecture(String name, String description) {
		super(Link.class);
		this.setName(name);
		this.setDescription(description);	
	}
	
	public Architecture(String name) {
		this(name, null);
	}	
	
	public Architecture() {
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
	
	public String toPrettyString() {
		String str = "# Architecture #\n";
		
		str += "name: " + this.getName() + "\n";
		str += "desc: " + this.getDescription() + "\n";
		str += "nodes:\n";
		
		for (EXNode exnode : this.vertexSet().stream().sorted().collect(Collectors.toList()))
			str += "\t" + exnode.toPrettyString() + "\n";
		
		str += "edges:\n";
		
		for (Link link : this.edgeSet().stream().sorted().collect(Collectors.toList()))
			str += "\t" + link.toPrettyString() + "\n";			
		
		return str;
	}
	
	@Override 
	public String toString() {
		return "Architecture(" + 
			   "name:" + this.getName() + ";" + 
			   "description:" + this.getDescription() + ";" + 
			   "nodes:" +  this.vertexSet() + ";" + 
			   "links:" + this.edgeSet() + ")";
	}
	
}
