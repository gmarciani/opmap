package main;

import control.exceptions.ModelException;
import control.exceptions.SolverException;
import model.application.Application;
import model.architecture.Architecture;
import model.optmodel.OPPModel;
import model.optmodel.mp.OPPStandard;
import model.report.Report;
import model.solver.OPPSolver;
import model.solver.mp.MPSolver;

public class Opmap {

	public static void main(String[] args) throws ModelException, SolverException {
		
		Application app = new Application("Sample DSP Application");
		Architecture arc = new Architecture("Sample Distributed Architecture");
		
		OPPModel model = new OPPStandard(app, arc);
		
		OPPSolver solver = new MPSolver();
		
		Report report = solver.solve(model);

		System.out.println(report);

	}

}
