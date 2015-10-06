package model.architecture;

import java.util.Random;
import java.util.stream.Collectors;

import commons.Randomizer;
import model.architecture.exnode.EXNode;
import model.architecture.link.Link;

public class ArchitectureGenerator {
	
	private static final String ARC_NAME 	 = "Sample Architecture";
	private static final String ARC_DESC 	 = "Randomly generated";
	
	private static final int 	EXNODES 	 = 10;	
	private static final double EXN_CONN[] 	 = {50.0, 100.0};
	
	private static final int 	EXN_RESOURCES[] 	= {2, 8};
	private static final double EXN_SPEEDUP[] 		= {5.0, 10.0};
	private static final double EXN_AVAILABILITY[] 	= {0.5, 1.0};
	
	private static final long 	LNK_DELAY[] 		= {30, 1000};
	private static final long 	LNK_BANDWIDTH[] 	= {1000, 10000};
	private static final double LNK_AVAILABILITY[] 	= {0.5, 1.0};
	
	
	protected Random rnd;
	
	private String 	name;
	private String 	desc;
	
	private int 	exnodes;	
	private double 	exnConn[];
	
	private int 	exnResources[];
	private double 	exnSpeedup[];
	private double 	exnAvailability[];
	
	private long 	lnkDelay[];
	private long 	lnkBandwidth[];
	private double 	lnkAvailability[];
	

	public ArchitectureGenerator() {
		this.exnConn = new double[2];
		this.exnResources = new int[2];
		this.exnSpeedup = new double[2];
		this.exnAvailability = new double[2];		
		this.lnkDelay= new long[2];
		this.lnkBandwidth = new long[2];
		this.lnkAvailability = new double[2];
		this.reset();
	}
	
	
	/********************************************************************************
	 * General		
	 ********************************************************************************/
	public ArchitectureGenerator setName(final String name) {
		if (name == null || name.isEmpty()) {
			this.name = ARC_NAME;
			return this;
		}
		this.name = name;
		return this;
	}
	
	public ArchitectureGenerator setDescription(final String desc) {
		if (desc == null || desc.isEmpty()) {
			this.desc = ARC_DESC;
			return this;
		}
		this.desc = desc;
		return this;
	}
	
	
	/********************************************************************************
	 * Topology		
	 ********************************************************************************/
	public ArchitectureGenerator setEXNodes(final int exnodes) {
		if (exnodes < 1) {
			this.exnodes = EXNODES;
			return this;
		}
		this.exnodes = exnodes;
		return this;
	}	
	
	public ArchitectureGenerator setEXNodeConnectivity(final double min, final double max) {
		if (min > max || min <= 0 || max <= 0) {
			this.exnConn[0] = EXN_CONN[0];
			this.exnConn[1] = EXN_CONN[1];
			return this;
		}	
		this.exnConn[0] = min;
		this.exnConn[1] = max;
		return this;
	}
	
	
	/********************************************************************************
	 * Computational: EXNodes
	 ********************************************************************************/
	public ArchitectureGenerator setEXNodeResources(final int min, final int max) {
		if (min > max || min <= 0 || max <= 0) {
			this.exnResources[0] = EXN_RESOURCES[0];
			this.exnResources[1] = EXN_RESOURCES[1];
			return this;
		}
		this.exnResources[0] = min;
		this.exnResources[1] = max;
		return this;
	}
	
	public ArchitectureGenerator setEXNodeSpeedup(final double min, final double max) {
		if (min > max || min <= 0 || max <= 0) {
			this.exnSpeedup[0] = EXN_SPEEDUP[0];
			this.exnSpeedup[1] = EXN_SPEEDUP[1];
			return this;
		}
		this.exnSpeedup[0] = min;
		this.exnSpeedup[1] = max;
		return this;
	}
	
	public ArchitectureGenerator setEXNodeAvailability(final double min, final double max) {
		if (min > max || min <= 0 || max <= 0) {
			this.exnAvailability[0] = EXN_AVAILABILITY[0];
			this.exnAvailability[1] = EXN_AVAILABILITY[1];
			return this;
		}
		this.exnAvailability[0] = min;
		this.exnAvailability[1] = max;
		return this;
	}
	
	
	/********************************************************************************
	 * Computational: Links
	 ********************************************************************************/
	public ArchitectureGenerator setLinkDelay(final long min, final long max) {
		if (min > max || min <= 0 || max <= 0) {
			this.lnkDelay[0] = LNK_DELAY[0];
			this.lnkDelay[1] = LNK_DELAY[1];
			return this;
		}
		this.lnkDelay[0] = min;
		this.lnkDelay[1] = max;
		return this;
	}
	
	public ArchitectureGenerator setLinkBandwidth(final long min, final long max) {
		if (min > max || min <= 0 || max <= 0) {
			this.lnkBandwidth[0] = LNK_BANDWIDTH[0];
			this.lnkBandwidth[1] = LNK_BANDWIDTH[1];
			return this;
		}
		this.lnkBandwidth[0] = min;
		this.lnkBandwidth[1] = max;
		return this;
	}	
	
	public ArchitectureGenerator setLinkAvailability(final double min, final double max) {
		if (min > max || min <= 0 || max <= 0) {
			this.lnkAvailability[0] = LNK_AVAILABILITY[0];
			this.lnkAvailability[1] = LNK_AVAILABILITY[1];
			return this;
		}
		this.lnkAvailability[0] = min;
		this.lnkAvailability[1] = max;
		return this;
	}	
	
	
	/********************************************************************************
	 * Creation		
	 ********************************************************************************/
	public Architecture create() {
		Architecture arc = new Architecture();
		
		arc.setName(this.name);
		arc.setDescription(this.desc);
		
		for (int i = 0; i < this.exnodes; i++) {
			int resources = Randomizer.rndInteger(this.rnd, this.exnResources[0], this.exnResources[1]);
			double speedup = Randomizer.rndDouble(this.rnd, this.exnSpeedup[0], this.exnSpeedup[1]);
			double availability = Randomizer.rndDouble(this.rnd, this.exnAvailability[0], this.exnAvailability[1]);
			EXNode node = new EXNode(i, "exnode" + i, resources, speedup, availability);
			arc.addVertex(node);
		}
		
		this.connect(arc);
		
		this.reset();
		
		return arc;
	}
	
	protected void connect(Architecture arc) {
		for (EXNode exnodeSRC : arc.vertexSet()) {
			arc.addEdge(exnodeSRC, exnodeSRC, new Link(exnodeSRC, exnodeSRC, 0, Long.MAX_VALUE, 1.0));
			for (EXNode exnodeDST : arc.vertexSet().stream().filter(node -> node.getId() != exnodeSRC.getId()).collect(Collectors.toList())) {
				long delay = Randomizer.rndLong(this.rnd, this.lnkDelay[0], this.lnkDelay[1]);
				long bandwidth = Randomizer.rndLong(this.rnd, this.lnkBandwidth[0], this.lnkBandwidth[1]);
				double availability = Randomizer.rndDouble(this.rnd, this.lnkAvailability[0], this.lnkAvailability[1]);
				arc.addEdge(exnodeSRC, exnodeDST, new Link(exnodeSRC, exnodeDST, delay, bandwidth, availability));
				arc.addEdge(exnodeDST, exnodeSRC, new Link(exnodeDST, exnodeSRC, delay, bandwidth, availability));					
			}				
		}
	}
	
	private void reset() {
		this.rnd = new Random();
		
		this.name = ARC_NAME;
		this.desc = ARC_DESC;
		
		this.exnodes = EXNODES;
		this.exnConn = EXN_CONN;
		
		this.exnResources[0] = EXN_RESOURCES[0];
		this.exnResources[1] = EXN_RESOURCES[1];
		this.exnSpeedup[0] = EXN_SPEEDUP[0];
		this.exnSpeedup[1] = EXN_SPEEDUP[1];
		this.exnAvailability[0] = EXN_AVAILABILITY[0];
		this.exnAvailability[1] = EXN_AVAILABILITY[1];
		
		this.lnkDelay[0] = LNK_DELAY[0];
		this.lnkDelay[1] = LNK_DELAY[1];
		this.lnkBandwidth[0] = LNK_BANDWIDTH[0];
		this.lnkBandwidth[1] = LNK_BANDWIDTH[1];
		this.lnkAvailability[0] = LNK_AVAILABILITY[0];
		this.lnkAvailability[1] = LNK_AVAILABILITY[1];
	}

}
