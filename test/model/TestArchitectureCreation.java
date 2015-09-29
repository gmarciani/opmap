package model;

import org.junit.Test;

import model.architecture.Architecture;
import model.architecture.ArchitectureFactory;
import model.architecture.link.LogicalLink;
import model.architecture.node.Computational;

public class TestArchitectureCreation {

	@Test 
	public void normal() {
		Architecture arc = new Architecture("Sample Distributed Architecture");
		
		Computational node1 = new Computational(1, "sensor1", 1, 1, 1.0);
		Computational node2 = new Computational(2, "station2", 1, 1, 1.0);
		Computational node3 = new Computational(3, "station3", 1, 1, 1.0);
		Computational node4 = new Computational(4, "datacenter", 1, 1, 1.0);

		arc.addVertex(node1);
		arc.addVertex(node2);
		arc.addVertex(node3);
		arc.addVertex(node4);		
		
		arc.addEdge(node1, node2, new LogicalLink(1.0, 1.0, 1.0));
		arc.addEdge(node1, node3, new LogicalLink(1.0, 1.0, 1.0));
		arc.addEdge(node2, node4, new LogicalLink(1.0, 1.0, 1.0));
		arc.addEdge(node3, node4, new LogicalLink(1.0, 1.0, 1.0));
		
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
