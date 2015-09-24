package model;

import org.junit.Test;

import model.architecture.Architecture;
import model.architecture.link.LogicalLink;
import model.architecture.node.Computational;

public class TestArchitecture {

	@Test 
	public void normal() {
		Architecture arc = new Architecture("Sample Grid Architecture");
		
		Computational node1 = new Computational(1, "sensor1", 1, 1, 1.0);
		Computational node2 = new Computational(2, "station2", 1, 1, 1.0);
		Computational node3 = new Computational(3, "station3", 1, 1, 1.0);
		Computational node4 = new Computational(4, "datacenter", 1, 1, 1.0);

		arc.addVertex(node1);
		arc.addVertex(node2);
		arc.addVertex(node3);
		arc.addVertex(node4);		
		
		arc.addEdge(new LogicalLink(1.0, 1.0, 1.0), node1, node2);
		arc.addEdge(new LogicalLink(1.0, 1.0, 1.0), node1, node3);
		arc.addEdge(new LogicalLink(1.0, 1.0, 1.0), node2, node4);
		arc.addEdge(new LogicalLink(1.0, 1.0, 1.0), node3, node4);
		
		System.out.println(arc);
	}
	
}
