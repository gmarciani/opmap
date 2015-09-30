package control.solver;

import control.exceptions.SolverException;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import model.placement.optmodel.OPPModel;
import model.placement.report.Report;

public abstract class AbstractOPPSolver implements OPPSolver {
	
	private IloCplex cplex;
	private OPPModel model;

	public AbstractOPPSolver() throws SolverException {
		try {
			this.setCPlex(new IloCplex());
		} catch (IloException exc) {
			throw new SolverException("Error while creating MPSolver " + exc.getMessage());
		}
	}
	
	@Override
	public IloCplex getCPlex() {
		return this.cplex;
	}
	
	protected void setCPlex(IloCplex cplex) {
		this.cplex = cplex;
	}

	@Override
	public OPPModel getModel() {
		return this.model;
	}
	
	protected void setModel(OPPModel model) {
		this.model = model;
	}

	@Override
	public abstract Report solve(OPPModel model) throws SolverException;
}
