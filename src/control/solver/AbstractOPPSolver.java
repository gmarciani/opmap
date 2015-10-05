package control.solver;

import control.exceptions.SolverException;
import model.placement.Report;
import model.placement.optmodel.OPPModel;

public abstract class AbstractOPPSolver implements OPPSolver {
	
	public final boolean DBG = false;

	public AbstractOPPSolver() {}

	@Override
	public abstract Report solve(OPPModel model) throws SolverException;
	
}
