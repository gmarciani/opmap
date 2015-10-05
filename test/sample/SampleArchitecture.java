package sample;

import static org.junit.Assert.fail;

import model.architecture.Architecture;
import model.architecture.ArchitectureFactory;
import model.architecture.exnode.EXNode;
import model.architecture.link.Link;

public final class SampleArchitecture {

	private SampleArchitecture() {}
	
	public static Architecture getSample() {
		Architecture arc = new Architecture("Sample Architecture", "Created manually");
		
		EXNode node0 = new EXNode(0, "gsensor1", Default.EXNODE_RESOURCES, Default.EXNODE_SPEEDUP, Default.EXNODE_AVAILABILITY);
		EXNode node1 = new EXNode(1, "station2", Default.EXNODE_RESOURCES, Default.EXNODE_SPEEDUP, Default.EXNODE_AVAILABILITY);
		EXNode node2 = new EXNode(2, "station3", Default.EXNODE_RESOURCES, Default.EXNODE_SPEEDUP, Default.EXNODE_AVAILABILITY);
		EXNode node3 = new EXNode(3, "dcenter1", Default.EXNODE_RESOURCES, Default.EXNODE_SPEEDUP, Default.EXNODE_AVAILABILITY);

		if (!arc.addVertex(node0)) fail("Computational insert failure");
		if (!arc.addVertex(node1)) fail("Computational insert failure");
		if (!arc.addVertex(node2)) fail("Computational insert failure");
		if (!arc.addVertex(node3)) fail("Computational insert failure");
		
		if (!arc.addEdge(node0, node1, new Link(node0, node1, 1.0, 1.0, 1.0))) fail("Logical Link insert failure");
		if (!arc.addEdge(node0, node2, new Link(node0, node2, 1.0, 1.0, 1.0))) fail("Logical Link insert failure");
		if (!arc.addEdge(node1, node3, new Link(node1, node3, 1.0, 1.0, 1.0))) fail("Logical Link insert failure");
		if (!arc.addEdge(node2, node3, new Link(node2, node3, 1.0, 1.0, 1.0))) fail("Logical Link insert failure");
		
		return arc;
	}
	
	public static final Architecture getRandomSample() {
		return getRandomSample(Default.EXNODE_RDN);
	}
	
	public static Architecture getRandomSample(final int exnodes) {
		ArchitectureFactory arcFactory = new ArchitectureFactory();
		
		Architecture arc = arcFactory.setName("Random Architecture")
									 .setDescription("Created randomly with exnodes=" + exnodes)
				 					 .setNodes(exnodes)
				 					 .create();
		
		return arc;
	}

}
