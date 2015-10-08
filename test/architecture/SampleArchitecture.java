package architecture;

import static org.junit.Assert.fail;

import control.exceptions.GeneratorException;
import model.architecture.Architecture;
import model.architecture.ArchitectureGenerator;
import model.architecture.exnode.EXNode;
import model.architecture.link.Link;

public final class SampleArchitecture {

	private SampleArchitecture() {}
	
	/********************************************************************************
	 * Deterministic
	 ********************************************************************************/	
	public static Architecture deterministic() {
		Architecture arc = new Architecture("Sample Architecture", "Created manually");
		
		EXNode node0 = new EXNode(0, "gsensor1", 1, 1.5, 0.5);
		EXNode node1 = new EXNode(1, "dcenter1", 4, 4.0, 0.5);
		EXNode node2 = new EXNode(2, "dcenter2", 4, 4.0, 0.5);
		EXNode node3 = new EXNode(3, "station1", 2, 2.0, 0.5);

		if (!arc.addVertex(node0)) fail("Computational insert failure");
		if (!arc.addVertex(node1)) fail("Computational insert failure");
		if (!arc.addVertex(node2)) fail("Computational insert failure");
		if (!arc.addVertex(node3)) fail("Computational insert failure");
		
		if (!arc.addEdge(node0, node1, new Link(node0, node1, 30.0, 1000.0, 1.0))) fail("Logical Link insert failure");
		if (!arc.addEdge(node0, node2, new Link(node0, node2, 30.0, 1000.0, 1.0))) fail("Logical Link insert failure");
		if (!arc.addEdge(node1, node3, new Link(node1, node3, 30.0, 1000.0, 1.0))) fail("Logical Link insert failure");
		if (!arc.addEdge(node2, node3, new Link(node2, node3, 30.0, 1000.0, 1.0))) fail("Logical Link insert failure");
		
		return arc;
	}
	
	
	/********************************************************************************
	 * Random: Uniform distribution of settings
	 ********************************************************************************/	
	public static Architecture randomUniform() {
		ArchitectureGenerator arcGen = new ArchitectureGenerator();
		
		Architecture arc = null;
		try {
			arc = arcGen.setName("Random Architecture")
						.setDescription("Created randomly (uniform)")
		 				.setEXNodes(5)
		 				.setEXNodeResources(2, 4)
		 				.setEXNodeSpeedup(2.0, 8.0)
		 				.setEXNodeAvailability(0.6, 0.7)
		 				.setLinkDelay(30.0, 300.0)
		 				.setLinkBandwidth(1000000.0, 1000000000.0)
		 				.setLinkAvailability(0.7, 0.9)
		 				.create();
		} catch (GeneratorException exc) {
			exc.printStackTrace();
		}
		
		return arc;
	}
	
	/********************************************************************************
	 * Random: Normal distribution of settings (mean, variance)
	 ********************************************************************************/	
	public static Architecture randomNormal() {
		ArchitectureGenerator arcGen = new ArchitectureGenerator();
		
		Architecture arc = null;
		try {
			arc = arcGen.setName("Random Architecture")
						.setDescription("Created randomly (gaussian)")
					 	.setEXNodes(5)
					 	.setEXNodeResources(2, 4, 2.0)
					 	.setEXNodeSpeedup(2.0, 8.0, 2.0)
					 	.setEXNodeAvailability(0.6, 0.7, 2.0)
					 	.setLinkDelay(30.0, 3000.0, 2.0)
					 	.setLinkBandwidth(1000000.0, 1000000000.0, 2.0)
					 	.setLinkAvailability(0.7, 0.9, 2.0)
					 	.create();
		} catch (GeneratorException exc) {
			exc.printStackTrace();
		}
		
		return arc;
	}

}
