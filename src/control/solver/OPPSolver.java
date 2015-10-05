package control.solver;

import control.exceptions.SolverException;
import model.placement.Report;
import model.placement.optmodel.OPPModel;

public interface OPPSolver {
	
	public Report solve(OPPModel model) throws SolverException;

}
