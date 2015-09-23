package model.architecture;

import java.io.IOException;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.architecture.link.LogicalLink;
import model.architecture.node.ComputationalNode;

public class Architecture {

	private String name;
	private String description;
	private DirectedGraph<ComputationalNode, LogicalLink> graph;
	
	public Architecture(String name, String description, DirectedGraph<ComputationalNode, LogicalLink> graph) {
		this.setName(name);
		this.setDescription(description);
		this.setGraph(graph);
	}
	
	public Architecture(String name, String description) {
		this.setName(name);
		this.setDescription(description);
		this.setGraph(new DefaultDirectedGraph<ComputationalNode, LogicalLink>(LogicalLink.class));		
	}
	
	public Architecture(String name) {
		this.setName(name);
		this.setDescription(null);
		this.setGraph(new DefaultDirectedGraph<ComputationalNode, LogicalLink>(LogicalLink.class));	
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
	
	public DirectedGraph<ComputationalNode, LogicalLink> getGraph() {
		return this.graph;
	}
	
	public void setGraph(DirectedGraph<ComputationalNode, LogicalLink> graph) {
		this.graph = graph;
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
	
	public static Architecture getSampleArchitecture() {
		// TODO Auto-generated method stub
		return null;
	}	
	
	@Override public String toString() {
		return "Architecture(name:" + this.getName() + ";" + 
							 "description:" + this.getDescription() + ";" + 
							 "graph:" + this.getGraph() + ")";
	}

	public void addComputationalNode(ComputationalNode node1) {
		// TODO Auto-generated method stub
		
	}

	public void addLogicalLink(int i, int j, LogicalLink logicalLink) {
		// TODO Auto-generated method stub
		
	}
	
}
