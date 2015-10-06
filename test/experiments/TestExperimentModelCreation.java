package experiments;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Clock;
import java.time.Instant;

import org.apache.commons.math3.stat.StatUtils;
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
import model.application.Application;
import model.architecture.Architecture;
import model.placement.optmodel.OPPModel;
import sample.Default;
import sample.SampleApplication;
import sample.SampleArchitecture;

public class TestExperimentModelCreation {

	@Rule public TestName name = new TestName();
	
	@Before
	public void testInfo() {
		System.out.println("\n/********************************************************************************");
		System.out.println(" * TEST: " + this.getClass().getSimpleName() + " " + name.getMethodName());
		System.out.println(" ********************************************************************************/\n");
	}
	
	@Test
	public void compareModelsWithFixedOPNodes() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {	
		Clock clk = Clock.systemDefaultZone();
		XYSeriesCollection dataset = new XYSeriesCollection();		
		
		Class<?> compareModels[] = Default.Experiments.ModelCreation.CMP_MODELS;	
		int opnodes = Default.Experiments.ModelCreation.FIXED_OPNODES;
		int exmin 	= Default.Experiments.ModelCreation.EXMIN;
		int exmax 	= Default.Experiments.ModelCreation.EXMAX;
		int expas 	= Default.Experiments.ModelCreation.EXPAS;		
		int repts 	= Default.Experiments.ModelCreation.REPETITIONS;
		
		double values[] = new double[repts];
		
		for (Class<?> optmodel : compareModels) {
			XYSeries series = new XYSeries(optmodel.getSimpleName());			
			Application app = SampleApplication.getRandomSample(opnodes);	
			
			for (int exnodes = exmin; exnodes <= exmax; exnodes += expas) {											
				Architecture arc = SampleArchitecture.getRandomSample(exnodes);				
				
				for (int rept = 1; rept <= repts; rept++) {
					System.out.println("#Compiling " + optmodel.getName() + " (OPNodes=" + opnodes + ")# exnodes=" + exnodes + "|repetition:" + rept);
					Constructor<?> modelConstructor = optmodel.getConstructor(Application.class, Architecture.class);
					Instant start = clk.instant();	
					OPPModel mdl = (OPPModel) modelConstructor.newInstance(app, arc);
					Instant end = clk.instant();
					mdl.getCPlex().end();
					values[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
				}	
				
				series.add(exnodes, StatUtils.mean(values));
			}
			
			dataset.addSeries(series);
		}		
		
		JFreeChart plot = Plotter.create("Model Creation (" + opnodes + " OPNodes)", "EXNodes", "Time (ms)", dataset);		
		
		try {
			Plotter.save(plot, Default.Experiments.RESULTS_DIR + plot.getTitle().getText() + ".svg");
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}
	}
	
	@Test
	public void compareModelsWithFixedEXNodes() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {	
		Clock clk = Clock.systemDefaultZone();
		XYSeriesCollection dataset = new XYSeriesCollection();		
		
		Class<?> compareModels[] = Default.Experiments.ModelCreation.CMP_MODELS;		
		int exnodes = Default.Experiments.ModelCreation.FIXED_EXNODES;
		int opmin 	= Default.Experiments.ModelCreation.OPMIN;
		int opmax 	= Default.Experiments.ModelCreation.OPMAX;
		int oppas 	= Default.Experiments.ModelCreation.OPPAS;		
		int repts 	= Default.Experiments.ModelCreation.REPETITIONS;
		
		double values[] = new double[repts];
		
		for (Class<?> optmodel : compareModels) {
			XYSeries series = new XYSeries(optmodel.getSimpleName());
			Architecture arc = SampleArchitecture.getRandomSample(exnodes);	
			
			for (int opnodes = opmin; opnodes <= opmax; opnodes += oppas) {											
				Application app = SampleApplication.getRandomSample(opnodes);				
				
				for (int rept = 1; rept <= repts; rept++) {
					System.out.println("#Compiling " + optmodel.getName() + " (EXNodes=" + exnodes + ")# opnodes=" + opnodes + "|repetition:" + rept);
					Constructor<?> modelConstructor = optmodel.getConstructor(Application.class, Architecture.class);
					Instant start = clk.instant();	
					OPPModel mdl = (OPPModel) modelConstructor.newInstance(app, arc);
					Instant end = clk.instant();
					mdl.getCPlex().end();
					values[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
				}	
				
				series.add(exnodes, StatUtils.mean(values));
			}
			
			dataset.addSeries(series);
		}		
		
		JFreeChart plot = Plotter.create("Model Creation (" + exnodes + " EXNodes)", "OPNodes", "Time (ms)", dataset);		
		
		try {
			Plotter.save(plot, Default.Experiments.RESULTS_DIR + plot.getTitle().getText() + ".svg");
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}
	}

}
