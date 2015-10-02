package optmodel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import control.exceptions.ModelException;
import control.exceptions.SolverException;
import control.solver.MPSolver;
import control.solver.OPPSolver;
import model.application.Application;
import model.architecture.Architecture;
import model.placement.Report;
import model.placement.optmodel.OPPModel;
import model.placement.optmodel.standard.OPPRestricted;
import sample.SampleApplication;
import sample.SampleArchitecture;

public class TestRestricted {

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
		
		OPPModel model = new OPPRestricted(app, arc);
		
		System.out.println(model);
	}
	
	@Test
	public void solve() throws ModelException, SolverException {
		Application app = SampleApplication.getRandomSample();
		Architecture arc = SampleArchitecture.getRandomSample();
		
		OPPModel model = new OPPRestricted(app, arc);
		
		OPPSolver solver = new MPSolver();
		
		Report report = solver.solve(model);

		System.out.println(report);		
	}

}
