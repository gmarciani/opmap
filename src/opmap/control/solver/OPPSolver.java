package opmap.control.solver;

import opmap.control.exception.SolverException;
import opmap.model.placement.Report;
import opmap.model.placement.optmodel.OPPModel;

public interface OPPSolver {
	
	public boolean solve(OPPModel model);
	
	public Report solveAndReport(OPPModel model) throws SolverException;

}
