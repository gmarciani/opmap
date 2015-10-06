package model.architecture;

import java.util.stream.Collectors;

import org.apache.commons.math3.random.RandomDataGenerator;

import model.architecture.exnode.EXNode;
import model.architecture.link.Link;

public class ArchitectureGenerator {
	
	private static final String ARC_NAME 	= "Sample Architecture";
	private static final String ARC_DESC 	= "Randomly generated";
	
	private static final int 	EXNODES 	= 10;	
	private static final double EXN_CONN[] 	= {50.0, 100.0};
	
	private static final int 	EXN_RES[] 	= {2, 8};
	private static final double EXN_SPEED[] = {5.0, 10.0};
	private static final double EXN_AVAIL[] = {0.5, 1.0};
	
	private static final long 	LNK_DELAY[]	= {30, 1000};
	private static final long 	LNK_BANDW[] = {1000, 10000};
	private static final double LNK_AVAIL[] = {0.5, 1.0};
	
	
	protected RandomDataGenerator rnd;
	
	private String 	name;
	private String 	desc;
	
	private int 	exnodes;	
	private double 	exnConn[];
	
	private int 	exnRes[];
	private double 	exnSpeed[];
	private double 	exnAvail[];
	
	private long 	lnkDelay[];
	private long 	lnkBandw[];
	private double 	lnkAvail[];
	

	public ArchitectureGenerator() {
		this.exnConn  = new double[2];
		this.exnRes   = new int[2];
		this.exnSpeed = new double[2];
		this.exnAvail = new double[2];		
		this.lnkDelay = new long[2];
		this.lnkBandw = new long[2];
		this.lnkAvail = new double[2];
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
			this.exnRes[0] = EXN_RES[0];
			this.exnRes[1] = EXN_RES[1];
			return this;
		}
		this.exnRes[0] = min;
		this.exnRes[1] = max;
		return this;
	}
	
	public ArchitectureGenerator setEXNodeSpeedup(final double min, final double max) {
		if (min > max || min <= 0 || max <= 0) {
			this.exnSpeed[0] = EXN_SPEED[0];
			this.exnSpeed[1] = EXN_SPEED[1];
			return this;
		}
		this.exnSpeed[0] = min;
		this.exnSpeed[1] = max;
		return this;
	}
	
	public ArchitectureGenerator setEXNodeAvailability(final double min, final double max) {
		if (min > max || min <= 0 || max <= 0) {
			this.exnAvail[0] = EXN_AVAIL[0];
			this.exnAvail[1] = EXN_AVAIL[1];
			return this;
		}
		this.exnAvail[0] = min;
		this.exnAvail[1] = max;
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
			this.lnkBandw[0] = LNK_BANDW[0];
			this.lnkBandw[1] = LNK_BANDW[1];
			return this;
		}
		this.lnkBandw[0] = min;
		this.lnkBandw[1] = max;
		return this;
	}	
	
	public ArchitectureGenerator setLinkAvailability(final double min, final double max) {
		if (min > max || min <= 0 || max <= 0) {
			this.lnkAvail[0] = LNK_AVAIL[0];
			this.lnkAvail[1] = LNK_AVAIL[1];
			return this;
		}
		this.lnkAvail[0] = min;
		this.lnkAvail[1] = max;
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
			int resources = this.rnd.nextInt(this.exnRes[0], this.exnRes[1]);
			double speedup = this.rnd.nextUniform(this.exnSpeed[0], this.exnSpeed[1]);
			double availability = this.rnd.nextUniform(this.exnAvail[0], this.exnAvail[1]);
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
				long delay = this.rnd.nextLong(this.lnkDelay[0], this.lnkDelay[1]);
				long bandwidth = this.rnd.nextLong(this.lnkBandw[0], this.lnkBandw[1]);
				double availability = this.rnd.nextUniform(this.lnkAvail[0], this.lnkAvail[1]);
				arc.addEdge(exnodeSRC, exnodeDST, new Link(exnodeSRC, exnodeDST, delay, bandwidth, availability));
				arc.addEdge(exnodeDST, exnodeSRC, new Link(exnodeDST, exnodeSRC, delay, bandwidth, availability));					
			}				
		}
	}
	
	private void reset() {
		this.rnd = new RandomDataGenerator();
		
		this.name = ARC_NAME;
		this.desc = ARC_DESC;
		
		this.exnodes = EXNODES;
		this.exnConn = EXN_CONN;
		
		this.exnRes[0] 	 = EXN_RES[0];
		this.exnRes[1] 	 = EXN_RES[1];
		this.exnSpeed[0] = EXN_SPEED[0];
		this.exnSpeed[1] = EXN_SPEED[1];
		this.exnAvail[0] = EXN_AVAIL[0];
		this.exnAvail[1] = EXN_AVAIL[1];
		
		this.lnkDelay[0] = LNK_DELAY[0];
		this.lnkDelay[1] = LNK_DELAY[1];
		this.lnkBandw[0] = LNK_BANDW[0];
		this.lnkBandw[1] = LNK_BANDW[1];
		this.lnkAvail[0] = LNK_AVAIL[0];
		this.lnkAvail[1] = LNK_AVAIL[1];
	}

}
