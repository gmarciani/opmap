package control;

import java.util.Random;

import org.junit.Test;

import control.exceptions.ModelException;
import control.exceptions.SolverException;
import control.solver.MPSolver;
import control.solver.OPPSolver;
import model.application.Application;
import model.application.operator.Operational;
import model.application.operator.Role;
import model.architecture.Architecture;
import model.architecture.link.LogicalLink;
import model.architecture.node.Computational;
import model.optmodel.OPPModel;
import model.optmodel.mp.OPPStandard;
import model.report.Report;

public class SolveStandard {

	@Test
	public void test() throws ModelException, SolverException {
		Application app = createSampleApplication();
		Architecture arc = createSampleArchitecture();
		
		System.out.println(app);
		System.out.println(arc);
		
		OPPModel model = new OPPStandard(app, arc);
		
		System.out.println(model);
		
		OPPSolver solver = new MPSolver();
		
		Report report = solver.solve(model);

		System.out.println(report);
		
	}
	
	private Application createSampleApplication() throws ModelException {
		Application app = new Application("Sample DSP application");
		
		Operational node1 = new Operational(0, Role.SRC, "gridsensor",  x -> new Long(1000), 1, rndDouble(1.0, 100.0));
		Operational node2 = new Operational(1, Role.PIP, "selection1",  x -> x/2, 			 1,	rndDouble(1.0, 100.0));
		Operational node3 = new Operational(2, Role.PIP, "selection2",  x -> x/2, 			 1,	rndDouble(1.0, 100.0));
		Operational node4 = new Operational(3, Role.SNK, "datacenter1", x -> new Long(1), 	 1,	rndDouble(1.0, 100.0));
		Operational node5 = new Operational(4, Role.SNK, "datacenter2", x -> new Long(1), 	 1,	rndDouble(1.0, 100.0));
				
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

	private Architecture createSampleArchitecture() {
		Architecture arc = new Architecture("Sample Cloud application");
		
		Computational node0 = new Computational(0, "sensor1",  2, rndDouble(1.0, 10.0), rndDouble(0.5, 1.0));
		Computational node1 = new Computational(1, "sensor2",  2, rndDouble(1.0, 10.0), rndDouble(0.5, 1.0));
		Computational node2 = new Computational(2, "sensor3",  2, rndDouble(1.0, 10.0), rndDouble(0.5, 1.0));
		Computational node3 = new Computational(3, "station1", 4, rndDouble(1.0, 10.0), rndDouble(0.5, 1.0));
		Computational node4 = new Computational(4, "station2", 4, rndDouble(1.0, 10.0), rndDouble(0.5, 1.0));
		Computational node5 = new Computational(5, "station3", 4, rndDouble(1.0, 10.0), rndDouble(0.5, 1.0));
		Computational node6 = new Computational(6, "station4", 4, rndDouble(1.0, 10.0), rndDouble(0.5, 1.0));
		Computational node7 = new Computational(7, "dcenter1", 8, rndDouble(1.0, 10.0), rndDouble(0.5, 1.0));
		Computational node8 = new Computational(8, "work1",    2, rndDouble(1.0, 10.0), rndDouble(0.5, 1.0));
		Computational node9 = new Computational(9, "work2",    2, rndDouble(1.0, 10.0), rndDouble(0.5, 1.0));

		arc.addVertex(node0);
		arc.addVertex(node1);
		arc.addVertex(node2);
		arc.addVertex(node3);
		arc.addVertex(node4);
		arc.addVertex(node5);
		arc.addVertex(node6);
		arc.addVertex(node7);
		arc.addVertex(node8);
		arc.addVertex(node9);
		
		for (Computational exnodeSRC : arc.vertexSet()) {
			for (Computational exnodeDST : arc.vertexSet()) {
				if (exnodeSRC.getId() == exnodeDST.getId()) {
					arc.addEdge(exnodeSRC, exnodeDST, new LogicalLink(0.0, Double.MAX_VALUE, 1.0));
				} else {
					arc.addEdge(exnodeSRC, exnodeDST, new LogicalLink(rndDouble(1.0, 100.0), rndDouble(1.0, 1000.0), rndDouble(0.5, 1.0)));
					arc.addEdge(exnodeDST, exnodeSRC, new LogicalLink(rndDouble(1.0, 100.0), rndDouble(1.0, 1000.0), rndDouble(0.5, 1.0)));
				}
					
			}				
		}
		
		return arc;
	}
	
	private double rndDouble(double lb, double ub) {
		Random rnd = new Random();		
		
		double value = lb + (ub - lb) * rnd.nextDouble();
		
	    return value;
	}

}
