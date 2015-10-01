package model.application;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import model.application.Application;
import model.application.operator.OPNode;
import model.application.operator.OPRole;

public class TestApplicationCreation {
	
	@Rule public TestName name = new TestName();
	
	@Before
	public void testInfo() {
		System.out.println("\n/********************************************************************************");
		System.out.println(" * TEST: " + this.getClass().getSimpleName() + " " + name.getMethodName());
		System.out.println(" ********************************************************************************/\n");
	}

	@Test 
	public void acyclic() {		
		Application app = new Application("Sample Acyclic Application", "Created manually");
		
		OPNode node1 = new OPNode(0, OPRole.SRC, "prod", x -> new Long(1000), 1, 1.0);
		OPNode node2 = new OPNode(1, OPRole.PIP, "selection1", x -> x/2, 1, 1.0);
		OPNode node3 = new OPNode(2, OPRole.PIP, "selection2", x -> x/2, 1, 1.0);
		OPNode node4 = new OPNode(3, OPRole.SNK, "cons1", x -> new Long(1), 1, 1.0);
		OPNode node5 = new OPNode(4, OPRole.SNK, "cons2", x -> new Long(1), 1, 1.0);
		
		if (!app.addStream(node1, node2)) fail("Fake cycle detected");
		if (!app.addStream(node1, node3)) fail("Fake cycle detected");
		if (!app.addStream(node2, node3)) fail("Fake cycle detected");
		if (!app.addStream(node2, node4)) fail("Fake cycle detected");
		if (!app.addStream(node3, node4)) fail("Fake cycle detected");
		if (!app.addStream(node3, node5)) fail("Fake cycle detected");
		
		System.out.println(app);
		
		System.out.println(app.toPrettyString());
	}
	
	@Test 
	public void cyclic() {		
		Application app = new Application("Sample Cyclic Application", "Created manually");
		
		OPNode node1 = new OPNode(0, OPRole.SRC, "prod", x -> new Long(1000), 1, 1.0);
		OPNode node2 = new OPNode(1, OPRole.PIP, "selection1", x -> x/2, 1, 1.0);
		OPNode node3 = new OPNode(2, OPRole.PIP, "selection2", x -> x/2, 1, 1.0);
		OPNode node4 = new OPNode(3, OPRole.SNK, "cons1", x -> new Long(1), 1, 1.0);
		OPNode node5 = new OPNode(4, OPRole.SNK, "cons2", x -> new Long(1), 1, 1.0);
		
		if (!app.addStream(node1, node2)) fail("Fake cycle detected");
		if (!app.addStream(node1, node3)) fail("Fake cycle detected");
		if (!app.addStream(node2, node3)) fail("Fake cycle detected");
		if (!app.addStream(node2, node4)) fail("Fake cycle detected");
		if (!app.addStream(node3, node4)) fail("Fake cycle detected");
		if (!app.addStream(node3, node5)) fail("Fake cycle detected");
		
		if (app.addStream(node3, node5))  fail("Real cycle not detected");
		
		System.out.println(app.toPrettyString());
	}
	
}
