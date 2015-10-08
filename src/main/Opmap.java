package main;

import control.exceptions.ModelException;
import control.exceptions.SolverException;
import control.solver.OPPSolver;
import control.solver.mp.MPSolver;
import model.application.Application;
import model.architecture.Architecture;
import model.placement.Report;
import model.placement.optmodel.OPPModel;
import model.placement.optmodel.cplex.OPPStandard;

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
