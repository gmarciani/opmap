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
	
	public Architecture(final String name, final String description) {
		super(Link.class);
		this.setName(name);
		this.setDescription(description);	
	}
	
	public Architecture(final String name) {
		this(name, null);
	}	
	
	public Architecture() {
		this(null, null);
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(final String description) {
		this.description = description;
	}
	
	public static Architecture readJSON(final String json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Architecture arc = mapper.readValue(json, Architecture.class);
		return arc;		
	}
	
	public static String writeJSON(final Architecture arc) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(arc);	
		return json;
	}
	
	public String toPrettyString() {
		String str = String.format("#architecture#\nname:%s\ndesc:%s\nexnodes:\n%s\nlinks:\n%s",
				this.getName(),
				this.getDescription(),
				this.vertexSet().stream().sorted().map(exnode -> exnode.toPrettyString()).collect(Collectors.joining("\n")),
				this.edgeSet().stream().sorted().map(link ->link.toPrettyString()).collect(Collectors.joining("\n")));		
		
		return str;
	}
	
	@Override 
	public String toString() {
		String str = String.format("Architecture(name:%s|desc:%s|exnodes:%s|links:%s)",
				this.getName(),
				this.getDescription(),
				this.vertexSet(),
				this.edgeSet());
		
		return str;
	}
	
}
