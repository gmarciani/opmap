package model;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import model.application.Application;
import model.application.operator.Operational;
import model.application.operator.OperationalPath;
import model.application.operator.Role;

public class TestApplication {

	@Test 
	public void normal() {
		Application app = getSimpleApp();
		
		System.out.println(app);
		
	}
	
	@Test 
	public void cycled() {
		Application app = getCycledApp();
		
		System.out.println(app);
	}
	
	@Test 
	public void pathsFromSourceToSink() {
		Application app = getSimpleApp();
		
		Operational source = app.getVertices().stream().findAny().get();
		Operational sink = app.getVertices().stream().findAny().get();
		
		//Set<OperationalPath> paths = app.getOperationalPaths(source, sink);
		
		//System.out.println(paths);
	}
	
	@Test 
	public void allPaths() {
		Application app = getSimpleApp();
		
		Set<OperationalPath> paths = app.getAllOperationalPaths();
		
		System.out.println(paths);
	}
	
	private static Application getSimpleApp() {
		Application app = new Application("Simple DSP Application");
		
		Operational node1 = new Operational(1, Role.SRC, "gridsensor", x -> new Long(1000), 1, 1.0, new LinkedHashSet<Long>());
		Operational node2 = new Operational(2, Role.PIP, "selection1", x -> x/2, 1, 1.0, new LinkedHashSet<Long>());
		Operational node3 = new Operational(3, Role.PIP, "selection2", x -> x/2, 1, 1.0, new LinkedHashSet<Long>());
		Operational node4 = new Operational(4, Role.SNK, "datacenter", x -> new Long(1), 1, 1.0, new LinkedHashSet<Long>());
		
		app.addStream(node1, node2);
		app.addStream(node1, node3);
		app.addStream(node2, node3);
		app.addStream(node2, node4);
		app.addStream(node3, node4);
		
		return app;
	}
	
	private static Application getCycledApp() {
		Application app = new Application("Cycled DSP Application");
		
		Operational node1 = new Operational(1, Role.SRC, "gridsensor", x -> new Long(1000), 1, 1.0, new LinkedHashSet<Long>());
		Operational node2 = new Operational(2, Role.PIP, "selection1", x -> x/2, 1, 1.0, new LinkedHashSet<Long>());
		Operational node3 = new Operational(3, Role.PIP, "selection2", x -> x/2, 1, 1.0, new LinkedHashSet<Long>());
		Operational node4 = new Operational(4, Role.SNK, "datacenter", x -> new Long(1), 1, 1.0, new LinkedHashSet<Long>());
		
		app.addStream(node1, node2);
		app.addStream(node1, node3);
		app.addStream(node2, node3);
		app.addStream(node2, node4);
		app.addStream(node3, node4);
		
		app.addStream(node2, node1);
		
		return app;
	}
}
