package control.solver;

import control.exceptions.SolverException;
import model.placement.Report;
import model.placement.optmodel.OPPModel;

public interface OPPSolver {
	
	public boolean solve(OPPModel model);
	
	public Report solveAndReport(OPPModel model) throws SolverException;

}
