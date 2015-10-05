package experiments;

import static org.junit.Assert.*;

import java.io.IOException;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import commons.GMath;
import commons.Plotter;
import control.exceptions.ModelException;
import control.exceptions.SolverException;
import control.solver.OPPSolver;
import control.solver.mp.MPSolver;
import model.application.Application;
import model.architecture.Architecture;
import model.placement.Report;
import model.placement.optmodel.OPPModel;
import model.placement.optmodel.cplex.OPPAlternative;
import sample.Default;
import sample.SampleApplication;
import sample.SampleArchitecture;

public class TestExperimentAlternative {

	@Rule public TestName name = new TestName();
	
	@Before
	public void testInfo() {
		System.out.println("\n/********************************************************************************");
		System.out.println(" * TEST: " + this.getClass().getSimpleName() + " " + name.getMethodName());
		System.out.println(" ********************************************************************************/\n");
	}

	@Test
	public void compareOPNodes() throws ModelException, SolverException {	
		XYSeriesCollection dataset = new XYSeriesCollection();	
		
		int compareOpnodes[] = Default.Experiments.ModelResolution.CMP_OPNODES;		
		int exmin = Default.Experiments.ModelResolution.EXMIN;
		int exmax = Default.Experiments.ModelResolution.EXMAX;
		int expas = Default.Experiments.ModelResolution.EXPAS;		
		int repts = Default.Experiments.ModelResolution.REPETITIONS;
		
		long values[] = new long[repts];
		
		for (int opnodes : compareOpnodes) {
			XYSeries series = new XYSeries(opnodes + " OPNodes");			
			Application app = SampleApplication.getRandomSample(opnodes);	
			
			for (int exnodes = exmin; exnodes <= exmax; exnodes += expas) {											
				Architecture arc = SampleArchitecture.getRandomSample(exnodes);
				
				boolean solvable = true;
				
				for (int rept = 1; rept <= repts; rept++) {
					System.out.println("#Solving with Alternative Model (OPNodes=" + opnodes + ")# exnodes=" + exnodes + "|repetition:" + rept);
					
					OPPModel model = new OPPAlternative(app, arc);
					
					OPPSolver solver = new MPSolver();
					
					Report report = solver.solve(model);
					
					model.getCPlex().end();
					
					if (report != null) {
						long elapsed = report.getElapsedSeconds();
						values[rept - 1] = elapsed;
						System.out.println("solved in " + elapsed + " seconds");
					} else {
						solvable = false;
						System.out.println("unsolvable");
						break;
					}	
				}	
				
				if (solvable)
					series.add(exnodes, GMath.getAverage(values));
				else
					break;
			}
			
			dataset.addSeries(series);
		}		
		
		JFreeChart plot = Plotter.create("Alternative Model (Comparing OPNodes)", "EXNodes", "Time (s)", dataset);		
		
		try {
			Plotter.save(plot, Default.Experiments.RESULTS_DIR + plot.getTitle().getText() + ".svg");
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}
	}
	
	@Test
	public void compareEXNodes() throws ModelException, SolverException {		
		XYSeriesCollection dataset = new XYSeriesCollection();		
				
		int compareExnodes[] = Default.Experiments.ModelResolution.CMP_EXNODES;		
		int opmin = Default.Experiments.ModelResolution.OPMIN;
		int opmax = Default.Experiments.ModelResolution.OPMAX;
		int oppas = Default.Experiments.ModelResolution.OPPAS;
		
		int repts = Default.Experiments.ModelResolution.REPETITIONS;
		
		long values[] = new long[repts];
		
		for (int exnodes : compareExnodes) {
			XYSeries series = new XYSeries(exnodes + " EXNodes");			
			Architecture arc = SampleArchitecture.getRandomSample(exnodes);
			
			for (int opnodes = opmin; opnodes <= opmax; opnodes += oppas) {								
				Application app = SampleApplication.getRandomSample(opnodes);
				
				boolean solvable = true;
				
				for (int rept = 1; rept <= repts; rept++) {
					System.out.println("#Solving with Alternative Model (EXNodes=" + exnodes + ")# opnodes=" + opnodes + "|repetition:" + rept);
					
					OPPModel model = new OPPAlternative(app, arc);
					
					OPPSolver solver = new MPSolver();
					
					Report report = solver.solve(model);
					
					model.getCPlex().end();
					
					if (report != null) {
						long elapsed = report.getElapsedSeconds();
						values[rept - 1] = elapsed;
						System.out.println("solved in " + elapsed + " seconds");
					} else {
						solvable = false;
						System.out.println("unsolvable");
						break;
					}	
				}	
				
				if (solvable)
					series.add(exnodes, GMath.getAverage(values));
				else
					break;
			}
			
			dataset.addSeries(series);
		}		
		
		JFreeChart plot = Plotter.create("Alternative Model (Comparing EXNodes)", "OPNodes", "Time (s)", dataset);		
		
		try {
			Plotter.save(plot, Default.Experiments.RESULTS_DIR + plot.getTitle().getText() + ".svg");
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}
	}

}
