package main;

import control.exceptions.ModelException;
import control.exceptions.SolverException;
import control.solver.MPSolver;
import control.solver.OPPSolver;
import model.application.Application;
import model.architecture.Architecture;
import model.placement.optmodel.OPPModel;
import model.placement.optmodel.OPPStandard;
import model.placement.report.Report;

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
