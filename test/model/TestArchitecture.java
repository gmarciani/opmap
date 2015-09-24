package model;

import org.junit.Test;

import model.architecture.Architecture;
import model.architecture.link.LogicalLink;
import model.architecture.node.ComputationalNode;
import model.architecture.node.ComputationalOffer;

public class TestArchitecture {

	@Test 
	public void normal() {
		Architecture arc = new Architecture("Sample Grid Architecture");
		
		ComputationalNode node1 = new ComputationalNode(1, "sensor1", new ComputationalOffer(1, 1, 1.0));
		ComputationalNode node2 = new ComputationalNode(2, "station2", new ComputationalOffer(1, 1, 1.0));
		ComputationalNode node3 = new ComputationalNode(3, "station3", new ComputationalOffer(1, 1, 1.0));
		ComputationalNode node4 = new ComputationalNode(4, "datacenter", new ComputationalOffer(1, 1, 1.0));

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
