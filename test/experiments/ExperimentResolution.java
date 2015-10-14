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
	
	final List<Class<?>> models = Experiments.Compilation.MODELS;
	final int repts = Experiments.Resolution.REPETITIONS;
	final Clock clk = Clock.systemDefaultZone();

	
	/********************************************************************************
	 * Model resolution with respect to the number of exnodes		
	 ********************************************************************************/
	@Test
	public void expREXNode() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {			
		
		int opnodes = Experiments.Resolution.R_EXNode.OPNODES;
		int exmin 	= Experiments.Resolution.R_EXNode.EXMIN;
		int exmax 	= Experiments.Resolution.R_EXNode.EXMAX;
		int expas 	= Experiments.Resolution.R_EXNode.EXPAS;		
		UNIT unit 	= Experiments.Resolution.R_EXNode.MEASURE;
		
		String title = String.format("R-EXNode - exnodes[%d,%d], opnodes:%d", exmin, exmax, opnodes);
		Path path = Paths.get(String.format("%s/%s.txt", Experiments.RESULTS_DIR, title));
		BufferedWriter writer = Files.newBufferedWriter(path);
		String header = String.format("EXNodes\t%s\n", models.stream().map(model -> model.getSimpleName()).collect(Collectors.joining("\t")));
		writer.write(header);
		
		Application app = Experiments.Resolution.R_EXNode.app();
		
		double iterValues[] = new double[repts];	
		double medians[]	= new double[models.size()];
			
		for (int exnodes = exmin; exnodes <= exmax; exnodes += expas) {											
			Architecture arc = Experiments.Resolution.R_EXNode.arc(exnodes);	
			for (Class<?> optmodel : models) {				
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
							iterValues[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
						else if (unit == UNIT.SECOND)
							iterValues[rept - 1] = (end.toEpochMilli() - start.toEpochMilli()) / 1000.0;
					}	
				}
				double median = StatUtils.percentile(iterValues, 50);
				medians[models.indexOf(optmodel)] = median;
			}
			writer.append(String.format("%d\t%s\n", exnodes, StringUtils.join(medians, '\t')));
		}
		writer.close();
	}
	
	
	/********************************************************************************
	 * Model resolution with respect to the number of opnodes	 	
	 ********************************************************************************/
	@Test
	public void expROPNode() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {	
		
		int exnodes = Experiments.Resolution.R_OPNode.EXNODES;
		int opmin 	= Experiments.Resolution.R_OPNode.OPMIN;
		int opmax 	= Experiments.Resolution.R_OPNode.OPMAX;
		int oppas 	= Experiments.Resolution.R_OPNode.OPPAS;			
		UNIT unit 	= Experiments.Resolution.R_OPNode.MEASURE;
		
		String title = String.format("R-OPNode - exnodes:%d, opnodes[%d,%d]", exnodes, opmin, opmax);
		Path path = Paths.get(String.format("%s/%s.txt", Experiments.RESULTS_DIR, title));
		BufferedWriter writer = Files.newBufferedWriter(path);
		String header = String.format("OPNodes\t%s\n", models.stream().map(model -> model.getSimpleName()).collect(Collectors.joining("\t")));
		writer.write(header);
		
		Architecture arc = Experiments.Resolution.R_OPNode.arc();
		
		double iterValues[] = new double[repts];		
		double medians[]	= new double[models.size()];
			
		for (int opnodes = opmin; opnodes <= opmax; opnodes += oppas) {											
			Application app = Experiments.Resolution.R_OPNode.app(opnodes);	
			for (Class<?> optmodel : models) {				
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
							iterValues[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
						else if (unit == UNIT.SECOND)
							iterValues[rept - 1] = (end.toEpochMilli() - start.toEpochMilli()) / 1000.0;
					}					
				}
				double median = StatUtils.percentile(iterValues, 50);
				medians[models.indexOf(optmodel)] = median;
			}
			writer.append(String.format("%d\t%s\n", opnodes, StringUtils.join(medians, '\t')));
		}
		writer.close();
	}

	
	/********************************************************************************
	 * Model resolution with respect to the opnodes pinnability factor 		
	 ********************************************************************************/
	@Test
	public void expRPINFactor() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {	
		
		int exnodes = Experiments.Resolution.R_PINFactor.EXNODES;
		int opnodes = Experiments.Resolution.R_PINFactor.OPNODES;
		double pinmin = Experiments.Resolution.R_PINFactor.PINMIN;
		double pinmax = Experiments.Resolution.R_PINFactor.PINMAX;
		double pinpas = Experiments.Resolution.R_PINFactor.PINPAS;			
		UNIT unit 	= Experiments.Resolution.R_PINFactor.MEASURE;
		
		String title = String.format("R-PINFactor - exnodes:%d, opnodes:%d, pinfact[%.2f,%.2f]", exnodes, opnodes, pinmin, pinmax);
		Path path = Paths.get(String.format("%s/%s.txt", Experiments.RESULTS_DIR, title));
		BufferedWriter writer = Files.newBufferedWriter(path);
		String header = String.format("PINFactor\t%s\n", models.stream().map(model -> model.getSimpleName()).collect(Collectors.joining("\t")));
		writer.write(header);
		
		Architecture arc = Experiments.Resolution.R_PINFactor.arc();
		
		double iterValues[] = new double[repts];	
		double medians[]	= new double[models.size()];
			
		for (double pinfact = pinmin; pinfact <= pinmax; pinfact += pinpas) {											
			Application app = Experiments.Resolution.R_PINFactor.app(arc, pinfact);
			for (Class<?> optmodel : models) {				
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
							iterValues[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
						else if (unit == UNIT.SECOND)
							iterValues[rept - 1] = (end.toEpochMilli() - start.toEpochMilli()) / 1000.0;
					}					
				}
				double median = StatUtils.percentile(iterValues, 50);
				medians[models.indexOf(optmodel)] = median;
			}
			writer.append(String.format("%.2f\t%s\n", pinfact, StringUtils.join(medians, '\t')));
		}
		writer.close();
	}

	
	/********************************************************************************
	 * Model resolution with respect to the opnodes diversity factor 	
	 ********************************************************************************/
	@Test
	public void expRDIVFactor() throws ModelException, SolverException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {	
		
		int exnodes = Experiments.Resolution.R_DIVFactor.EXNODES;
		int opnodes = Experiments.Resolution.R_DIVFactor.OPNODES;
		double divmin = Experiments.Resolution.R_DIVFactor.DIVMIN;
		double divmax = Experiments.Resolution.R_DIVFactor.DIVMAX;
		double divpas = Experiments.Resolution.R_DIVFactor.DIVPAS;			
		UNIT unit 	= Experiments.Resolution.R_DIVFactor.MEASURE;
		
		String title = String.format("R-DIVFactor - exnodes:%d, opnodes:%d, divfact[%.2f,%.2f]", exnodes, opnodes, divmin, divmax);
		Path path = Paths.get(String.format("%s/%s.txt", Experiments.RESULTS_DIR, title));
		BufferedWriter writer = Files.newBufferedWriter(path);
		String header = String.format("DIVFactor\t%s\n", models.stream().map(model -> model.getSimpleName()).collect(Collectors.joining("\t")));
		writer.write(header);
		
		double iterValues[] = new double[repts];
		double values[]	= new double[models.size()];
			
		for (double divfact = divmin; divfact <= divmax; divfact += divpas) {											
			Architecture arc = Experiments.Resolution.R_DIVFactor.arc(divfact);
			Application app = Experiments.Resolution.R_DIVFactor.app(divfact);
			for (Class<?> optmodel : models) {				
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
							iterValues[rept - 1] = end.toEpochMilli() - start.toEpochMilli();
						else if (unit == UNIT.SECOND)
							iterValues[rept - 1] = (end.toEpochMilli() - start.toEpochMilli()) / 1000.0;
					}					
				}
				double median = StatUtils.percentile(iterValues, 50);
				values[models.indexOf(optmodel)] = median;
			}
			writer.append(String.format("%.2f\t%s\n", divfact, StringUtils.join(values, '\t')));
		}
		writer.close();
	}

}
