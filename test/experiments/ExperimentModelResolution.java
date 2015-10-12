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
import experiments.Experiments.UNIT;
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
	
	final Class<?> compareModels[] = Experiments.Resolution.CMP_MODELS;
	final int repts = Experiments.Resolution.REPETITIONS;
	final Clock clk = Clock.systemDefaultZone();

	
	/********************************************************************************
	 * Model resolution with respect to the number of exnodes		
	 ********************************************************************************/
	@Test
	public void expREXNode() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {	
		Map<String, XYSeries> data = new HashMap<String, XYSeries>();
		for (Class<?> optmodel : compareModels) {
			String modelName = optmodel.getSimpleName();
			data.put(modelName, new XYSeries(modelName));
		}				
		
		int opnodes = Experiments.Resolution.WRT_EXNodes.OPNODES;
		int exmin 	= Experiments.Resolution.WRT_EXNodes.EXMIN;
		int exmax 	= Experiments.Resolution.WRT_EXNodes.EXMAX;
		int expas 	= Experiments.Resolution.WRT_EXNodes.EXPAS;			
		
		UNIT unit 	= Experiments.Resolution.WRT_EXNodes.MEASURE;
		
		Application app = Experiments.Resolution.WRT_EXNodes.app();
		
		double values[] = new double[repts];			
			
		for (int exnodes = exmin; exnodes <= exmax; exnodes += expas) {											
			Architecture arc = Experiments.Resolution.WRT_EXNodes.arc(exnodes);	
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
						arc = Experiments.Resolution.WRT_EXNodes.arc(exnodes);	
						rept -= 1;
					} else {
						if (unit == UNIT.MILLIS)
							values[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
						else if (unit == UNIT.SECOND)
							values[rept - 1] = (end.toEpochMilli() - start.toEpochMilli()) / 1000.0;
					}					
				}
				data.get(modelName).add(exnodes, StatUtils.percentile(values, 50));
			}		
		}
			
		XYSeriesCollection dataset = new XYSeriesCollection();		
		for (Class<?> optmodel : compareModels)
			dataset.addSeries(data.get(optmodel.getSimpleName()));
		
		String title = String.format("R-EXNode");
		String subtitle = String.format("exnodes[%d,%d], opnodes%d", exmin, exmax, opnodes);
		JFreeChart plot = Plotter.createLine(title, null, "EXNodes", String.format("Time (%s)", unit.toString()), dataset);			
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
	public void expROPNode() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {	
		Map<String, XYSeries> data = new HashMap<String, XYSeries>();
		for (Class<?> optmodel : compareModels) {
			String modelName = optmodel.getSimpleName();
			data.put(modelName, new XYSeries(modelName));
		}				
		
		int exnodes = Experiments.Resolution.WRT_OPNodes.EXNODES;
		int opmin 	= Experiments.Resolution.WRT_OPNodes.OPMIN;
		int opmax 	= Experiments.Resolution.WRT_OPNodes.OPMAX;
		int oppas 	= Experiments.Resolution.WRT_OPNodes.OPPAS;		
		
		UNIT unit 	= Experiments.Resolution.WRT_OPNodes.MEASURE;
		
		Architecture arc = Experiments.Resolution.WRT_OPNodes.arc();
		
		double values[] = new double[repts];			
			
		for (int opnodes = opmin; opnodes <= opmax; opnodes += oppas) {											
			Application app = Experiments.Resolution.WRT_OPNodes.app(opnodes);	
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
						app = Experiments.Resolution.WRT_OPNodes.app(opnodes);	
						rept -= 1;
					} else {
						if (unit == UNIT.MILLIS)
							values[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
						else if (unit == UNIT.SECOND)
							values[rept - 1] = (end.toEpochMilli() - start.toEpochMilli()) / 1000.0;
					}					
				}
				data.get(modelName).add(opnodes, StatUtils.percentile(values, 50));
			}		
		}
			
		XYSeriesCollection dataset = new XYSeriesCollection();		
		for (Class<?> optmodel : compareModels)
			dataset.addSeries(data.get(optmodel.getSimpleName()));
		
		String title = String.format("R-OPNode");
		String subtitle = String.format("exnodes%d, opnodes[%d,%d]", exnodes, opmin, opmax);
		JFreeChart plot = Plotter.createLine(title, null, "OPNodes", String.format("Time (%s)", unit.toString()), dataset);			
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
	public void expRPINFactor() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {	
		Map<String, XYSeries> data = new HashMap<String, XYSeries>();
		for (Class<?> optmodel : compareModels) {
			String modelName = optmodel.getSimpleName();
			data.put(modelName, new XYSeries(modelName));
		}				
		
		int exnodes = Experiments.Resolution.WRT_PINFactor.EXNODES;
		int opnodes = Experiments.Resolution.WRT_PINFactor.OPNODES;
		double pinmin = Experiments.Resolution.WRT_PINFactor.PINMIN;
		double pinmax = Experiments.Resolution.WRT_PINFactor.PINMAX;
		double pinpas = Experiments.Resolution.WRT_PINFactor.PINPAS;		
		
		UNIT unit 	= Experiments.Resolution.WRT_PINFactor.MEASURE;
		
		Architecture arc = Experiments.Resolution.WRT_PINFactor.arc();
		
		double values[] = new double[repts];			
			
		for (double pinfact = pinmin; pinfact <= pinmax; pinfact += pinpas) {											
			Application app = Experiments.Resolution.WRT_PINFactor.app(arc, pinfact);
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
						app = Experiments.Resolution.WRT_PINFactor.app(arc, pinfact);
						rept -= 1;
					} else {
						if (unit == UNIT.MILLIS)
							values[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
						else if (unit == UNIT.SECOND)
							values[rept - 1] = (end.toEpochMilli() - start.toEpochMilli()) / 1000.0;
					}					
				}
				data.get(modelName).add(pinfact, StatUtils.percentile(values, 50));
			}		
		}
			
		XYSeriesCollection dataset = new XYSeriesCollection();		
		for (Class<?> optmodel : compareModels)
			dataset.addSeries(data.get(optmodel.getSimpleName()));
		
		String title = String.format("R-PINFactor");
		String subtitle = String.format("exnodes%d, opnodes%d, pinfact[%.2f,%.2f]", exnodes, opnodes, pinmin, pinmax);
		JFreeChart plot = Plotter.createLine(title, null, "PINFactor", String.format("Time (%s)", unit.toString()), dataset);			
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
	public void expRDIVFactor() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {	
		Map<String, XYSeries> data = new HashMap<String, XYSeries>();
		for (Class<?> optmodel : compareModels) {
			String modelName = optmodel.getSimpleName();
			data.put(modelName, new XYSeries(modelName));
		}				
		
		int exnodes = Experiments.Resolution.WRT_DIVFactor.EXNODES;
		int opnodes = Experiments.Resolution.WRT_DIVFactor.OPNODES;
		double divmin = Experiments.Resolution.WRT_DIVFactor.DIVMIN;
		double divmax = Experiments.Resolution.WRT_DIVFactor.DIVMAX;
		double divpas = Experiments.Resolution.WRT_DIVFactor.DIVPAS;	
		
		UNIT unit 	= Experiments.Resolution.WRT_DIVFactor.MEASURE;
		
		Application app = Experiments.Resolution.WRT_DIVFactor.app();
		
		double values[] = new double[repts];			
			
		for (double divfact = divmin; divfact <= divmax; divfact += divpas) {											
			Architecture arc = Experiments.Resolution.WRT_DIVFactor.arc(divfact);
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
						arc = Experiments.Resolution.WRT_DIVFactor.arc(divfact);
						rept -= 1;
					} else {
						if (unit == UNIT.MILLIS)
							values[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
						else if (unit == UNIT.SECOND)
							values[rept - 1] = (end.toEpochMilli() - start.toEpochMilli()) / 1000.0;
					}					
				}
				data.get(modelName).add(divfact, StatUtils.percentile(values, 50));
			}		
		}
			
		XYSeriesCollection dataset = new XYSeriesCollection();		
		for (Class<?> optmodel : compareModels)
			dataset.addSeries(data.get(optmodel.getSimpleName()));
		
		String title = String.format("R-DIVFactor");
		String subtitle = String.format("exnodes%d, opnodes%d, divfact[%.2f,%.2f]", exnodes, opnodes, divmin, divmax);
		JFreeChart plot = Plotter.createLine(title, null, "DIVFactor", String.format("Time (%s)", unit.toString()), dataset);			
		try {
			Plotter.save(plot, String.format("%s/%s - %s.svg", Experiments.RESULTS_DIR, title, subtitle));
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}
	}

}
