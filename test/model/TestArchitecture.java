package model;

import org.junit.Test;

import model.architecture.Architecture;
import model.architecture.link.LogicalLink;
import model.architecture.node.ComputationalNode;
import model.architecture.node.ComputationalOffer;

public class TestArchitecture {

	@Test public void normal() {
		Architecture arc = new Architecture("Sample Grid Architecture");
		
		ComputationalNode node1 = new ComputationalNode(1, "sensor1", new ComputationalOffer(1, 1, 1.0));
		ComputationalNode node2 = new ComputationalNode(2, "station2", new ComputationalOffer(1, 1, 1.0));
		ComputationalNode node3 = new ComputationalNode(3, "station3", new ComputationalOffer(1, 1, 1.0));
		ComputationalNode node4 = new ComputationalNode(4, "datacenter", new ComputationalOffer(1, 1, 1.0));

		arc.addComputationalNode(node1);
		arc.addComputationalNode(node2);
		arc.addComputationalNode(node3);
		arc.addComputationalNode(node4);				
		
		arc.addLogicalLink(1, 2, new LogicalLink(1.0, 1.0, 1.0));
		arc.addLogicalLink(1, 3, new LogicalLink(1.0, 1.0, 1.0));
		arc.addLogicalLink(2, 4, new LogicalLink(1.0, 1.0, 1.0));
		arc.addLogicalLink(3, 4, new LogicalLink(1.0, 1.0, 1.0));
		
		System.out.println(arc);
	}

}
