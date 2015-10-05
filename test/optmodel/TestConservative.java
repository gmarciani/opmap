package optmodel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import control.exceptions.ModelException;
import control.exceptions.SolverException;
import control.solver.OPPSolver;
import control.solver.mp.MPSolver;
import model.application.Application;
import model.architecture.Architecture;
import model.placement.Report;
import model.placement.optmodel.OPPModel;
import model.placement.optmodel.cplex.OPPConservative;
import sample.SampleApplication;
import sample.SampleArchitecture;

public class TestConservative {

	@Rule public TestName name = new TestName();
	
	@Before
	public void testInfo() {
		System.out.println("\n/********************************************************************************");
		System.out.println(" * TEST: " + this.getClass().getSimpleName() + " " + name.getMethodName());
		System.out.println(" ********************************************************************************/\n");
	}

	@Test
	public void create() throws ModelException {	
		Application app = SampleApplication.getRandomSample();
		Architecture arc = SampleArchitecture.getRandomSample();
		
		OPPModel model = new OPPConservative(app, arc);
		
		System.out.println(model);
		
		model.getCPlex().end();
	}
	
	@Test
	public void solve() throws ModelException, SolverException {
		Application app = SampleApplication.getRandomSample();
		Architecture arc = SampleArchitecture.getRandomSample();
		
		System.out.println(app.toPrettyString());		
		System.out.println(arc.toPrettyString());
		
		OPPModel model = new OPPConservative(app, arc);
		
		OPPSolver solver = new MPSolver();
		
		Report report = solver.solve(model);

		if (report != null)
			System.out.println(report.toPrettyString());
		else 
			System.out.println(model.getName() + " UNSOLVABLE");	
		
		model.getCPlex().end();
	}

}
