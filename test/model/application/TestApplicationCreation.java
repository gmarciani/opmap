package model.application;

import static org.junit.Assert.fail;

import org.junit.Test;

import model.application.Application;
import model.application.operator.Operational;
import model.application.operator.Role;

public class TestApplicationCreation {

	@Test 
	public void acyclic() {
		Application app = new Application("Sample Acyclic DSP Application");
		
		Operational node1 = new Operational(0, Role.SRC, "gridsensor", x -> new Long(1000), 1, 1.0);
		Operational node2 = new Operational(1, Role.PIP, "selection1", x -> x/2, 1, 1.0);
		Operational node3 = new Operational(2, Role.PIP, "selection2", x -> x/2, 1, 1.0);
		Operational node4 = new Operational(3, Role.SNK, "datacenter1", x -> new Long(1), 1, 1.0);
		Operational node5 = new Operational(4, Role.SNK, "datacenter2", x -> new Long(1), 1, 1.0);
		
		if (!app.addStream(node1, node2)) fail("Fake cycle detected");
		if (!app.addStream(node1, node3)) fail("Fake cycle detected");
		if (!app.addStream(node2, node3)) fail("Fake cycle detected");
		if (!app.addStream(node2, node4)) fail("Fake cycle detected");
		if (!app.addStream(node3, node4)) fail("Fake cycle detected");
		if (!app.addStream(node3, node5)) fail("Fake cycle detected");
		
		System.out.println(app);		
	}
	
	@Test 
	public void cyclic() {
		Application app = new Application("Sample Cyclic DSP Application");
		
		Operational node1 = new Operational(0, Role.SRC, "gridsensor", x -> new Long(1000), 1, 1.0);
		Operational node2 = new Operational(1, Role.PIP, "selection1", x -> x/2, 1, 1.0);
		Operational node3 = new Operational(2, Role.PIP, "selection2", x -> x/2, 1, 1.0);
		Operational node4 = new Operational(3, Role.SNK, "datacenter1", x -> new Long(1), 1, 1.0);
		Operational node5 = new Operational(4, Role.SNK, "datacenter2", x -> new Long(1), 1, 1.0);
		
		if (!app.addStream(node1, node2)) fail("Fake cycle detected");
		if (!app.addStream(node1, node3)) fail("Fake cycle detected");
		if (!app.addStream(node2, node3)) fail("Fake cycle detected");
		if (!app.addStream(node2, node4)) fail("Fake cycle detected");
		if (!app.addStream(node3, node4)) fail("Fake cycle detected");
		if (!app.addStream(node3, node5)) fail("Fake cycle detected");
		
		if (app.addStream(node3, node5))  fail("Real cycle not detected");
	}
	
}
