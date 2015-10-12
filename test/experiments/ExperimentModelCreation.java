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
import experiments.Experiments.UNIT;
import model.application.Application;
import model.architecture.Architecture;
import model.placement.optmodel.OPPModel;

public class ExperimentModelCreation {

	@Rule 
	public TestName name = new TestName();
	
	@Before
	public void testInfo() {
		System.out.println("\n/********************************************************************************");
		System.out.println(" * TEST: " + this.getClass().getSimpleName() + " " + name.getMethodName());
		System.out.println(" ********************************************************************************/\n");
	}
	
	final Class<?> compareModels[] = Experiments.Creation.CMP_MODELS;
	final int repts = Experiments.Creation.REPETITIONS;
	final Clock clk = Clock.systemDefaultZone();
	
	@Test
	public void expCEXNode() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {	
		Map<String, XYSeries> data = new HashMap<String, XYSeries>();
		for (Class<?> optmodel : compareModels) {
			String modelName = optmodel.getSimpleName();
			data.put(modelName, new XYSeries(modelName));
		}				
		
		int opnodes = Experiments.Creation.WRT_EXNodes.OPNODES;
		int exmin 	= Experiments.Creation.WRT_EXNodes.EXMIN;
		int exmax 	= Experiments.Creation.WRT_EXNodes.EXMAX;
		int expas 	= Experiments.Creation.WRT_EXNodes.EXPAS;	
		
		UNIT unit 	= Experiments.Creation.WRT_EXNodes.MEASURE;
		
		Application app = Experiments.Creation.WRT_EXNodes.app();
		
		double values[] = new double[repts];			
			
		for (int exnodes = exmin; exnodes <= exmax; exnodes += expas) {											
			Architecture arc = Experiments.Creation.WRT_EXNodes.arc(exnodes);	
			for (Class<?> optmodel : compareModels) {
				String modelName = optmodel.getSimpleName();
				for (int rept = 1; rept <= repts; rept++) {
					System.out.printf("#compiling %s wrt. EXNodes# exnodes:%d/%d | opnodes:%d | rep:%d/%d\n", modelName, exnodes, exmax, opnodes, rept, repts);
					Constructor<?> modelConstructor = optmodel.getConstructor(Application.class, Architecture.class);
					Instant start = clk.instant();	
					OPPModel mdl = (OPPModel) modelConstructor.newInstance(app, arc);
					Instant end = clk.instant();
					mdl.getCPlex().end();
					if (unit == UNIT.MILLIS)
						values[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
					else if (unit == UNIT.SECOND)
						values[rept - 1] = (end.toEpochMilli() - start.toEpochMilli()) / 1000.0;
				}
				data.get(modelName).add(exnodes, StatUtils.percentile(values, 50));
			}		
		}
			
		XYSeriesCollection dataset = new XYSeriesCollection();		
		for (Class<?> optmodel : compareModels)
			dataset.addSeries(data.get(optmodel.getSimpleName()));
		
		String title = String.format("C-EXNode");
		String subtitle = String.format("exnodes[%d,%d], opnodes:%d", exmin, exmax, opnodes);
		JFreeChart plot = Plotter.createLine(null, null, "EXNodes", String.format("Time (%s)", unit.toString()), dataset);			
		try {
			Plotter.save(plot, String.format("%s/%s - %s.svg", Experiments.RESULTS_DIR, title, subtitle));
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}
	}
	
	@Test
	public void expCOPNode() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {	
		Map<String, XYSeries> data = new HashMap<String, XYSeries>();
		for (Class<?> optmodel : compareModels) {
			String modelName = optmodel.getSimpleName();
			data.put(modelName, new XYSeries(modelName));
		}				
		
		int exnodes = Experiments.Creation.WRT_OPNodes.EXNODES;
		int opmin 	= Experiments.Creation.WRT_OPNodes.OPMIN;
		int opmax 	= Experiments.Creation.WRT_OPNodes.OPMAX;
		int oppas 	= Experiments.Creation.WRT_OPNodes.OPPAS;		
		
		UNIT unit 	= Experiments.Creation.WRT_OPNodes.MEASURE;
		
		Architecture arc = Experiments.Creation.WRT_OPNodes.arc();
		
		double values[] = new double[repts];			
			
		for (int opnodes = opmin; opnodes <= opmax; opnodes += oppas) {											
			Application app = Experiments.Creation.WRT_OPNodes.app(opnodes);	
			for (Class<?> optmodel : compareModels) {
				String modelName = optmodel.getSimpleName();
				for (int rept = 1; rept <= repts; rept++) {
					System.out.printf("#compiling %s wrt. OPNodes# exnodes:%d | opnodes:%d/%d | rep:%d/%d\n", modelName, exnodes, opnodes, opmax, rept, repts);
					Constructor<?> modelConstructor = optmodel.getConstructor(Application.class, Architecture.class);
					Instant start = clk.instant();	
					OPPModel mdl = (OPPModel) modelConstructor.newInstance(app, arc);
					Instant end = clk.instant();
					mdl.getCPlex().end();
					if (unit == UNIT.MILLIS)
						values[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
					else if (unit == UNIT.SECOND)
						values[rept - 1] = (end.toEpochMilli() - start.toEpochMilli()) / 1000.0;
				}
				data.get(modelName).add(opnodes, StatUtils.percentile(values, 50));
			}		
		}
			
		XYSeriesCollection dataset = new XYSeriesCollection();		
		for (Class<?> optmodel : compareModels)
			dataset.addSeries(data.get(optmodel.getSimpleName()));
		
		String title = String.format("C-OPNode");
		String subtitle = String.format("exnodes:%d, opnodes[%d,%d]", exnodes, opmin, opmax);
		JFreeChart plot = Plotter.createLine(null, null, "OPNodes", String.format("Time (%s)", unit.toString()), dataset);
		try {
			Plotter.save(plot, String.format("%s/%s - %s.svg", Experiments.RESULTS_DIR, title, subtitle));
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}
	}
	
	@Test
	public void expCPINFactor() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {	
		Map<String, XYSeries> data = new HashMap<String, XYSeries>();
		for (Class<?> optmodel : compareModels) {
			String modelName = optmodel.getSimpleName();
			data.put(modelName, new XYSeries(modelName));
		}				
		
		int exnodes = Experiments.Creation.WRT_PINFactor.EXNODES;
		int opnodes = Experiments.Creation.WRT_PINFactor.OPNODES;
		double pinmin = Experiments.Creation.WRT_PINFactor.PINMIN;
		double pinmax = Experiments.Creation.WRT_PINFactor.PINMAX;
		double pinpas = Experiments.Creation.WRT_PINFactor.PINPAS;	
		
		UNIT unit 	= Experiments.Creation.WRT_PINFactor.MEASURE;
		
		Architecture arc = Experiments.Creation.WRT_PINFactor.arc();
		
		double values[] = new double[repts];			
			
		for (double pinfact = pinmin; pinfact <= pinmax; pinfact += pinpas) {											
			Application app = Experiments.Creation.WRT_PINFactor.app(arc, pinfact);	
			for (Class<?> optmodel : compareModels) {
				String modelName = optmodel.getSimpleName();
				for (int rept = 1; rept <= repts; rept++) {
					System.out.printf("#compiling %s wrt. PINFactor# exnodes:%d | opnodes:%d | pinfact:%.2f/%.2f | rep:%d/%d\n", modelName, exnodes, opnodes, pinfact, pinmax, rept, repts);
					Constructor<?> modelConstructor = optmodel.getConstructor(Application.class, Architecture.class);
					Instant start = clk.instant();	
					OPPModel mdl = (OPPModel) modelConstructor.newInstance(app, arc);
					Instant end = clk.instant();
					mdl.getCPlex().end();
					if (unit == UNIT.MILLIS)
						values[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
					else if (unit == UNIT.SECOND)
						values[rept - 1] = (end.toEpochMilli() - start.toEpochMilli()) / 1000.0;
				}
				data.get(modelName).add(pinfact, StatUtils.percentile(values, 50));
			}		
		}
			
		XYSeriesCollection dataset = new XYSeriesCollection();		
		for (Class<?> optmodel : compareModels)
			dataset.addSeries(data.get(optmodel.getSimpleName()));
		
		String title = String.format("C-PINFactor");
		String subtitle = String.format("exnodes:%d, opnodes:%d, pinfact[%.2f,%.2f]", exnodes, opnodes, pinmin, pinmax);
		JFreeChart plot = Plotter.createLine(null, null, "PINFactor", String.format("Time (%s)", unit.toString()), dataset);
		try {
			Plotter.save(plot, String.format("%s/%s - %s.svg", Experiments.RESULTS_DIR, title, subtitle));
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}
	}

}
