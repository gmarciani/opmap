package architecture;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import model.architecture.Architecture;
import model.architecture.link.Link;
import model.architecture.node.EXNode;

public class TestArchitectureCreation {
	
	@Rule public TestName name = new TestName();
	
	@Before
	public void testInfo() {
		System.out.println("\n/********************************************************************************");
		System.out.println(" * TEST: " + this.getClass().getSimpleName() + " " + name.getMethodName());
		System.out.println(" ********************************************************************************/\n");
	}

	@Test 
	public void simple() {
		Architecture arc = new Architecture("Sample Architecture", "Created manually");
		
		EXNode node0 = new EXNode(0, "sensor1", 1, 1, 1.0);
		EXNode node1 = new EXNode(1, "station2", 1, 1, 1.0);
		EXNode node2 = new EXNode(2, "station3", 1, 1, 1.0);
		EXNode node3 = new EXNode(3, "datacenter", 1, 1, 1.0);

		if (!arc.addVertex(node0)) fail("Computational insert failure");
		if (!arc.addVertex(node1)) fail("Computational insert failure");
		if (!arc.addVertex(node2)) fail("Computational insert failure");
		if (!arc.addVertex(node3)) fail("Computational insert failure");
		
		if (!arc.addEdge(node0, node1, new Link(node0, node1, 1.0, 1.0, 1.0))) fail("Logical Link insert failure");
		if (!arc.addEdge(node0, node2, new Link(node0, node2, 1.0, 1.0, 1.0))) fail("Logical Link insert failure");
		if (!arc.addEdge(node1, node3, new Link(node1, node3, 1.0, 1.0, 1.0))) fail("Logical Link insert failure");
		if (!arc.addEdge(node2, node3, new Link(node2, node3, 1.0, 1.0, 1.0))) fail("Logical Link insert failure");
		
		System.out.println(arc);
		
		System.out.println(arc.toPrettyString());
	}
	
}
