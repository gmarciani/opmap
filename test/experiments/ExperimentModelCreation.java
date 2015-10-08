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
	
	final Class<?> compareModels[] = Experiments.ModelCreation.CMP_MODELS;
	final int repts = Experiments.ModelCreation.REPETITIONS;
	final Clock clk = Clock.systemDefaultZone();
	
	@Test
	public void wrt_EXNodes() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {	
		Map<String, XYSeries> data = new HashMap<String, XYSeries>();
		for (Class<?> optmodel : compareModels) {
			String modelName = optmodel.getSimpleName();
			data.put(modelName, new XYSeries(modelName));
		}				
		
		int opnodes = Experiments.ModelCreation.WRT_EXNodes.OPNODES;
		int exmin 	= Experiments.ModelCreation.WRT_EXNodes.EXMIN;
		int exmax 	= Experiments.ModelCreation.WRT_EXNodes.EXMAX;
		int expas 	= Experiments.ModelCreation.WRT_EXNodes.EXPAS;				
		
		Application app = Experiments.ModelCreation.WRT_EXNodes.app();
		
		double values[] = new double[repts];			
			
		for (int exnodes = exmin; exnodes <= exmax; exnodes += expas) {											
			Architecture arc = Experiments.ModelCreation.WRT_EXNodes.arc(exnodes);	
			for (Class<?> optmodel : compareModels) {
				String modelName = optmodel.getSimpleName();
				for (int rept = 1; rept <= repts; rept++) {
					System.out.printf("#compiling %s wrt. EXNodes# opnodes:%d | exnodes:%d/%d | rep:%d/%d\n", modelName, opnodes, exnodes, exmax, rept, repts);
					Constructor<?> modelConstructor = optmodel.getConstructor(Application.class, Architecture.class);
					Instant start = clk.instant();	
					OPPModel mdl = (OPPModel) modelConstructor.newInstance(app, arc);
					Instant end = clk.instant();
					mdl.getCPlex().end();
					values[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
				}
				data.get(modelName).add(exnodes, StatUtils.mean(values));
			}		
		}
			
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		for (XYSeries mdlData : data.values())
			dataset.addSeries(mdlData);	
		
		String title = String.format("Model Creation (%d OPNodes)", opnodes);
		JFreeChart plot = Plotter.create(title, "EXNodes", "Time (ms)", dataset);		
		
		try {
			Plotter.save(plot, Experiments.RESULTS_DIR + plot.getTitle().getText() + ".svg");
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}
	}
	
	@Test
	public void wrt_OPNodes() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {	
		Map<String, XYSeries> data = new HashMap<String, XYSeries>();
		for (Class<?> optmodel : compareModels) {
			String modelName = optmodel.getSimpleName();
			data.put(modelName, new XYSeries(modelName));
		}				
		
		int exnodes = Experiments.ModelCreation.WRT_OPNodes.EXNODES;
		int opmin 	= Experiments.ModelCreation.WRT_OPNodes.OPMIN;
		int opmax 	= Experiments.ModelCreation.WRT_OPNodes.OPMAX;
		int oppas 	= Experiments.ModelCreation.WRT_OPNodes.OPPAS;				
		
		Architecture arc = Experiments.ModelCreation.WRT_OPNodes.arc();
		
		double values[] = new double[repts];			
			
		for (int opnodes = opmin; opnodes <= opmax; opnodes += oppas) {											
			Application app = Experiments.ModelCreation.WRT_OPNodes.app(opnodes);	
			for (Class<?> optmodel : compareModels) {
				String modelName = optmodel.getSimpleName();
				for (int rept = 1; rept <= repts; rept++) {
					System.out.printf("#compiling %s wrt. OPNodes# exnodes:%d | opnodes:%d/%d | rep:%d/%d\n", modelName, exnodes, opnodes, opmax, rept, repts);
					Constructor<?> modelConstructor = optmodel.getConstructor(Application.class, Architecture.class);
					Instant start = clk.instant();	
					OPPModel mdl = (OPPModel) modelConstructor.newInstance(app, arc);
					Instant end = clk.instant();
					mdl.getCPlex().end();
					values[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
				}
				data.get(modelName).add(opnodes, StatUtils.mean(values));
			}		
		}
			
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		for (XYSeries mdlData : data.values())
			dataset.addSeries(mdlData);	
		
		String title = String.format("Model Creation (%d EXNodes)", exnodes);
		JFreeChart plot = Plotter.create(title, "OPNodes", "Time (ms)", dataset);		
		
		try {
			Plotter.save(plot, Experiments.RESULTS_DIR + plot.getTitle().getText() + ".svg");
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}
	}
	
	@Test
	public void wrt_PinDegree() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {	
		Map<String, XYSeries> data = new HashMap<String, XYSeries>();
		for (Class<?> optmodel : compareModels) {
			String modelName = optmodel.getSimpleName();
			data.put(modelName, new XYSeries(modelName));
		}				
		
		int exnodes = Experiments.ModelCreation.WRT_PinnDegree.EXNODES;
		int opnodes = Experiments.ModelCreation.WRT_PinnDegree.OPNODES;
		double pinmin = Experiments.ModelCreation.WRT_PinnDegree.PINMIN;
		double pinmax = Experiments.ModelCreation.WRT_PinnDegree.PINMAX;
		double pinpas = Experiments.ModelCreation.WRT_PinnDegree.PINPAS;	
		
		Architecture arc = Experiments.ModelCreation.WRT_PinnDegree.arc();
		
		double values[] = new double[repts];			
			
		for (double pindeg = pinmin; pindeg <= pinmax; pindeg += pinpas) {											
			Application app = Experiments.ModelCreation.WRT_PinnDegree.app(arc, pindeg);	
			for (Class<?> optmodel : compareModels) {
				String modelName = optmodel.getSimpleName();
				for (int rept = 1; rept <= repts; rept++) {
					System.out.printf("#compiling %s wrt. PinDegree# exnodes:%d | opnodes:%d | pindeg:%f/%f | rep:%d/%d\n", modelName, exnodes, opnodes, pindeg, pinmax, rept, repts);
					Constructor<?> modelConstructor = optmodel.getConstructor(Application.class, Architecture.class);
					Instant start = clk.instant();	
					OPPModel mdl = (OPPModel) modelConstructor.newInstance(app, arc);
					Instant end = clk.instant();
					mdl.getCPlex().end();
					values[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
				}
				data.get(modelName).add(pindeg, StatUtils.mean(values));
			}		
		}
			
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		for (XYSeries mdlData : data.values())
			dataset.addSeries(mdlData);	
		
		String title = String.format("Model Creation (%d EXNodes %d OPNodes)", exnodes, opnodes);
		JFreeChart plot = Plotter.create(title, "PinDeg", "Time (ms)", dataset);		
		
		try {
			Plotter.save(plot, Experiments.RESULTS_DIR + plot.getTitle().getText() + ".svg");
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}
	}

}
