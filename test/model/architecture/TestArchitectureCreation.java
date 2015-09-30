package model.architecture;

import static org.junit.Assert.*;

import org.junit.Test;

import model.architecture.Architecture;
import model.architecture.ArchitectureFactory;
import model.architecture.link.LogicalLink;
import model.architecture.node.Computational;

public class TestArchitectureCreation {

	@Test 
	public void normal() {
		Architecture arc = new Architecture("Sample Distributed Architecture");
		
		Computational node0 = new Computational(0, "sensor1", 1, 1, 1.0);
		Computational node1 = new Computational(1, "station2", 1, 1, 1.0);
		Computational node2 = new Computational(2, "station3", 1, 1, 1.0);
		Computational node3 = new Computational(3, "datacenter", 1, 1, 1.0);

		if (!arc.addVertex(node0)) fail("Computational insert failure");
		if (!arc.addVertex(node1)) fail("Computational insert failure");
		if (!arc.addVertex(node2)) fail("Computational insert failure");
		if (!arc.addVertex(node3)) fail("Computational insert failure");
		
		if (!arc.addEdge(node0, node1, new LogicalLink(node0, node1, 1.0, 1.0, 1.0))) fail("Logical Link insert failure");
		if (!arc.addEdge(node0, node2, new LogicalLink(node0, node2, 1.0, 1.0, 1.0))) fail("Logical Link insert failure");
		if (!arc.addEdge(node1, node3, new LogicalLink(node1, node3, 1.0, 1.0, 1.0))) fail("Logical Link insert failure");
		if (!arc.addEdge(node2, node3, new LogicalLink(node2, node3, 1.0, 1.0, 1.0))) fail("Logical Link insert failure");
		
		System.out.println(arc);
	}
	
	@Test
	public void random() {
		ArchitectureFactory arcFactory = new ArchitectureFactory();
		
		Architecture arc = arcFactory.setName("Random Distributed Architecture")
									 .setDescription("Created by ArchitectureFactory")
				 					 .setNodes(5)
				 					 .create();
		
		System.out.println(arc);
	}
	
}
