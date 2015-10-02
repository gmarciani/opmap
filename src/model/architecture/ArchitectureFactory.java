package model.architecture;

import java.util.Random;

import commons.Randomizer;
import model.architecture.exnode.EXNode;
import model.architecture.link.Link;

public class ArchitectureFactory {
	
	private static final String DEFAULT_NAME = "";
	private static final String DEFAULT_DESCRIPTION = "";
	private static final int DEFAULT_NODES = 10;
	
	protected Random rnd;
	
	private String name;
	private String description;
	private int nodes;	

	public ArchitectureFactory() {
		this.clear();
	}
	
	public ArchitectureFactory setName(final String name) {
		this.name = name;
		return this;
	}
	
	public ArchitectureFactory setDescription(final String description) {
		this.description = description;
		return this;
	}
	
	public ArchitectureFactory setNodes(final int nodes) {
		this.nodes = nodes;
		return this;
	}	
	
	public Architecture create() {
		Architecture arc = new Architecture();
		
		arc.setName(this.name);
		arc.setDescription(this.description);
		
		for (int i = 0; i < this.nodes; i++) {
			EXNode node = new EXNode(i, "exnode" + i,  4, Randomizer.rndDouble(this.rnd, 1.0, 10.0), Randomizer.rndDouble(this.rnd, 0.5, 1.0));
			arc.addVertex(node);
		}
		
		for (EXNode exnodeSRC : arc.vertexSet()) {
			for (EXNode exnodeDST : arc.vertexSet()) {
				if (exnodeSRC.getId() == exnodeDST.getId()) {
					arc.addEdge(exnodeSRC, exnodeDST, new Link(exnodeSRC, exnodeDST, 0.0, Double.MAX_VALUE, 1.0));
				} else {
					arc.addEdge(exnodeSRC, exnodeDST, new Link(exnodeSRC, exnodeDST, Randomizer.rndDouble(this.rnd, 1.0, 100.0), Randomizer.rndDouble(this.rnd, 1.0, 1000.0), Randomizer.rndDouble(this.rnd, 0.5, 1.0)));
					arc.addEdge(exnodeDST, exnodeSRC, new Link(exnodeSRC, exnodeDST, Randomizer.rndDouble(this.rnd, 1.0, 100.0), Randomizer.rndDouble(this.rnd, 1.0, 1000.0), Randomizer.rndDouble(this.rnd, 0.5, 1.0)));
				}					
			}				
		}
		
		this.clear();
		
		return arc;
	}
	
	private void clear() {
		this.rnd = new Random();
		this.name = DEFAULT_NAME;
		this.description = DEFAULT_DESCRIPTION;
		this.nodes = DEFAULT_NODES;
	}

}
