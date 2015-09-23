package model;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import model.application.Application;
import model.application.OperationalPath;
import model.application.operator.ComputationalDemand;
import model.application.operator.OperationalNode;
import model.application.operator.Operator;
import model.application.operator.OperatorType;
import model.application.operator.Transformation;
import model.application.operator.TransformationType;

public class TestApplication {

	@Test public void normal() {
		Application app = getSimpleApp();
		
		System.out.println(app);
		
	}
	
	@Test public void cycled() {
		Application app = getCycledApp();
		
		System.out.println(app);
	}
	
	@Test public void pathsFromSourceToSink() {
		Application app = getSimpleApp();
		
		OperationalNode source = app.getOperationals().getSources().first();
		OperationalNode sink = app.getOperationals().getSinks().first();
		
		Set<OperationalPath> paths = app.getOperationalPaths(source, sink);
		
		System.out.println(paths);
	}
	
	@Test public void allPaths() {
		Application app = getSimpleApp();
		
		Set<OperationalPath> paths = app.getAllOperationalPaths();
		
		System.out.println(paths);
	}
	
	private static Application getSimpleApp() {
		Application app = new Application("Simple DSP Application");
		
		OperationalNode node1 = new OperationalNode(1, new Operator(OperatorType.SRC, "gridsensor", new Transformation(TransformationType.EQUAL, 100)), new ComputationalDemand(1, 1.0), new LinkedHashSet<Long>());
		OperationalNode node2 = new OperationalNode(2, new Operator(OperatorType.PIP, "selection1", new Transformation(TransformationType.RATIO, 0.5)), new ComputationalDemand(1, 1.0), new LinkedHashSet<Long>());
		OperationalNode node3 = new OperationalNode(3, new Operator(OperatorType.PIP, "selection2", new Transformation(TransformationType.RATIO, 0.5)), new ComputationalDemand(1, 1.0), new LinkedHashSet<Long>());
		OperationalNode node4 = new OperationalNode(4, new Operator(OperatorType.SNK, "datacenter", new Transformation(TransformationType.EQUAL, 0.0)), new ComputationalDemand(1, 1.0), new LinkedHashSet<Long>());
		
		app.addOperational(node1);
		app.addOperational(node2);
		app.addOperational(node3);
		app.addOperational(node4);
		
		app.addStream(1, 2);
		app.addStream(1, 3);
		app.addStream(2, 4);
		app.addStream(3, 4);
		
		return app;
	}
	
	private static Application getCycledApp() {
		Application app = new Application("Cycled DSP Application");
		
		OperationalNode node1 = new OperationalNode(1, new Operator(OperatorType.SRC, "gridsensor", new Transformation(TransformationType.EQUAL, 100)), new ComputationalDemand(1, 1.0), new LinkedHashSet<Long>());
		OperationalNode node2 = new OperationalNode(2, new Operator(OperatorType.PIP, "selection1", new Transformation(TransformationType.RATIO, 0.5)), new ComputationalDemand(1, 1.0), new LinkedHashSet<Long>());
		OperationalNode node3 = new OperationalNode(3, new Operator(OperatorType.PIP, "selection2", new Transformation(TransformationType.RATIO, 0.5)), new ComputationalDemand(1, 1.0), new LinkedHashSet<Long>());
		OperationalNode node4 = new OperationalNode(4, new Operator(OperatorType.SNK, "datacenter", new Transformation(TransformationType.EQUAL, 0.0)), new ComputationalDemand(1, 1.0), new LinkedHashSet<Long>());
		
		app.addOperational(node1);
		app.addOperational(node2);
		app.addOperational(node3);
		app.addOperational(node4);	
		
		app.addStream(1, 2);
		app.addStream(1, 3);
		app.addStream(2, 4);
		app.addStream(3, 4);
		app.addStream(2, 1);
		
		return app;
	}
}
