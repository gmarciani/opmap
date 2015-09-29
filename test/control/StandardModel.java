package control;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Test;

import control.exceptions.ModelException;
import control.exceptions.SolverException;
import control.plotter.Plotter;
import control.solver.MPSolver;
import control.solver.OPPSolver;
import model.application.Application;
import model.application.ApplicationFactory;
import model.architecture.Architecture;
import model.architecture.ArchitectureFactory;
import model.optmodel.OPPModel;
import model.optmodel.mp.OPPStandard;
import model.report.Report;

public class StandardModel {
	
	ApplicationFactory appFactory = new ApplicationFactory();
	ArchitectureFactory arcFactory = new ArchitectureFactory();
	
	@Test
	public void create() throws ModelException {
		Application app = appFactory.setName("Sample DSP Application")
									.setDescription("StandardModel.create")
				 					.setNodes(5)
				 					.create();
		Architecture arc = arcFactory.setName("Sample Distributed Architecture")
									 .setDescription("StandardModel.create")
									 .setNodes(5)
									 .create();
		
		OPPModel model = new OPPStandard(app, arc);
		
		System.out.println(model);
	}

	@Test
	public void solveAndReport() throws ModelException, SolverException {
		Application app = appFactory.setName("Sample DSP Application")
									.setDescription("StandardModel.solveAndReport")
									.setNodes(5)
									.create();
		Architecture arc = arcFactory.setName("Sample Distributed Architecture")
				 					 .setDescription("StandardModel.solveAndReport")
				 					 .setNodes(5)
				 					 .create();
		
		OPPModel model = new OPPStandard(app, arc);
		
		OPPSolver solver = new MPSolver();
		
		Report report = solver.solve(model);

		System.out.println(report);		
	}
	
	@Test
	public void solveAndPlotByComputationals() throws ModelException, SolverException {
		XYSeriesCollection dataset = new XYSeriesCollection();
		int opmin = 10;
		int opmax = 50;
		int oppas = 10;
		int exmin = 10;
		int exmax = 100;
		int expas = 10;
		
		for (int opnodes = opmin; opnodes <= opmax; opnodes += oppas) {
			XYSeries series = new XYSeries("Operationals-" + opnodes);
			
			for (int exnodes = exmin; exnodes <= exmax; exnodes += expas) {
				Application app = appFactory.setName("Sample DSP Application")
											.setDescription("StandardModel.solveAndPlotByComputationals")
											.setNodes(opnodes)
											.create();
				Architecture arc = arcFactory.setName("Sample Distributed Architecture")
	 					 					 .setDescription("StandardModel.solveAndPlotByComputationals")
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
		
		JFreeChart plot = Plotter.create("Standard Model", "Computationals", "Time (ms)", dataset);		
		
		try {
			Plotter.save(plot, "./test/plot/svg/" + plot.getTitle() + ".svg");
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}
	}	

}
