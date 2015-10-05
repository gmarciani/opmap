package model.application;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import commons.Randomizer;
import model.application.opnode.OPNode;
import model.application.opnode.OPRole;

public class ApplicationFactory {
	
	private static final String DEFAULT_NAME = "";
	private static final String DEFAULT_DESCRIPTION = "";
	private static final int DEFAULT_SRCNODES = 1;
	private static final int DEFAULT_PIPNODES = 1;
	private static final int DEFAULT_SNKNODES = 1;
	private static final long DEFAULT_SRCPROD = 100000;
	private static final double DEFAULT_PIPCONS = 0.95;
	
	private Random rnd;
	
	private String name;
	private String description;
	private int srcnodes;
	private int pipnodes;
	private int snknodes;

	public ApplicationFactory() {
		this.clear();
	}
	
	public ApplicationFactory setName(final String name) {
		this.name = name;
		return this;
	}
	
	public ApplicationFactory setDescription(final String description) {
		this.description = description;
		return this;
	}
	
	public ApplicationFactory setNodes(final int pipnodes) {
		this.pipnodes = pipnodes;
		return this;
	}
	
	public Application create() {
		Application app = new Application();
		Set<OPNode> srcs = new HashSet<OPNode>();
		Set<OPNode> snks = new HashSet<OPNode>();
		Set<OPNode> pips = new HashSet<OPNode>();	
		Set<OPNode> visited = new HashSet<OPNode>();
		
		app.setName(this.name);
		app.setDescription(this.description);
		
		int nxtid = 0;
		
		for (int srcnode = 1; srcnode <= this.srcnodes; srcnode++, nxtid++) {
			OPNode node = new OPNode(nxtid, OPRole.SRC, "src" + srcnode, x -> new Long(DEFAULT_SRCPROD), 1, Randomizer.rndDouble(this.rnd, 1.0, 100.0));
			srcs.add(node);
		}
		
		for (int pipnode = 1; pipnode <= this.pipnodes; pipnode++, nxtid++) {
			OPNode node = new OPNode(nxtid, OPRole.PIP, "opr" + pipnode, x -> Math.round(x * DEFAULT_PIPCONS), 1,	Randomizer.rndDouble(this.rnd, 1.0, 100.0));
			pips.add(node);
		}		
		
		for (int snknode = 1; snknode <= this.snknodes; snknode++, nxtid++) {
			OPNode node = new OPNode(nxtid, OPRole.SNK, "snk" + snknode, x -> Math.round(x * DEFAULT_PIPCONS), 1, Randomizer.rndDouble(this.rnd, 1.0, 100.0));
			snks.add(node);
		}				
		
		for (OPNode srcnode : srcs.stream().sorted().collect(Collectors.toList())) {
			OPNode pipnode = Randomizer.rndItem(this.rnd, pips);
			if (app.addStream(srcnode, pipnode))
				visited.add(pipnode);
		}
		
		while (!visited.containsAll(pips)) {
			OPNode pipSRC = Randomizer.rndItem(this.rnd, visited);
			OPNode pipDST = Randomizer.rndItem(this.rnd, pips.stream().filter(e -> !visited.contains(e)).collect(Collectors.toSet()));
			
			if (pipSRC.getId() == pipDST.getId())
				continue;
			
			if (app.addStream(pipSRC, pipDST))
				visited.add(pipDST);			
		}
		
		for (OPNode pipnode : app.getPipes().stream().filter(n -> app.outDegreeOf(n) == 0).collect(Collectors.toList())) {
			OPNode snknode = Randomizer.rndItem(this.rnd, snks);
			app.addStream(pipnode, snknode);
		}
			
		
		for (OPNode snknode : snks.stream().sorted().collect(Collectors.toList())) {
			OPNode pipnode = Randomizer.rndItem(this.rnd, pips);
			app.addStream(pipnode, snknode);
		}			
		
		return app;		
	}
	
	private void clear() {
		this.rnd = new Random();
		this.name = DEFAULT_NAME;
		this.description = DEFAULT_DESCRIPTION;
		this.srcnodes = DEFAULT_SRCNODES;
		this.pipnodes = DEFAULT_PIPNODES;
		this.snknodes = DEFAULT_SNKNODES;
	}

}
