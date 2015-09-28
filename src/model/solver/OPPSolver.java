package model.solver;

import control.exceptions.SolverException;
import ilog.cplex.IloCplex;
import model.optmodel.OPPModel;
import model.report.Report;

public interface OPPSolver {
	
	public IloCplex getCPlex();
	
	public OPPModel getModel();
	
	public Report solve(OPPModel model) throws SolverException;

}
