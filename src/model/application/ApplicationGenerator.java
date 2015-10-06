package model.application;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.random.RandomDataGenerator;
import model.application.opnode.OPNode;
import model.application.opnode.OPRole;

public class ApplicationGenerator {
	
	
	
	private static final String APP_NAME 	 = "Sample Application";
	private static final String APP_DESC 	 = "Randomly generated";
	
	private static final double OPN_CONN[] 		= {50.0, 100.0};
	
	private static final int 	OPN_SRC = 1;
	private static final int 	OPN_PIP = 1;
	private static final int 	OPN_SNK = 1;
	
	private static final double	SRC_PROD[]		= {1000, 10000};
	private static final double PIP_CONS[]		= {0.5, 0.8};
	private static final double SNK_CONS[]		= {0.7, 0.9};
	
	private static final int 	OPN_RESOURCES[] = {1, 2};
	private static final double OPN_SPEED[] 	= {5.0, 10.0};	
	
	private RandomDataGenerator rnd;
	
	private String 	name;
	private String 	desc;	
	
	private int 	srcnodes;
	private int 	pipnodes;
	private int 	snknodes;
	private double 	opnConn[];
	
	private double 	srcProd[];
	private double 	pipCons[];
	private double 	snkCons[];
	
	private int 	opnResources[];
	private double 	opnSpeed[];	

	public ApplicationGenerator() {
		this.opnConn = new double[2];
		
		this.srcProd = new double[2];
		this.pipCons = new double[2];
		this.snkCons = new double[2];
		
		this.opnResources = new int[2];
		this.opnSpeed = new double[2];
		
		this.reset();
	}
	
	
	/********************************************************************************
	 * General		
	 ********************************************************************************/
	public ApplicationGenerator setName(final String name) {
		if (name == null || name.isEmpty()) {
			this.name = APP_NAME;
			return this;
		}
		this.name = name;
		return this;
	}
	
	public ApplicationGenerator setDescription(final String desc) {
		if (desc == null || desc.isEmpty()) {
			this.desc = APP_DESC;
			return this;
		}
		this.desc = desc;
		return this;
	}
	
	
	/********************************************************************************
	 * Topology		
	 ********************************************************************************/
	public ApplicationGenerator setSRC(final int srcnodes) {
		if (srcnodes < 1) {
			this.srcnodes = OPN_SRC;
			return this;
		}
		this.srcnodes = srcnodes;
		return this;
	}
	
	public ApplicationGenerator setPIP(final int pipnodes) {
		if (pipnodes < 1) {
			this.pipnodes = OPN_PIP;
			return this;
		}
		this.pipnodes = pipnodes;
		return this;
	}
	
	public ApplicationGenerator setSNK(final int snknodes) {
		if (snknodes < 1) {
			this.snknodes = OPN_SNK;
			return this;
		}			
		this.snknodes = snknodes;
		return this;
	}
	
	public ApplicationGenerator setOPNodeConnectivity(final double min, final double max) {
		if (min > max || min <= 0 || max <= 0) {
			this.opnConn[0] = OPN_CONN[0];
			this.opnConn[1] = OPN_CONN[1];
			return this;
		}			
		this.opnConn[0] = min;
		this.opnConn[1] = max;
		return this;
	}
	
	
	/********************************************************************************
	 * Computational: OPNodes		
	 ********************************************************************************/
	public ApplicationGenerator setSRCProd(final double min, final double max) {
		if (min > max || min <= 0 || max <= 0) {
			this.srcProd[0] = SRC_PROD[0];
			this.srcProd[1] = SRC_PROD[1];
			return this;
		}
		this.srcProd[0] = min;
		this.srcProd[1] = max;
		return this;
	}
	
	public ApplicationGenerator setPIPCons(final double min, final double max) {
		if (min > max || min <= 0 || max <= 0) {
			this.pipCons[0] = PIP_CONS[0];
			this.pipCons[1] = PIP_CONS[1];
			return this;
		}
		this.pipCons[0] = min;
		this.pipCons[1] = max;
		return this;
	}
	
	public ApplicationGenerator setSNKCons(final double min, final double max) {
		if (min > max || min <= 0 || max <= 0) {
			this.snkCons[0] = SNK_CONS[0];
			this.snkCons[1] = SNK_CONS[1];
			return this;
		}
		this.snkCons[0] = min;
		this.snkCons[1] = max;
		return this;
	}

	public ApplicationGenerator setOPNodeResources(final int min, final int max) {
		if (min > max || min <= 0 || max <= 0) {
			this.opnResources[0] = OPN_RESOURCES[0];
			this.opnResources[1] = OPN_RESOURCES[1];
			return this;
		}
		this.opnResources[0] = min;
		this.opnResources[1] = max;
		return this;
	}
	
	public ApplicationGenerator setOPNodeSpeed(final double min, final double max) {
		if (min > max || min <= 0 || max <= 0) {
			this.opnSpeed[0] = OPN_SPEED[0];
			this.opnSpeed[1] = OPN_SPEED[1];
			return this;
		}
		this.opnSpeed[0] = min;
		this.opnSpeed[1] = max;
		return this;
	}
		
	
	/********************************************************************************
	 * Creation		
	 ********************************************************************************/
	public Application create() {
		Application app = new Application();
		Set<OPNode> srcs = new HashSet<OPNode>();
		Set<OPNode> snks = new HashSet<OPNode>();
		Set<OPNode> pips = new HashSet<OPNode>();	
		Set<OPNode> visited = new HashSet<OPNode>();
		
		app.setName(this.name);
		app.setDescription(this.desc);
		
		int nxtid = 0;
		
		for (int srcnode = 1; srcnode <= this.srcnodes; srcnode++, nxtid++) {
			double prod = this.rnd.nextUniform(this.srcProd[0], this.srcProd[1]);
			int resources = this.rnd.nextInt(this.opnResources[0], this.opnResources[1]);
			double speed = this.rnd.nextUniform(this.opnSpeed[0], this.opnSpeed[1]);
			OPNode node = new OPNode(nxtid, OPRole.SRC, "src" + srcnode, x -> prod, resources, speed);			
			srcs.add(node);
		}
		
		for (int pipnode = 1; pipnode <= this.pipnodes; pipnode++, nxtid++) {
			double cons = this.rnd.nextUniform(this.pipCons[0], this.pipCons[1]);
			int resources = this.rnd.nextInt(this.opnResources[0], this.opnResources[1]);
			double speed = this.rnd.nextUniform(this.opnSpeed[0], this.opnSpeed[1]);
			OPNode node = new OPNode(nxtid, OPRole.SNK, "opr" + pipnode, x -> x * cons, resources, speed);
			pips.add(node);
		}		
		
		for (int snknode = 1; snknode <= this.snknodes; snknode++, nxtid++) {	
			double cons = this.rnd.nextUniform(this.snkCons[0], this.snkCons[1]);
			int resources = this.rnd.nextInt(this.opnResources[0], this.opnResources[1]);
			double speed = this.rnd.nextUniform(this.opnSpeed[0], this.opnSpeed[1]);
			OPNode node = new OPNode(nxtid, OPRole.SNK, "src" + snknode, x -> x * cons, resources, speed);
			snks.add(node);
		}				
		
		for (OPNode srcnode : srcs.stream().sorted().collect(Collectors.toList())) {
			OPNode pipnode = (OPNode) this.rnd.nextSample(pips, 1)[0];
			if (app.addStream(srcnode, pipnode))
				visited.add(pipnode);
		}
		
		while (!visited.containsAll(pips)) {
			OPNode pipSRC = (OPNode) this.rnd.nextSample(visited, 1)[0];
			OPNode pipDST = (OPNode) this.rnd.nextSample(pips.stream().filter(e -> !visited.contains(e)).collect(Collectors.toSet()), 1)[0];
			
			if (pipSRC.getId() == pipDST.getId())
				continue;
			
			if (app.addStream(pipSRC, pipDST))
				visited.add(pipDST);			
		}
		
		for (OPNode pipnode : app.getPipes().stream().filter(n -> app.outDegreeOf(n) == 0).collect(Collectors.toList())) {
			OPNode snknode = (OPNode) this.rnd.nextSample(snks, 1)[0];
			app.addStream(pipnode, snknode);
		}
			
		
		for (OPNode snknode : snks.stream().sorted().collect(Collectors.toList())) {
			OPNode pipnode = (OPNode) this.rnd.nextSample(pips, 1)[0];
			app.addStream(pipnode, snknode);
		}			
		
		return app;		
	}
	
	private void reset() {
		this.rnd = new RandomDataGenerator();
		
		this.name = APP_NAME;
		this.desc = APP_DESC;
		
		this.srcnodes = OPN_SRC;
		this.pipnodes = OPN_PIP;
		this.snknodes = OPN_SNK;		
		this.opnConn  = OPN_CONN;		
		
		this.srcProd[0] = SRC_PROD[0];
		this.srcProd[1] = SRC_PROD[1];
		this.pipCons[0] = PIP_CONS[0];
		this.pipCons[1] = PIP_CONS[1];
		this.snkCons[0] = SNK_CONS[0];
		this.snkCons[1] = SNK_CONS[1];	
		
		this.opnResources[0] = OPN_RESOURCES[0];
		this.opnResources[1] = OPN_RESOURCES[1];
		this.opnSpeed[0] = OPN_SPEED[0];
		this.opnSpeed[1] = OPN_SPEED[1];
		
		
	}

}
