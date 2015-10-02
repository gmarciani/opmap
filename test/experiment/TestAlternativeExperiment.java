package experiment;

import static org.junit.Assert.*;

import java.io.IOException;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import commons.Plotter;
import control.exceptions.ModelException;
import control.exceptions.SolverException;
import control.solver.MPSolver;
import control.solver.OPPSolver;
import model.application.Application;
import model.architecture.Architecture;
import model.placement.Report;
import model.placement.optmodel.OPPModel;
import model.placement.optmodel.standard.OPPRestricted;
import sample.Default;
import sample.SampleApplication;
import sample.SampleArchitecture;

public class TestAlternativeExperiment {

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
		
		for (int opnodes = Default.OPMIN; opnodes <= Default.OPMAX; opnodes += Default.OPPAS) {
			XYSeries series = new XYSeries(opnodes + " OPNodes");			
			Application app = SampleApplication.getRandomSample(opnodes);
			
			for (int exnodes = Default.EXMIN; exnodes <= Default.EXMAX; exnodes += Default.EXPAS) {				
				System.out.println("#Solving Restricted Model (OPNodes)# opnodes=" + opnodes + "|exnodes=" + exnodes);				
				
				Architecture arc = SampleArchitecture.getRandomSample(exnodes);
				
				OPPModel model = new OPPRestricted(app, arc);
				
				OPPSolver solver = new MPSolver();
				
				Report report = solver.solve(model);
				
				if (report != null)
					series.add(exnodes, report.getTime());
				else
					series.add(exnodes, 0.0);
			}
			
			dataset.addSeries(series);
		}		
		
		JFreeChart plot = Plotter.create("Restricted Model (EXNodes)", "EXNodes", "Time (s)", dataset);		
		
		try {
			Plotter.save(plot, "./test/plotter/svg/" + plot.getTitle().getText() + ".svg");
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}
	}
	
	@Test
	public void compareEXNodes() throws ModelException, SolverException {		
		XYSeriesCollection dataset = new XYSeriesCollection();		
		
		for (int exnodes = Default.EXMIN; exnodes <= Default.EXMAX; exnodes += Default.EXPAS) {
			XYSeries series = new XYSeries(exnodes + " EXNodes");			
			Architecture arc = SampleArchitecture.getRandomSample(exnodes);
			
			for (int opnodes = Default.OPMIN; opnodes <= Default.OPMAX; opnodes += Default.OPPAS) {				
				System.out.println("#Solving Restricted Model (OPNodes)# opnodes=" + opnodes + "|exnodes=" + exnodes);
				
				Application app = SampleApplication.getRandomSample(opnodes);				
				
				OPPModel model = new OPPRestricted(app, arc);
				
				OPPSolver solver = new MPSolver();
				
				Report report = solver.solve(model);
				
				if (report != null)
					series.add(exnodes, report.getTime());
				else
					series.add(exnodes, 0.0);
			}
			
			dataset.addSeries(series);
		}		
		
		JFreeChart plot = Plotter.create("Restricted Model (OPNodes)", "OPNodes", "Time (s)", dataset);		
		
		try {
			Plotter.save(plot, "./test/plotter/svg/" + plot.getTitle().getText() + ".svg");
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}
	}

}
