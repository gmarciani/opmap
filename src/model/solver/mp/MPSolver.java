package model.solver.mp;

import control.exceptions.SolverException;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import model.solver.AbstractOPPSolver;

public class MPSolver extends AbstractOPPSolver {

	public MPSolver() throws SolverException {
		try {
			super.setCPlex(new IloCplex());
		} catch (IloException exc) {
			throw new SolverException("Error while creating MPSolver " + exc.getMessage());
		}
	}

}
