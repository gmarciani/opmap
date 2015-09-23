package model.solver;

import control.exceptions.SolverException;
import model.optmodel.OPPCPlexModel;
import model.report.Report;

public interface OPPSolver {
	
	public Report solve(OPPCPlexModel model) throws SolverException;

}
