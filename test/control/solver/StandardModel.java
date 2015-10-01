package control.solver;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import control.exceptions.ModelException;
import control.exceptions.SolverException;
import control.plotter.Plotter;
import control.solver.MPSolver;
import control.solver.OPPSolver;
import model.application.Application;
import model.application.ApplicationFactory;
import model.architecture.Architecture;
import model.architecture.ArchitectureFactory;
import model.placement.Report;
import model.placement.optmodel.OPPModel;
import model.placement.optmodel.standard.OPPStandard;

public class StandardModel {
	
	@Rule public TestName name = new TestName();
	
	@Before
	public void testInfo() {
		System.out.println("\n/********************************************************************************");
		System.out.println(" * TEST: " + this.getClass().getSimpleName() + " " + name.getMethodName());
		System.out.println(" ********************************************************************************/\n");
	}
	
	@Test
	public void create() throws ModelException {
		ApplicationFactory appFactory = new ApplicationFactory();
		ArchitectureFactory arcFactory = new ArchitectureFactory();		
		int opnodes = 5;
		int exnodes = 5;
		
		Application app = appFactory.setName("Sample Application")
									.setDescription("Created randomly with opnodes=" + opnodes)
									.setNodes(opnodes)
									.create();
		Architecture arc = arcFactory.setName("Sample Architecture")
				 					 .setDescription("Created randomly with exnodes=" + exnodes)
				 					 .setNodes(exnodes)
				 					 .create();
		
		OPPModel model = new OPPStandard(app, arc);
		
		System.out.println(model);
	}

	@Test
	public void solveAndReport() throws ModelException, SolverException {
		ApplicationFactory appFactory = new ApplicationFactory();
		ArchitectureFactory arcFactory = new ArchitectureFactory();		
		int opnodes = 5;
		int exnodes = 5;
		
		Application app = appFactory.setName("Sample Application")
									.setDescription("Created randomly with opnodes=" + opnodes)
									.setNodes(opnodes)
									.create();
		Architecture arc = arcFactory.setName("Sample Architecture")
				 					 .setDescription("Created randomly with exnodes=" + exnodes)
				 					 .setNodes(exnodes)
				 					 .create();
		
		OPPModel model = new OPPStandard(app, arc);
		
		OPPSolver solver = new MPSolver();
		
		Report report = solver.solve(model);

		System.out.println(report);		
	}
	
	@Test
	public void solveAndPlotByEXNodes() throws ModelException, SolverException {
		ApplicationFactory appFactory = new ApplicationFactory();
		ArchitectureFactory arcFactory = new ArchitectureFactory();
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		int opmin = 2;
		int opmax = 10;
		int oppas = 2;
		int exmin = 10;
		int exmax = 30;
		int expas = 5;
		
		for (int opnodes = opmin; opnodes <= opmax; opnodes += oppas) {
			XYSeries series = new XYSeries(opnodes + " OPNodes");
			
			for (int exnodes = exmin; exnodes <= exmax; exnodes += expas) {
				
				System.out.println("# Solving: opnodes=" + opnodes + " and exnodes=" + exnodes);
				Application app = appFactory.setName("Sample Application")
											.setDescription("Created randomly with opnodes=" + opnodes)
											.setNodes(opnodes)
											.create();
				Architecture arc = arcFactory.setName("Sample Architecture")
	 					 					 .setDescription("Created randomly with exnodes=" + exnodes)
	 					 					 .setNodes(exnodes)
	 					 					 .create();
				
				OPPModel model = new OPPStandard(app, arc);
				
				OPPSolver solver = new MPSolver();
				
				Report report = solver.solve(model);
				
				if (report != null)
					series.add(exnodes, report.getTime());
				else
					series.add(exnodes, 0.0);
			}
			
			dataset.addSeries(series);
		}		
		
		JFreeChart plot = Plotter.create("Standard Model", "EXNodes", "Time (s)", dataset);		
		
		try {
			Plotter.save(plot, "./test/control/plotter/svg/" + plot.getTitle().getText() + ".svg");
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}
	}	

}
