package control.solver;

import control.exceptions.SolverException;
import model.placement.Report;
import model.placement.optmodel.OPPModel;

public abstract class AbstractOPPSolver implements OPPSolver {
	
	public final boolean DBG = false;

	public AbstractOPPSolver() {}
	
	@Override 
	public abstract boolean solve(OPPModel model);

	@Override
	public abstract Report solveAndReport(OPPModel model) throws SolverException;
	
}
