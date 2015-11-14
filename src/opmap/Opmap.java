package opmap;

import opmap.control.exception.ModelException;
import opmap.control.exception.SolverException;
import opmap.control.solver.MPSolver;
import opmap.control.solver.OPPSolver;
import opmap.model.application.Application;
import opmap.model.architecture.Architecture;
import opmap.model.placement.Report;
import opmap.model.placement.optmodel.OPPModel;
import opmap.model.placement.optmodel.cplex.OPPStandard;

public class Opmap {

	public static void main(String[] args) throws ModelException, SolverException {
		
		Application app = new Application("Sample DSP Application");
		Architecture arc = new Architecture("Sample Distributed Architecture");
		
		OPPModel model = new OPPStandard(app, arc);
		
		OPPSolver solver = new MPSolver();
		
		Report report = solver.solveAndReport(model);

		System.out.println(report);
		
	}

}
