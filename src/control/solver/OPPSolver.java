package control.solver;

import control.exceptions.SolverException;
import ilog.cplex.IloCplex;
import model.placement.Report;
import model.placement.optmodel.OPPModel;

public interface OPPSolver {
	
	public IloCplex getCPlex();
	
	public OPPModel getModel();
	
	public Report solve(OPPModel model) throws SolverException;

}
