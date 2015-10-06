package sample;

import static org.junit.Assert.fail;

import model.architecture.Architecture;
import model.architecture.ArchitectureGenerator;
import model.architecture.exnode.EXNode;
import model.architecture.link.Link;

public final class SampleArchitecture {

	private SampleArchitecture() {}
	
	public static Architecture getDeterministicSample() {
		Architecture arc = new Architecture("Sample Architecture", "Created manually");
		
		EXNode node0 = new EXNode(0, "gsensor1", Default.EXNODE_RESOURCES, Default.EXNODE_SPEEDUP, Default.EXNODE_AVAILABILITY);
		EXNode node1 = new EXNode(1, "station2", Default.EXNODE_RESOURCES, Default.EXNODE_SPEEDUP, Default.EXNODE_AVAILABILITY);
		EXNode node2 = new EXNode(2, "station3", Default.EXNODE_RESOURCES, Default.EXNODE_SPEEDUP, Default.EXNODE_AVAILABILITY);
		EXNode node3 = new EXNode(3, "dcenter1", Default.EXNODE_RESOURCES, Default.EXNODE_SPEEDUP, Default.EXNODE_AVAILABILITY);

		if (!arc.addVertex(node0)) fail("Computational insert failure");
		if (!arc.addVertex(node1)) fail("Computational insert failure");
		if (!arc.addVertex(node2)) fail("Computational insert failure");
		if (!arc.addVertex(node3)) fail("Computational insert failure");
		
		if (!arc.addEdge(node0, node1, new Link(node0, node1, 30, 1000, 1.0))) fail("Logical Link insert failure");
		if (!arc.addEdge(node0, node2, new Link(node0, node2, 30, 1000, 1.0))) fail("Logical Link insert failure");
		if (!arc.addEdge(node1, node3, new Link(node1, node3, 30, 1000, 1.0))) fail("Logical Link insert failure");
		if (!arc.addEdge(node2, node3, new Link(node2, node3, 30, 1000, 1.0))) fail("Logical Link insert failure");
		
		return arc;
	}
	
	public static final Architecture getRandomSample() {
		return getRandomSample(Default.EXNODE_RDN);
	}
	
	public static Architecture getRandomSample(final int exnodes) {
		ArchitectureGenerator arcGen = new ArchitectureGenerator();
		
		Architecture arc = arcGen.setName("Random Architecture")
								 .setDescription("Created randomly with exnodes=" + exnodes)
				 				 .setEXNodes(exnodes)
				 				 .setEXNodeConnectivity(80.0, 100.0)
				 				 .setEXNodeResources(2, 4)
				 				 .setEXNodeSpeedup(2.0, 8.0)
				 				 .setEXNodeAvailability(0.6, 0.7)
				 				 .setLinkDelay(30, 3000)
				 				 .setLinkBandwidth(1000000, 1000000000)
				 				 .setLinkAvailability(0.7, 0.9)
				 				 .create();
		
		return arc;
	}

}
