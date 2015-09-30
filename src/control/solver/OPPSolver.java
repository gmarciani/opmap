package control.solver;

import control.exceptions.SolverException;
import ilog.cplex.IloCplex;
import model.placement.optmodel.OPPModel;
import model.placement.report.Report;

public interface OPPSolver {
	
	public IloCplex getCPlex();
	
	public OPPModel getModel();
	
	public Report solve(OPPModel model) throws SolverException;

}
