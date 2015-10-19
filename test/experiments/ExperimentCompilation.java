package experiments;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import control.exceptions.ModelException;
import control.exceptions.SolverException;
import experiments.Experiments.UNIT;
import model.application.Application;
import model.architecture.Architecture;
import model.placement.optmodel.OPPModel;

public class ExperimentCompilation {

	@Rule 
	public TestName name = new TestName();
	
	@Before
	public void testInfo() {
		System.out.println("\n/********************************************************************************");
		System.out.println(" * TEST: " + this.getClass().getSimpleName() + " " + name.getMethodName());
		System.out.println(" ********************************************************************************/\n");
	}
	
	final List<Class<?>> models = Experiments.Compilation.MODELS;
	final int repts = Experiments.Compilation.REPETITIONS;
	final Clock clk = Clock.systemDefaultZone();
	
	/********************************************************************************
	 * Model compilation with respect to the number of exnodes		
	 ********************************************************************************/	
	@Test
	public void expCEXNode() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {	
		
		int opnodes = Experiments.Compilation.C_EXNode.OPNODES;
		int exmin 	= Experiments.Compilation.C_EXNode.EXMIN;
		int exmax 	= Experiments.Compilation.C_EXNode.EXMAX;
		int expas 	= Experiments.Compilation.C_EXNode.EXPAS;			
		UNIT unit 	= Experiments.Compilation.C_EXNode.MEASURE;
		
		String title = String.format("C-EXNode - exnodes[%d,%d], opnodes:%d", exmin, exmax, opnodes);
		Path path = Paths.get(String.format("%s/%s.txt", Experiments.RESULTS_DIR, title));
		BufferedWriter writer = Files.newBufferedWriter(path);
		String header = String.format("EXNodes\t%s\n", models.stream().map(model -> model.getSimpleName()).collect(Collectors.joining("\t")));
		writer.write(header);
		
		Application app = Experiments.Compilation.C_EXNode.app();
		
		double iterValues [] = new double[repts];
		double medians[]	= new double[models.size()];
			
		for (int exnodes = exmin; exnodes <= exmax; exnodes += expas) {											
			Architecture arc = Experiments.Compilation.C_EXNode.arc(exnodes);
			for (Class<?> optmodel : models) {
				String modelName = optmodel.getSimpleName();
				for (int rept = 1; rept <= repts; rept++) {
					System.out.printf("#C-EXNode: compiling %s# exnodes:%d/%d | opnodes:%d | rep:%d/%d\n", modelName, exnodes, exmax, opnodes, rept, repts);
					Constructor<?> modelConstructor = optmodel.getConstructor(Application.class, Architecture.class);
					Instant start = clk.instant();	
					OPPModel mdl = (OPPModel) modelConstructor.newInstance(app, arc);
					Instant end = clk.instant();
					mdl.getCPlex().end();
					if (unit == UNIT.MILLIS)
						iterValues[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
					else if (unit == UNIT.SECOND)
						iterValues[rept - 1] = (end.toEpochMilli() - start.toEpochMilli()) / 1000.0;
				}
				double median = StatUtils.percentile(iterValues, 50);
				medians[models.indexOf(optmodel)] = median;
			}
			writer.append(String.format("%d\t%s\n", exnodes, StringUtils.join(medians, '\t')));
		}		
		writer.close();
	}
	
	/********************************************************************************
	 * Model compilation with respect to the number of opnodes		
	 ********************************************************************************/
	@Test
	public void expCOPNode() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {	
	
		int exnodes = Experiments.Compilation.C_OPNode.EXNODES;
		int opmin 	= Experiments.Compilation.C_OPNode.OPMIN;
		int opmax 	= Experiments.Compilation.C_OPNode.OPMAX;
		int oppas 	= Experiments.Compilation.C_OPNode.OPPAS;		
		UNIT unit 	= Experiments.Compilation.C_OPNode.MEASURE;
		
		String title = String.format("C-OPNode - exnodes:%d, opnodes[%d,%d]", exnodes, opmin, opmax);
		Path path = Paths.get(String.format("%s/%s.txt", Experiments.RESULTS_DIR, title));
		BufferedWriter writer = Files.newBufferedWriter(path);
		String header = String.format("OPNodes\t%s\n", models.stream().map(model -> model.getSimpleName()).collect(Collectors.joining("\t")));
		writer.write(header);
		
		Architecture arc = Experiments.Compilation.C_OPNode.arc();
		
		double observe[] = new double[repts];
		double medians[] = new double[models.size()];
			
		for (int opnodes = opmin; opnodes <= opmax; opnodes += oppas) {											
			Application app = Experiments.Compilation.C_OPNode.app(opnodes);	
			for (Class<?> optmodel : models) {
				String modelName = optmodel.getSimpleName();
				for (int rept = 1; rept <= repts; rept++) {
					System.out.printf("#C-OPNode: compiling: %s# exnodes:%d | opnodes:%d/%d | rep:%d/%d\n", modelName, exnodes, opnodes, opmax, rept, repts);
					Constructor<?> modelConstructor = optmodel.getConstructor(Application.class, Architecture.class);
					Instant start = clk.instant();	
					OPPModel mdl = (OPPModel) modelConstructor.newInstance(app, arc);
					Instant end = clk.instant();
					mdl.getCPlex().end();
					if (unit == UNIT.MILLIS)
						observe[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
					else if (unit == UNIT.SECOND)
						observe[rept - 1] = (end.toEpochMilli() - start.toEpochMilli()) / 1000.0;
				}
				double median = StatUtils.percentile(observe, 50);
				medians[models.indexOf(optmodel)] = median;
			}
			writer.append(String.format("%d\t%s\n", opnodes, StringUtils.join(medians, '\t')));
		}		
		writer.close();
	}
	
	/********************************************************************************
	 * Model compilation with respect to the pin factor		
	 ********************************************************************************/
	@Test
	public void expCPINFactor() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {	
		
		int exnodes = Experiments.Compilation.C_PINFactor.EXNODES;
		int opnodes = Experiments.Compilation.C_PINFactor.OPNODES;
		double pinmin = Experiments.Compilation.C_PINFactor.PINMIN;
		double pinmax = Experiments.Compilation.C_PINFactor.PINMAX;
		double pinpas = Experiments.Compilation.C_PINFactor.PINPAS;			
		UNIT unit 	= Experiments.Compilation.C_PINFactor.MEASURE;
		
		String title = String.format("C-PINFactor - exnodes:%d, opnodes:%d, pinfact[%.2f,%.2f]", exnodes, opnodes, pinmin, pinmax);
		Path path = Paths.get(String.format("%s/%s.txt", Experiments.RESULTS_DIR, title));
		BufferedWriter writer = Files.newBufferedWriter(path);
		String header = String.format("PINFactor\t%s\n", models.stream().map(model -> model.getSimpleName()).collect(Collectors.joining("\t")));
		writer.write(header);
		
		Architecture arc = Experiments.Compilation.C_PINFactor.arc();
		
		double observe[] = new double[repts];			
		double medians[] = new double[models.size()];
			
		for (double pinfact = pinmin; pinfact <= pinmax; pinfact += pinpas) {											
			Application app = Experiments.Compilation.C_PINFactor.app(arc, pinfact);	
			for (Class<?> optmodel : models) {
				String modelName = optmodel.getSimpleName();
				for (int rept = 1; rept <= repts; rept++) {
					System.out.printf("#C-PINFactor: compiling: %s# exnodes:%d | opnodes:%d | pinfact:%.2f/%.2f | rep:%d/%d\n", modelName, exnodes, opnodes, pinfact, pinmax, rept, repts);
					Constructor<?> modelConstructor = optmodel.getConstructor(Application.class, Architecture.class);
					Instant start = clk.instant();	
					OPPModel mdl = (OPPModel) modelConstructor.newInstance(app, arc);
					Instant end = clk.instant();
					mdl.getCPlex().end();
					if (unit == UNIT.MILLIS)
						observe[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
					else if (unit == UNIT.SECOND)
						observe[rept - 1] = (end.toEpochMilli() - start.toEpochMilli()) / 1000.0;
				}
				double median = StatUtils.percentile(observe, 50);
				medians[models.indexOf(optmodel)] = median;
			}	
			writer.append(String.format("%.2f\t%s\n", pinfact, StringUtils.join(medians, '\t')));
		}
		writer.close();
	}

}
