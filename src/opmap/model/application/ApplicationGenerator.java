package opmap.model.application;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.random.RandomDataGenerator;

import commons.GMath;
import opmap.control.exception.GeneratorException;

public final class ApplicationGenerator {	
	
	private static final String APP_NAME 	= "Sample Application";
	private static final String APP_DESC 	= "Randomly generated";	
	
	private static final int 	OPN_SRC 	= 1;
	private static final int 	OPN_PIP 	= 1;
	private static final int 	OPN_SNK 	= 1;
	private static final double OPN_CONN[] 	= {0.5, 1.0};
	private static final double OPN_PINN[]	= {1.0, 1.0};
	
	private static final double	SRC_PROD[] 	= {1000, 10000};
	private static final double PIP_CONS[] 	= {0.5, 0.8};
	private static final double SNK_CONS[] 	= {0.7, 0.9};
	
	private static final int 	OPN_RES[] 	= {1, 2};
	private static final double OPN_SPEED[] = {5.0, 10.0};		
	
	private RandomDataGenerator rnd;
	
	private String name;
	private String desc;	
	
	private int srcnodes;
	private int pipnodes;
	private int snknodes;
	
	private double opnConn[];
	private double opnConnVar;
	
	private double opnPinn[];
	private double opnPinnVar;
	
	private double srcProd[];
	private double srcProdVar;
	
	private double pipCons[];
	private double pipConsVar;
	
	private double snkCons[];
	private double snkConsVar;
	
	private int    opnRes[];
	private double opnResVar;
	
	private double opnSpeed[];	
	private double opnSpeedVar;
	
	Set<Integer> exnodes;

	public ApplicationGenerator() {		
		this.opnConn = new double[2];
		this.opnPinn = new double[2];
		this.srcProd = new double[2];
		this.pipCons = new double[2];
		this.snkCons = new double[2];		
		this.opnRes  = new int[2];
		this.opnSpeed = new double[2];	
		this.exnodes = new HashSet<Integer>();
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
	public ApplicationGenerator setSRC(final int srcnodes) throws GeneratorException {
		if (srcnodes < 1)
			throw new GeneratorException("Invalid arguments");
		this.srcnodes = srcnodes;
		return this;
	}
	
	public ApplicationGenerator setPIP(final int pipnodes) throws GeneratorException {
		if (pipnodes < 1)
			throw new GeneratorException("Invalid arguments");
		this.pipnodes = pipnodes;
		return this;
	}
	
	public ApplicationGenerator setSNK(final int snknodes) throws GeneratorException {
		if (snknodes < 1)
			throw new GeneratorException("Invalid arguments");
		this.snknodes = snknodes;
		return this;
	}
	
	public ApplicationGenerator setOPNodeConnectivity(final double min, final double max) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0)
			throw new GeneratorException("Invalid arguments");
		this.opnConn[0] = min;
		this.opnConn[1] = max;
		this.opnConnVar = 0.0;
		return this;
	}
	
	public ApplicationGenerator setOPNodeConnectivity(final double min, final double max, final double var) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0 || var < 0.0)
			throw new GeneratorException("Invalid arguments");
		this.opnConn[0] = min;
		this.opnConn[1] = max;
		this.opnConnVar = var * (max - min) / 2.0;
		return this;
	}
	
	public ApplicationGenerator setOPNodePinnability(final Set<Integer> exnodes, final double min, final double max) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0)
			throw new GeneratorException("Invalid arguments");
		this.opnPinn[0] = min;
		this.opnPinn[1] = max;
		this.opnPinnVar = 0.0;
		this.exnodes.clear();
		this.exnodes.addAll(exnodes);
		return this;
	}
	
	public ApplicationGenerator setOPNodePinnability(final Set<Integer> exnodes, final double min, final double max, final double var) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0 || var < 0.0)
			throw new GeneratorException("Invalid arguments");
		this.opnPinn[0] = min;
		this.opnPinn[1] = max;
		this.opnPinnVar = var * (max - min) / 2.0;;
		this.exnodes.clear();
		this.exnodes.addAll(exnodes);
		return this;
	}
	
	
	/********************************************************************************
	 * Computational Demand: OPNodes	
	 ********************************************************************************/
	public ApplicationGenerator setSRCProd(final double min, final double max) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0)
			throw new GeneratorException("Invalid arguments");
		this.srcProd[0] = min;
		this.srcProd[1] = max;
		this.srcProdVar = 0.0;
		return this;
	}
	
	public ApplicationGenerator setSRCProd(final double min, final double max, final double var) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0 || var < 0.0)
			throw new GeneratorException("Invalid arguments");
		this.srcProd[0] = min;
		this.srcProd[1] = max;
		this.srcProdVar = var * (max - min) / 2.0;;
		return this;
	}
	
	public ApplicationGenerator setPIPCons(final double min, final double max) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0)
			throw new GeneratorException("Invalid arguments");
		this.pipCons[0] = min;
		this.pipCons[1] = max;
		this.pipConsVar = 0.0;
		return this;
	}
	
	public ApplicationGenerator setPIPCons(final double min, final double max, final double var) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0 || var < 0.0)
			throw new GeneratorException("Invalid arguments");
		this.pipCons[0] = min;
		this.pipCons[1] = max;
		this.pipConsVar = var * (max - min) / 2.0;;
		return this;
	}
	
	public ApplicationGenerator setSNKCons(final double min, final double max) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0)
			throw new GeneratorException("Invalid arguments");
		this.snkCons[0] = min;
		this.snkCons[1] = max;
		this.snkConsVar = 0.0;
		return this;
	}
	
	public ApplicationGenerator setSNKCons(final double min, final double max, final double var) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0 || var < 0.0)
			throw new GeneratorException("Invalid arguments");
		this.snkCons[0] = min;
		this.snkCons[1] = max;
		this.snkConsVar = var * (max - min) / 2.0;;
		return this;
	}

	public ApplicationGenerator setOPNodeResources(final int min, final int max) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0)
			throw new GeneratorException("Invalid arguments");
		this.opnRes[0] = min;
		this.opnRes[1] = max;
		this.opnResVar = 0.0;
		return this;
	}
	
	public ApplicationGenerator setOPNodeResources(final int min, final int max, final double var) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0 || var < 0.0)
			throw new GeneratorException("Invalid arguments");
		this.opnRes[0] = min;
		this.opnRes[1] = max;
		this.opnResVar = var * (max - min) / 2.0;;
		return this;
	}
	
	public ApplicationGenerator setOPNodeSpeed(final double min, final double max) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0)
			throw new GeneratorException("Invalid arguments");
		this.opnSpeed[0] = min;
		this.opnSpeed[1] = max;
		this.opnSpeedVar = 0.0;
		return this;
	}
	
	public ApplicationGenerator setOPNodeSpeed(final double min, final double max, final double var) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0 || var < 0.0)
			throw new GeneratorException("Invalid arguments");
		this.opnSpeed[0] = min;
		this.opnSpeed[1] = max;
		this.opnSpeedVar = var * (max - min) / 2.0;;
		return this;
	}
		
	
	/********************************************************************************
	 * Creation		
	 ********************************************************************************/
	public Application create() {
		Application app = new Application();	
		Set<OPNode> nodes = new HashSet<OPNode>();
		
		this.meta(app);
		
		nodes = this.nodes();	
		
		this.streams(app, nodes);
		
		this.pinnability(app);
		
		this.reset();
		
		return app;		
	}
	
	private void meta(Application app) {
		app.setName(this.name);
		app.setDescription(this.desc);
	}
	
	private Set<OPNode> nodes() {
		Set<OPNode> nodes = new HashSet<OPNode>(this.srcnodes + this.pipnodes + this.snknodes);
		int nxtid = 0;
		
		for (int srcnode = 1; srcnode <= this.srcnodes; srcnode++, nxtid++) {
			double prod = GMath.randomNormal(this.rnd, this.srcProd[0], this.srcProd[1], this.srcProdVar);
			int resources = GMath.randomNormalInt(this.rnd, this.opnRes[0], this.opnRes[1], this.opnResVar);
			double speed = GMath.randomNormal(this.rnd, this.opnSpeed[0], this.opnSpeed[1], this.opnSpeedVar);
			OPNode node = new OPNode(nxtid, OPRole.SRC, "src" + srcnode, x -> prod, resources, speed);			
			nodes.add(node);
		}
		
		for (int pipnode = 1; pipnode <= this.pipnodes; pipnode++, nxtid++) {
			double cons = GMath.randomNormal(this.rnd, this.pipCons[0], this.pipCons[1], this.pipConsVar);
			int resources = GMath.randomNormalInt(this.rnd, this.opnRes[0], this.opnRes[1], this.opnResVar);
			double speed = GMath.randomNormal(this.rnd, this.opnSpeed[0], this.opnSpeed[1], this.opnSpeedVar);
			OPNode node = new OPNode(nxtid, OPRole.PIP, "pip" + pipnode, x -> x * cons, resources, speed);
			nodes.add(node);
		}		
		
		for (int snknode = 1; snknode <= this.snknodes; snknode++, nxtid++) {	
			double cons = GMath.randomNormal(this.rnd, this.snkCons[0], this.snkCons[1], this.snkConsVar);
			int resources = GMath.randomNormalInt(this.rnd, this.opnRes[0], this.opnRes[1], this.opnResVar);
			double speed = GMath.randomNormal(this.rnd, this.opnSpeed[0], this.opnSpeed[1], this.opnSpeedVar);
			OPNode node = new OPNode(nxtid, OPRole.SNK, "snk" + snknode, x -> x * cons, resources, speed);
			nodes.add(node);
		}	
		
		return nodes;
	}
	
	private void streams(Application app, Set<OPNode> nodes) {
		Set<OPNode> srcs = new HashSet<OPNode>();
		Set<OPNode> pips = new HashSet<OPNode>();
		Set<OPNode> snks = new HashSet<OPNode>();		
		Set<OPNode> vstd = new HashSet<OPNode>();
		
		srcs.addAll(nodes.stream().filter(node -> node.isSource()).collect(Collectors.toSet()));
		pips.addAll(nodes.stream().filter(node -> node.isPipe()).collect(Collectors.toSet()));
		snks.addAll(nodes.stream().filter(node -> node.isSink()).collect(Collectors.toSet()));
		
		for (OPNode srcnode : srcs) {
			OPNode pipnode = (OPNode) GMath.randomElement(this.rnd, pips);
			if (app.addStream(srcnode, pipnode))
				vstd.add(pipnode);
		}
		
		while (!vstd.containsAll(pips)) {
			Set<OPNode> notsrcs = vstd.stream().filter(node -> app.outDegreeOf(node) == 0).collect(Collectors.toSet());
			OPNode pipSRC = (OPNode) GMath.randomElement(this.rnd, notsrcs);
			int outDegree = GMath.randomNormalInt(this.rnd, this.pipnodes * this.opnConn[0], 
															this.pipnodes * this.opnConn[1], 
															this.opnConnVar);
			List<OPNode> notvstd = pips.stream().filter(node -> !vstd.contains(node) && !pipSRC.equals(node)).collect(Collectors.toList());
			List<Object> connectables = GMath.randomElements(this.rnd, notvstd, outDegree);
			
			for (Object pipDST : connectables)
				if (app.addStream(pipSRC, (OPNode)pipDST))
					vstd.add((OPNode)pipDST);		
		}
		
		Set<OPNode> endpips = app.getPipes().stream().filter(node -> app.outDegreeOf(node) == 0).collect(Collectors.toSet());	
		for (OPNode pipnode : endpips) {
			OPNode snknode = (OPNode) GMath.randomElement(this.rnd, snks);
			if (app.addStream(pipnode, snknode))
				vstd.add(snknode);
		}					
		
		Set<OPNode> notrcdsnks = snks.stream().filter(node -> !vstd.contains(node)).collect(Collectors.toSet());
		for (OPNode snknode : notrcdsnks) {
			OPNode pipnode = (OPNode) GMath.randomElement(this.rnd, pips);
			if (app.addStream(pipnode, snknode))
				vstd.add(snknode);
		}
	}
	
	private void pinnability(Application app) {
		if (this.exnodes.isEmpty()) {
			for (OPNode opnode : app.vertexSet())
				opnode.setAlwaysPinnable(true);
			return;
		}
		
		Set<Integer> pinnableAsSrc = new HashSet<Integer>();
		Set<Integer> pinnableAsSnk = new HashSet<Integer>();
		Set<Integer> pinnableAsPip = new HashSet<Integer>();
		Set<Integer> pinnedAsSrc = new HashSet<Integer>();
		Set<Integer> pinnedAsSnk = new HashSet<Integer>();
		Set<Integer> pinnedAsPip = new HashSet<Integer>();
		
		pinnableAsSrc.addAll(this.exnodes);		
		for (OPNode srcnode : app.getSources()) {
			Integer exnodeid = (Integer) GMath.randomElement(this.rnd, pinnableAsSrc);
			if (srcnode.addPinnable(exnodeid)) {
				pinnedAsSrc.add(exnodeid);
				pinnableAsSrc.remove(exnodeid);
			}							
		}
		
		pinnableAsSnk.addAll(this.exnodes.stream().filter(nodeid -> !pinnedAsSrc.contains(nodeid)).collect(Collectors.toSet()));		
		for (OPNode snknode : app.getSinks()) {
			Integer exnodeid = (Integer) GMath.randomElement(this.rnd, pinnableAsSnk);
			if (snknode.addPinnable(exnodeid)) {
				pinnedAsSnk.add(exnodeid);
				pinnableAsSnk.remove(exnodeid);
			}							
		}
		
		pinnableAsPip.addAll(this.exnodes.stream().filter(nodeid -> !pinnedAsSrc.contains(nodeid) && !pinnedAsSnk.contains(nodeid)).collect(Collectors.toSet()));
		for (OPNode pipnode : app.getPipes()) {
			int pinnDegree = GMath.randomNormalInt(this.rnd, pinnableAsPip.size() * this.opnPinn[0], pinnableAsPip.size() * this.opnPinn[1], this.opnPinnVar);
			List<Object> exnodeids = GMath.randomElements(this.rnd, pinnableAsPip, pinnDegree);
			for (Object exnodeid : exnodeids) {
				if (pipnode.addPinnable((Integer)exnodeid))
					pinnedAsPip.add((Integer)exnodeid);
			}				
		}
		
			
	}
	
	/********************************************************************************
	 * Reset		
	 ********************************************************************************/
	private void reset() {
		this.rnd = new RandomDataGenerator();
		
		this.name = APP_NAME;
		this.desc = APP_DESC;
		
		this.srcnodes = OPN_SRC;
		this.pipnodes = OPN_PIP;
		this.snknodes = OPN_SNK;
		
		this.opnConn[0]  = OPN_CONN[0];	
		this.opnConn[1]  = OPN_CONN[1];	
		this.opnPinn[0]  = OPN_PINN[0];
		this.opnPinn[1]  = OPN_PINN[1];
		
		this.srcProd[0] = SRC_PROD[0];
		this.srcProd[1] = SRC_PROD[1];
		this.pipCons[0] = PIP_CONS[0];
		this.pipCons[1] = PIP_CONS[1];
		this.snkCons[0] = SNK_CONS[0];
		this.snkCons[1] = SNK_CONS[1];	
		
		this.opnRes[0]   = OPN_RES[0];
		this.opnRes[1]   = OPN_RES[1];
		this.opnSpeed[0] = OPN_SPEED[0];
		this.opnSpeed[1] = OPN_SPEED[1];		
		
		this.opnConnVar  = 0.0;
		this.opnPinnVar	 = 0.0;
		this.srcProdVar  = 0.0;
		this.pipConsVar  = 0.0;
		this.snkConsVar  = 0.0;
		this.opnResVar   = 0.0;
		this.opnSpeedVar = 0.0;		
		
		this.exnodes.clear();
	}

}
