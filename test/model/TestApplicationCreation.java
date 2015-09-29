package model;

import static org.junit.Assert.fail;

import org.junit.Test;

import control.exceptions.ModelException;
import model.application.Application;
import model.application.ApplicationFactory;
import model.application.operator.Operational;
import model.application.operator.Role;

public class TestApplicationCreation {

	@Test 
	public void normal() {
		Application app = null;
		try {
			app = getAcyclicApp();
		} catch (ModelException exc) {
			fail();
		}
		
		System.out.println(app);
		
	}
	
	@Test 
	public void cycled() {
		@SuppressWarnings("unused")
		Application app;
		try {
			app = getCyclicApp();
		} catch (ModelException exc) {
			return;
		}
		
		fail();
	}
	
	@Test
	public void random() {
		ApplicationFactory appFactory = new ApplicationFactory();
		
		Application app = appFactory.setName("Random DSP Application")
									.setDescription("Created by ApplicationFactory")
									.setNodes(5)
									.create();
		
		System.out.println(app);
	}
	
	private static Application getAcyclicApp() throws ModelException {
		Application app = new Application("Sample Acyclic DSP Application");
		
		Operational node1 = new Operational(0, Role.SRC, "gridsensor", x -> new Long(1000), 1, 1.0);
		Operational node2 = new Operational(1, Role.PIP, "selection1", x -> x/2, 1, 1.0);
		Operational node3 = new Operational(2, Role.PIP, "selection2", x -> x/2, 1, 1.0);
		Operational node4 = new Operational(3, Role.SNK, "datacenter1", x -> new Long(1), 1, 1.0);
		Operational node5 = new Operational(4, Role.SNK, "datacenter2", x -> new Long(1), 1, 1.0);
				
		app.addOperational(node1);
		app.addOperational(node2);
		app.addOperational(node3);
		app.addOperational(node4);
		app.addOperational(node5);
		
		app.addStream(node1, node2);
		app.addStream(node1, node3);
		app.addStream(node2, node3);
		app.addStream(node2, node4);
		app.addStream(node3, node4);
		app.addStream(node3, node5);
		
		return app;
	}
	
	private static Application getCyclicApp() throws ModelException {
		Application app = new Application("Sample Cyclic DSP Application");
		
		Operational node1 = new Operational(1, Role.SRC, "gridsensor", x -> new Long(1000), 1, 1.0);
		Operational node2 = new Operational(2, Role.PIP, "selection1", x -> x/2, 1, 1.0);
		Operational node3 = new Operational(3, Role.PIP, "selection2", x -> x/2, 1, 1.0);
		Operational node4 = new Operational(4, Role.SNK, "datacenter", x -> new Long(1), 1, 1.0);
		
		app.addOperational(node1);
		app.addOperational(node2);
		app.addOperational(node3);
		app.addOperational(node4);
		
		app.addStream(node1, node2);
		app.addStream(node1, node3);
		app.addStream(node2, node3);
		app.addStream(node2, node4);
		app.addStream(node3, node4);
		
		app.addStream(node2, node1);
		
		return app;
	}
}
