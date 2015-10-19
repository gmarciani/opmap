package model.architecture;

import java.util.stream.Collectors;

import org.apache.commons.math3.random.RandomDataGenerator;

import commons.GMath;
import control.exceptions.GeneratorException;
import model.architecture.exnode.EXNode;
import model.architecture.link.Link;

public class ArchitectureGenerator {
	
	private static final String ARC_NAME 	= "Sample Architecture";
	private static final String ARC_DESC 	= "Randomly generated";
	
	private static final int 	EXNODES 	= 10;	
	
	private static final int 	EXN_RES[] 	= {2, 8};
	private static final double EXN_SPEED[] = {5.0, 10.0};
	private static final double EXN_AVAIL[] = {0.5, 1.0};
	
	private static final long 	LNK_DELAY[]	= {30, 1000};
	private static final long 	LNK_BANDW[] = {1000, 10000};
	private static final double LNK_AVAIL[] = {0.5, 1.0};
	
	
	protected RandomDataGenerator rnd;
	
	private String name;
	private String desc;
	
	private int exnodes;	
	
	private int    exnRes[];
	private double exnResVar;
	
	private double exnSpeed[];
	private double exnSpeedVar;
	
	private double exnAvail[];
	private double exnAvailVar;
	
	private double lnkDelay[];
	private double lnkDelayVar;
	
	private double lnkBandw[];
	private double lnkBandwVar;
	
	private double lnkAvail[];
	private double lnkAvailVar;
	

	public ArchitectureGenerator() {
		this.exnRes   = new int[2];
		this.exnSpeed = new double[2];
		this.exnAvail = new double[2];		
		this.lnkDelay = new double[2];
		this.lnkBandw = new double[2];
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
	
	
	/********************************************************************************
	 * Computational Offer: EXNodes
	 ********************************************************************************/
	public ArchitectureGenerator setEXNodeResources(final int min, final int max) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0)
			throw new GeneratorException("Invalid arguments");
		this.exnRes[0] = min;
		this.exnRes[1] = max;
		this.exnResVar = 0.0;
		return this;
	}
	
	public ArchitectureGenerator setEXNodeResources(final int min, final int max, final double var) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0 || var < 0.0)
			throw new GeneratorException("Invalid arguments");
		this.exnRes[0] = min;
		this.exnRes[1] = max;
		this.exnResVar = var * (max - min) / 2.0;;
		return this;
	}	
	
	public ArchitectureGenerator setEXNodeSpeedup(final double min, final double max) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0) 
			throw new GeneratorException("Invalid arguments");
		this.exnSpeed[0] = min;
		this.exnSpeed[1] = max;
		this.exnSpeedVar = 0.0;
		return this;
	}
	
	public ArchitectureGenerator setEXNodeSpeedup(final double min, final double max, final double var) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0 || var < 0.0)
			throw new GeneratorException("Invalid arguments");
		this.exnSpeed[0] = min;
		this.exnSpeed[1] = max;
		this.exnSpeedVar = var * (max - min) / 2.0;;
		return this;
	}
	
	public ArchitectureGenerator setEXNodeAvailability(final double min, final double max) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0) 
			throw new GeneratorException("Invalid arguments");
		this.exnAvail[0] = min;
		this.exnAvail[1] = max;
		this.exnAvailVar = 0.0;
		return this;
	}
	
	public ArchitectureGenerator setEXNodeAvailability(final double min, final double max, final double var) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0 || var < 0.0)
			throw new GeneratorException("Invalid arguments");
		this.exnAvail[0] = min;
		this.exnAvail[1] = max;
		this.exnAvailVar = var * (max - min) / 2.0;;
		return this;
	}
	
	
	/********************************************************************************
	 * Computational Offer: Links
	 ********************************************************************************/
	public ArchitectureGenerator setLinkDelay(final double min, final double max) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0) 
			throw new GeneratorException("Invalid arguments");
		this.lnkDelay[0] = min;
		this.lnkDelay[1] = max;
		this.lnkDelayVar = 0.0;
		return this;
	}
	
	public ArchitectureGenerator setLinkDelay(final double min, final double max, final double var) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0 || var < 0.0)
			throw new GeneratorException("Invalid arguments");
		this.lnkDelay[0] = min;
		this.lnkDelay[1] = max;
		this.lnkDelayVar = var * (max - min) / 2.0;;
		return this;
	}
	
	public ArchitectureGenerator setLinkBandwidth(final double min, final double max) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0) 
			throw new GeneratorException("Invalid arguments");
		this.lnkBandw[0] = min;
		this.lnkBandw[1] = max;
		this.lnkBandwVar = 0.0;
		return this;
	}	
	
	public ArchitectureGenerator setLinkBandwidth(final double min, final double max, final double var) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0 || var < 0.0)
			throw new GeneratorException("Invalid arguments");
		this.lnkBandw[0] = min;
		this.lnkBandw[1] = max;
		this.lnkBandwVar = var * (max - min) / 2.0;;
		return this;
	}
	
	public ArchitectureGenerator setLinkAvailability(final double min, final double max) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0) 
			throw new GeneratorException("Invalid arguments");
		this.lnkAvail[0] = min;
		this.lnkAvail[1] = max;
		this.lnkAvailVar = 0.0;
		return this;
	}
	
	public ArchitectureGenerator setLinkAvailability(final double min, final double max, final double var) throws GeneratorException {
		if (min > max || min <= 0 || max <= 0 || var < 0.0)
			throw new GeneratorException("Invalid arguments");
		this.lnkAvail[0] = min;
		this.lnkAvail[1] = max;
		this.lnkAvailVar = var * (max - min) / 2.0;;
		return this;
	}
	
	
	/********************************************************************************
	 * Creation		
	 ********************************************************************************/
	public Architecture create() {
		Architecture arc = new Architecture();
		
		this.meta(arc);
		
		this.nodes(arc);		
		
		this.connect(arc);
		
		this.reset();
		
		return arc;
	}
	
	protected void meta(Architecture arc) {
		arc.setName(this.name);
		arc.setDescription(this.desc);
	}
	
	protected void nodes(Architecture arc) {
		for (int i = 0; i < this.exnodes; i++) {
			int resources = GMath.randomNormalInt(this.rnd, this.exnRes[0], this.exnRes[1], this.exnResVar);
			double speedup = GMath.randomNormal(this.rnd, this.exnSpeed[0], this.exnSpeed[1], this.exnSpeedVar);
			double availability = GMath.randomNormal(this.rnd, this.exnAvail[0], this.exnAvail[1], this.exnAvailVar);
			EXNode node = new EXNode(i, "exnode" + i, resources, speedup, availability);
			arc.addVertex(node);
		}
	}
	
	protected void connect(Architecture arc) {
		for (EXNode exnodeSRC : arc.vertexSet()) {
			arc.addEdge(exnodeSRC, exnodeSRC, new Link(exnodeSRC, exnodeSRC, 0, Long.MAX_VALUE, 1.0));
			for (EXNode exnodeDST : arc.vertexSet().stream().filter(node -> node.getId() != exnodeSRC.getId()).collect(Collectors.toList())) {
				double delay = GMath.randomNormal(this.rnd, this.lnkDelay[0], this.lnkDelay[1], this.lnkDelayVar);
				double bandwidth = GMath.randomNormal(this.rnd, this.lnkBandw[0], this.lnkBandw[1], this.lnkBandwVar);
				double availability = GMath.randomNormal(this.rnd, this.lnkAvail[0], this.lnkAvail[1], this.lnkAvailVar);
				arc.addEdge(exnodeSRC, exnodeDST, new Link(exnodeSRC, exnodeDST, delay, bandwidth, availability));
				arc.addEdge(exnodeDST, exnodeSRC, new Link(exnodeDST, exnodeSRC, delay, bandwidth, availability));					
			}				
		}
	}
	
	
	/********************************************************************************
	 * Reset		
	 ********************************************************************************/
	private void reset() {
		this.rnd = new RandomDataGenerator();
		
		this.name = ARC_NAME;
		this.desc = ARC_DESC;
		
		this.exnodes = EXNODES;
		
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
		
		this.exnResVar 	 = 0.0;
		this.exnSpeedVar = 0.0;
		this.exnAvailVar = 0.0;
		this.lnkDelayVar = 0.0;
		this.lnkBandwVar = 0.0;
		this.lnkAvailVar = 0.0;
	}

}
