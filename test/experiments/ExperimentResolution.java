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

public class ExperimentResolution {
	
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
		
		int opnodes = Experiments.Resolution.R_EXNode.OPNODES;
		int exmin 	= Experiments.Resolution.R_EXNode.EXMIN;
		int exmax 	= Experiments.Resolution.R_EXNode.EXMAX;
		int expas 	= Experiments.Resolution.R_EXNode.EXPAS;			
		
		UNIT unit 	= Experiments.Resolution.R_EXNode.MEASURE;
		
		Application app = Experiments.Resolution.R_EXNode.app();
		
		double values[] = new double[repts];			
			
		for (int exnodes = exmin; exnodes <= exmax; exnodes += expas) {											
			Architecture arc = Experiments.Resolution.R_EXNode.arc(exnodes);	
			for (Class<?> optmodel : compareModels) {				
				Constructor<?> modelConstructor = optmodel.getConstructor(Application.class, Architecture.class);				
				String modelName = optmodel.getSimpleName();
				for (int rept = 1; rept <= repts; rept++) {
					System.out.printf("#R-EXNode: resolving %s# exnodes:%d/%d | opnodes:%d | rep:%d/%d\n", modelName, exnodes, exmax, opnodes, rept, repts);
					OPPModel mdl = (OPPModel) modelConstructor.newInstance(app, arc);
					OPPSolver solver = new MPSolver();
					Instant start = clk.instant();
					boolean solved = solver.solve(mdl);
					Instant end = clk.instant();
					mdl.getCPlex().end();
					if (!solved) {
						System.out.println("Unsolvable");
						arc = Experiments.Resolution.R_EXNode.arc(exnodes);	
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
		JFreeChart plot = Plotter.createLine(null, null, "Nodi computazionali", String.format("Tempo (%s)", unit.toString()), dataset);			
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
		
		int exnodes = Experiments.Resolution.R_OPNode.EXNODES;
		int opmin 	= Experiments.Resolution.R_OPNode.OPMIN;
		int opmax 	= Experiments.Resolution.R_OPNode.OPMAX;
		int oppas 	= Experiments.Resolution.R_OPNode.OPPAS;		
		
		UNIT unit 	= Experiments.Resolution.R_OPNode.MEASURE;
		
		Architecture arc = Experiments.Resolution.R_OPNode.arc();
		
		double values[] = new double[repts];			
			
		for (int opnodes = opmin; opnodes <= opmax; opnodes += oppas) {											
			Application app = Experiments.Resolution.R_OPNode.app(opnodes);	
			for (Class<?> optmodel : compareModels) {				
				Constructor<?> modelConstructor = optmodel.getConstructor(Application.class, Architecture.class);				
				String modelName = optmodel.getSimpleName();
				for (int rept = 1; rept <= repts; rept++) {
					System.out.printf("#R-OPNode: resolving %s# exnodes:%d | opnodes:%d/%d | rep:%d/%d\n", modelName, exnodes, opnodes, opmax, rept, repts);
					OPPModel mdl = (OPPModel) modelConstructor.newInstance(app, arc);
					OPPSolver solver = new MPSolver();
					Instant start = clk.instant();
					boolean solved = solver.solve(mdl);
					Instant end = clk.instant();
					mdl.getCPlex().end();
					if (!solved) {
						System.out.println("Unsolvable");
						app = Experiments.Resolution.R_OPNode.app(opnodes);	
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
		JFreeChart plot = Plotter.createLine(null, null, "Nodi operazionali", String.format("Tempo (%s)", unit.toString()), dataset);			
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
		
		int exnodes = Experiments.Resolution.R_PINFactor.EXNODES;
		int opnodes = Experiments.Resolution.R_PINFactor.OPNODES;
		double pinmin = Experiments.Resolution.R_PINFactor.PINMIN;
		double pinmax = Experiments.Resolution.R_PINFactor.PINMAX;
		double pinpas = Experiments.Resolution.R_PINFactor.PINPAS;		
		
		UNIT unit 	= Experiments.Resolution.R_PINFactor.MEASURE;
		
		Architecture arc = Experiments.Resolution.R_PINFactor.arc();
		
		double values[] = new double[repts];			
			
		for (double pinfact = pinmin; pinfact <= pinmax; pinfact += pinpas) {											
			Application app = Experiments.Resolution.R_PINFactor.app(arc, pinfact);
			for (Class<?> optmodel : compareModels) {				
				Constructor<?> modelConstructor = optmodel.getConstructor(Application.class, Architecture.class);				
				String modelName = optmodel.getSimpleName();
				for (int rept = 1; rept <= repts; rept++) {
					System.out.printf("#R-PINFactor: resolving %s# exnodes:%d | opnodes:%d | pinfact:%.2f/%.2f | rep:%d/%d\n", modelName, exnodes, opnodes, pinfact, pinmax, rept, repts);
					OPPModel mdl = (OPPModel) modelConstructor.newInstance(app, arc);
					OPPSolver solver = new MPSolver();
					Instant start = clk.instant();
					boolean solved = solver.solve(mdl);
					Instant end = clk.instant();
					mdl.getCPlex().end();
					if (!solved) {
						System.out.println("Unsolvable");
						app = Experiments.Resolution.R_PINFactor.app(arc, pinfact);
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
		JFreeChart plot = Plotter.createLine(null, null, "Fattore di pin", String.format("Tempo (%s)", unit.toString()), dataset);			
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
		
		int exnodes = Experiments.Resolution.R_DIVFactor.EXNODES;
		int opnodes = Experiments.Resolution.R_DIVFactor.OPNODES;
		double divmin = Experiments.Resolution.R_DIVFactor.DIVMIN;
		double divmax = Experiments.Resolution.R_DIVFactor.DIVMAX;
		double divpas = Experiments.Resolution.R_DIVFactor.DIVPAS;	
		
		UNIT unit 	= Experiments.Resolution.R_DIVFactor.MEASURE;
		
		
		
		double values[] = new double[repts];			
			
		for (double divfact = divmin; divfact <= divmax; divfact += divpas) {											
			Architecture arc = Experiments.Resolution.R_DIVFactor.arc(divfact);
			Application app = Experiments.Resolution.R_DIVFactor.app(divfact);
			for (Class<?> optmodel : compareModels) {				
				Constructor<?> modelConstructor = optmodel.getConstructor(Application.class, Architecture.class);				
				String modelName = optmodel.getSimpleName();
				for (int rept = 1; rept <= repts; rept++) {
					System.out.printf("#R-DIVFactor: resolving %s# exnodes:%d | opnodes:%d | divfact:%.2f/%.2f | rep:%d/%d\n", modelName, exnodes, opnodes, divfact, divmax, rept, repts);
					OPPModel mdl = (OPPModel) modelConstructor.newInstance(app, arc);
					OPPSolver solver = new MPSolver();
					Instant start = clk.instant();
					boolean solved = solver.solve(mdl);
					Instant end = clk.instant();
					mdl.getCPlex().end();
					if (!solved) {
						System.out.println("Unsolvable");
						arc = Experiments.Resolution.R_DIVFactor.arc(divfact);
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
		JFreeChart plot = Plotter.createLine(null, null, "Fattore di diversità", String.format("Tempo (%s)", unit.toString()), dataset);			
		try {
			Plotter.save(plot, String.format("%s/%s - %s.svg", Experiments.RESULTS_DIR, title, subtitle));
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}
	}

}
