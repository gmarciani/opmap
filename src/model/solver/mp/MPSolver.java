package model.solver.mp;

import control.exceptions.SolverException;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import model.optmodel.OPPModel;
import model.solver.OPPSolver;

public class MPSolver implements OPPSolver {
	
	private IloCplex cplex;
	private OPPModel model;

	public MPSolver() throws SolverException {
		try {
			this.cplex = new IloCplex();
		} catch (IloException exc) {
			throw new SolverException("Error while initiating CPlex: " + exc.getMessage());
		}
	}
	
	@Override
	public IloCplex getCPlex() {
		return this.cplex;
	}

	@Override
	public OPPModel getModel() {
		return this.model;
	}

}
