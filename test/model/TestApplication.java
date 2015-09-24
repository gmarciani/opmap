package model;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import model.application.Application;
import model.application.OperationalPath;
import model.application.operator.ComputationalDemand;
import model.application.operator.OperationalNode;
import model.application.operator.Operator;
import model.application.operator.OperatorRole;

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
		
		OperationalNode source = app.getVertices().stream().findAny().get();
		OperationalNode sink = app.getVertices().stream().findAny().get();
		
		Set<OperationalPath> paths = app.getOperationalPaths(source, sink);
		
		System.out.println(paths);
	}
	
	@Test 
	public void allPaths() {
		Application app = getSimpleApp();
		
		Set<OperationalPath> paths = app.getAllOperationalPaths();
		
		System.out.println(paths);
	}
	
	private static Application getSimpleApp() {
		Application app = new Application("Simple DSP Application");
		
		OperationalNode node1 = new OperationalNode(1, new Operator(OperatorRole.SRC, "gridsensor", x -> new Long(1000)), new ComputationalDemand(1, 1.0), new LinkedHashSet<Long>());
		OperationalNode node2 = new OperationalNode(2, new Operator(OperatorRole.PIP, "selection1", x -> x/2), new ComputationalDemand(1, 1.0), new LinkedHashSet<Long>());
		OperationalNode node3 = new OperationalNode(3, new Operator(OperatorRole.PIP, "selection2", x -> x/2), new ComputationalDemand(1, 1.0), new LinkedHashSet<Long>());
		OperationalNode node4 = new OperationalNode(4, new Operator(OperatorRole.SNK, "datacenter", x -> new Long(1)), new ComputationalDemand(1, 1.0), new LinkedHashSet<Long>());
		
		app.addStream(node1, node2);
		app.addStream(node1, node3);
		app.addStream(node2, node4);
		app.addStream(node3, node4);
		
		return app;
	}
	
	private static Application getCycledApp() {
		Application app = new Application("Cycled DSP Application");
		
		OperationalNode node1 = new OperationalNode(1, new Operator(OperatorRole.SRC, "gridsensor", x -> new Long(1000)), new ComputationalDemand(1, 1.0), new LinkedHashSet<Long>());
		OperationalNode node2 = new OperationalNode(2, new Operator(OperatorRole.PIP, "selection1", x -> x/2), new ComputationalDemand(1, 1.0), new LinkedHashSet<Long>());
		OperationalNode node3 = new OperationalNode(3, new Operator(OperatorRole.PIP, "selection2", x -> x/2), new ComputationalDemand(1, 1.0), new LinkedHashSet<Long>());
		OperationalNode node4 = new OperationalNode(4, new Operator(OperatorRole.SNK, "datacenter", x -> new Long(1)), new ComputationalDemand(1, 1.0), new LinkedHashSet<Long>());
		
		app.addStream(node1, node2);
		app.addStream(node1, node3);
		app.addStream(node2, node4);
		app.addStream(node3, node4);
		
		app.addStream(node2, node1);
		
		return app;
	}
}
