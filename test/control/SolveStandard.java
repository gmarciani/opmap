package control;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Random;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
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
import view.LinePlot;

public class SolveStandard {

	@Test
	public void solve() throws ModelException, SolverException {
		Application app = createSampleApplication(5);
		Architecture arc = createSampleArchitecture(10);
		
		System.out.println(app);
		System.out.println(arc);
		
		OPPModel model = new OPPStandard(app, arc);
		
		System.out.println(model);
		
		OPPSolver solver = new MPSolver();
		
		Report report = solver.solve(model);

		System.out.println(report);		
	}
	
	@Test
	public void solveAndPlot() throws ModelException, SolverException {
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries standard = new XYSeries("Standard");
		 
		for (int nodes = 1; nodes <= 100; nodes+=10) {
			Application app = createSampleApplication(nodes);
			Architecture arc = createSampleArchitecture(nodes);
			
			OPPModel model = new OPPStandard(app, arc);
			
			OPPSolver solver = new MPSolver();
			
			Report report = solver.solve(model);
			
			if (report != null)
				standard.add(nodes, report.getTime());
			else
				standard.add(nodes, 0.0);		
		}
		
		dataset.addSeries(standard);
		
		LinePlot plot = new LinePlot("SolveAndPlot", "This is the sample plot subtitle", dataset);
		
		try {
			plot.save("./test/plot/svg/" + plot.getTitle() + ".svg", 500, 300);
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}
	}
	
	private Application createSampleApplication(int nodes) throws ModelException {
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

	private Architecture createSampleArchitecture(int nodes) {
		Architecture arc = new Architecture("Sample Cloud application");
		
		for (int i = 0; i < nodes; i++) {
			Computational node = new Computational(i, "comp" + i,  4, rndDouble(1.0, 10.0), rndDouble(0.5, 1.0));
			arc.addVertex(node);
		}
		
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
