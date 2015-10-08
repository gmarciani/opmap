package experiments;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

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
import control.solver.OPPSolver;
import control.solver.mp.MPSolver;
import model.application.Application;
import model.architecture.Architecture;
import model.placement.optmodel.OPPModel;

public class ExperimentModelResolution {
	
	@Rule 
	public TestName name = new TestName();
	
	@Before
	public void testInfo() {
		System.out.println("\n/********************************************************************************");
		System.out.println(" * TEST: " + this.getClass().getSimpleName() + " " + name.getMethodName());
		System.out.println(" ********************************************************************************/\n");
	}
	
	final Class<?> compareModels[] = Experiments.ModelResolution.CMP_MODELS;
	final int repts = Experiments.ModelResolution.REPETITIONS;
	final Clock clk = Clock.systemDefaultZone();

	
	/********************************************************************************
	 * Model resolution with respect to the number of exnodes		
	 ********************************************************************************/
	@Test
	public void wrt_EXNodes() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {	
		Map<String, XYSeries> data = new HashMap<String, XYSeries>();
		for (Class<?> optmodel : compareModels) {
			String modelName = optmodel.getSimpleName();
			data.put(modelName, new XYSeries(modelName));
		}				
		
		int opnodes = Experiments.ModelResolution.WRT_EXNodes.OPNODES;
		int exmin 	= Experiments.ModelResolution.WRT_EXNodes.EXMIN;
		int exmax 	= Experiments.ModelResolution.WRT_EXNodes.EXMAX;
		int expas 	= Experiments.ModelResolution.WRT_EXNodes.EXPAS;				
		
		Application app = Experiments.ModelResolution.WRT_EXNodes.app();
		
		double values[] = new double[repts];			
			
		for (int exnodes = exmin; exnodes <= exmax; exnodes += expas) {											
			Architecture arc = Experiments.ModelResolution.WRT_EXNodes.arc(exnodes);	
			for (Class<?> optmodel : compareModels) {				
				Constructor<?> modelConstructor = optmodel.getConstructor(Application.class, Architecture.class);				
				String modelName = optmodel.getSimpleName();
				for (int rept = 1; rept <= repts; rept++) {
					System.out.printf("#resolution by %s wrt. EXNodes# exnodes:%d/%d | opnodes:%d | rep:%d/%d\n", modelName, exnodes, exmax, opnodes, rept, repts);
					OPPModel mdl = (OPPModel) modelConstructor.newInstance(app, arc);
					OPPSolver solver = new MPSolver();
					Instant start = clk.instant();
					boolean solved = solver.solve(mdl);
					Instant end = clk.instant();
					mdl.getCPlex().end();
					if (!solved) {
						System.out.println("Unsolvable");
						arc = Experiments.ModelResolution.WRT_EXNodes.arc(exnodes);	
						rept -= 1;
					} else {
						values[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
					}					
				}
				data.get(modelName).add(exnodes, StatUtils.mean(values));
			}		
		}
			
		XYSeriesCollection dataset = new XYSeriesCollection();		
		for (Class<?> optmodel : compareModels)
			dataset.addSeries(data.get(optmodel.getSimpleName()));
		
		String title = String.format("Model Resolution");
		String subtitle = String.format("exnodes:[%d,%d] | opnodes:%d", exmin, exmax, opnodes);
		JFreeChart plot = Plotter.createLine(title, null, "EXNodes", "Time (ms)", dataset);			
		try {
			Plotter.save(plot, String.format("%s/%s - %s.svg", Experiments.RESULTS_DIR, title, subtitle));
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}
	}
	
	
	/********************************************************************************
	 * Model resolution with respect to the number of opnodes	 		
	 ********************************************************************************/
	@Test
	public void wrt_OPNodes() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {	
		Map<String, XYSeries> data = new HashMap<String, XYSeries>();
		for (Class<?> optmodel : compareModels) {
			String modelName = optmodel.getSimpleName();
			data.put(modelName, new XYSeries(modelName));
		}				
		
		int exnodes = Experiments.ModelResolution.WRT_OPNodes.EXNODES;
		int opmin 	= Experiments.ModelResolution.WRT_OPNodes.OPMIN;
		int opmax 	= Experiments.ModelResolution.WRT_OPNodes.OPMAX;
		int oppas 	= Experiments.ModelResolution.WRT_OPNodes.OPPAS;				
		
		Architecture arc = Experiments.ModelResolution.WRT_OPNodes.arc();
		
		double values[] = new double[repts];			
			
		for (int opnodes = opmin; opnodes <= opmax; opnodes += oppas) {											
			Application app = Experiments.ModelResolution.WRT_OPNodes.app(opnodes);	
			for (Class<?> optmodel : compareModels) {				
				Constructor<?> modelConstructor = optmodel.getConstructor(Application.class, Architecture.class);				
				String modelName = optmodel.getSimpleName();
				for (int rept = 1; rept <= repts; rept++) {
					System.out.printf("#resolution by %s wrt. OPNodes# exnodes:%d | opnodes:%d/%d | rep:%d/%d\n", modelName, exnodes, opnodes, opmax, rept, repts);
					OPPModel mdl = (OPPModel) modelConstructor.newInstance(app, arc);
					OPPSolver solver = new MPSolver();
					Instant start = clk.instant();
					boolean solved = solver.solve(mdl);
					Instant end = clk.instant();
					mdl.getCPlex().end();
					if (!solved) {
						System.out.println("Unsolvable");
						app = Experiments.ModelResolution.WRT_OPNodes.app(opnodes);	
						rept -= 1;
					} else {
						values[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
					}					
				}
				data.get(modelName).add(opnodes, StatUtils.mean(values));
			}		
		}
			
		XYSeriesCollection dataset = new XYSeriesCollection();		
		for (Class<?> optmodel : compareModels)
			dataset.addSeries(data.get(optmodel.getSimpleName()));
		
		String title = String.format("Model Resolution");
		String subtitle = String.format("exnodes:%d | opnodes:[%d,%d]", exnodes, opmin, opmax);
		JFreeChart plot = Plotter.createLine(title, null, "OPNodes", "Time (ms)", dataset);			
		try {
			Plotter.save(plot, String.format("%s/%s - %s.svg", Experiments.RESULTS_DIR, title, subtitle));
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}
	}

	
	/********************************************************************************
	 * Model resolution with respect to the opnodes pinnability factor 		
	 ********************************************************************************/
	@Test
	public void wrt_PINFactor() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {	
		Map<String, XYSeries> data = new HashMap<String, XYSeries>();
		for (Class<?> optmodel : compareModels) {
			String modelName = optmodel.getSimpleName();
			data.put(modelName, new XYSeries(modelName));
		}				
		
		int exnodes = Experiments.ModelResolution.WRT_PINFactor.EXNODES;
		int opnodes = Experiments.ModelResolution.WRT_PINFactor.OPNODES;
		double pinmin = Experiments.ModelResolution.WRT_PINFactor.PINMIN;
		double pinmax = Experiments.ModelResolution.WRT_PINFactor.PINMAX;
		double pinpas = Experiments.ModelResolution.WRT_PINFactor.PINPAS;			
		
		Architecture arc = Experiments.ModelResolution.WRT_PINFactor.arc();
		
		double values[] = new double[repts];			
			
		for (double pinfact = pinmin; pinfact <= pinmax; pinfact += pinpas) {											
			Application app = Experiments.ModelResolution.WRT_PINFactor.app(arc, pinfact);
			for (Class<?> optmodel : compareModels) {				
				Constructor<?> modelConstructor = optmodel.getConstructor(Application.class, Architecture.class);				
				String modelName = optmodel.getSimpleName();
				for (int rept = 1; rept <= repts; rept++) {
					System.out.printf("#resolution by %s wrt. PINFactor# exnodes:%d | opnodes:%d | pinfact:%.2f/%.2f | rep:%d/%d\n", modelName, exnodes, opnodes, pinfact, pinmax, rept, repts);
					OPPModel mdl = (OPPModel) modelConstructor.newInstance(app, arc);
					OPPSolver solver = new MPSolver();
					Instant start = clk.instant();
					boolean solved = solver.solve(mdl);
					Instant end = clk.instant();
					mdl.getCPlex().end();
					if (!solved) {
						System.out.println("Unsolvable");
						app = Experiments.ModelResolution.WRT_PINFactor.app(arc, pinfact);
						rept -= 1;
					} else {
						values[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
					}					
				}
				data.get(modelName).add(pinfact, StatUtils.mean(values));
			}		
		}
			
		XYSeriesCollection dataset = new XYSeriesCollection();		
		for (Class<?> optmodel : compareModels)
			dataset.addSeries(data.get(optmodel.getSimpleName()));
		
		String title = String.format("Model Resolution");
		String subtitle = String.format("exnodes:%d | opnodes:%d | pinfact:[%.2f,%.2f]", exnodes, opnodes, pinmin, pinmax);
		JFreeChart plot = Plotter.createLine(title, null, "PINFactor", "Time (ms)", dataset);			
		try {
			Plotter.save(plot, String.format("%s/%s - %s.svg", Experiments.RESULTS_DIR, title, subtitle));
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}
	}

	
	/********************************************************************************
	 * Model resolution with respect to the opnodes diversity factor 		
	 ********************************************************************************/
	@Test
	public void wrt_DIVFactor() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {	
		Map<String, XYSeries> data = new HashMap<String, XYSeries>();
		for (Class<?> optmodel : compareModels) {
			String modelName = optmodel.getSimpleName();
			data.put(modelName, new XYSeries(modelName));
		}				
		
		int exnodes = Experiments.ModelResolution.WRT_DIVFactor.EXNODES;
		int opnodes = Experiments.ModelResolution.WRT_DIVFactor.OPNODES;
		double divmin = Experiments.ModelResolution.WRT_DIVFactor.DIVMIN;
		double divmax = Experiments.ModelResolution.WRT_DIVFactor.DIVMAX;
		double divpas = Experiments.ModelResolution.WRT_DIVFactor.DIVPAS;			
		
		Application app = Experiments.ModelResolution.WRT_DIVFactor.app();
		
		double values[] = new double[repts];			
			
		for (double divfact = divmin; divfact <= divmax; divfact += divpas) {											
			Architecture arc = Experiments.ModelResolution.WRT_DIVFactor.arc(divfact);
			for (Class<?> optmodel : compareModels) {				
				Constructor<?> modelConstructor = optmodel.getConstructor(Application.class, Architecture.class);				
				String modelName = optmodel.getSimpleName();
				for (int rept = 1; rept <= repts; rept++) {
					System.out.printf("#resolution by %s wrt. DIVFactor# exnodes:%d | opnodes:%d | divfact:%.2f/%.2f | rep:%d/%d\n", modelName, exnodes, opnodes, divfact, divmax, rept, repts);
					OPPModel mdl = (OPPModel) modelConstructor.newInstance(app, arc);
					OPPSolver solver = new MPSolver();
					Instant start = clk.instant();
					boolean solved = solver.solve(mdl);
					Instant end = clk.instant();
					mdl.getCPlex().end();
					if (!solved) {
						System.out.println("Unsolvable");
						arc = Experiments.ModelResolution.WRT_DIVFactor.arc(divfact);
						rept -= 1;
					} else {
						values[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
					}					
				}
				data.get(modelName).add(divfact, StatUtils.mean(values));
			}		
		}
			
		XYSeriesCollection dataset = new XYSeriesCollection();		
		for (Class<?> optmodel : compareModels)
			dataset.addSeries(data.get(optmodel.getSimpleName()));
		
		String title = String.format("Model Resolution");
		String subtitle = String.format("exnodes:%d | opnodes:%d | divfact:[%.2f,%.2f]", exnodes, opnodes, divmin, divmax);
		JFreeChart plot = Plotter.createLine(title, null, "DIVFactor", "Time (ms)", dataset);			
		try {
			Plotter.save(plot, String.format("%s/%s - %s.svg", Experiments.RESULTS_DIR, title, subtitle));
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}
	}

}
