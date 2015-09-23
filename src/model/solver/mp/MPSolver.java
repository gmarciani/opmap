package model.solver.mp;

import control.exceptions.SolverException;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import model.optmodel.OPPCPlexModel;
import model.report.Report;
import model.solver.OPPSolver;

public class MPSolver implements OPPSolver {
	
	private MPSolverParams params;
	
	public MPSolver(MPSolverParams params) {
		this.setParams(params);
	}

	public MPSolver() {
		this.setParams(MPSolverParams.getDefault());
	}
	
	public MPSolverParams getParams() {
		return this.params;
	}

	public void setParams(MPSolverParams params) {
		this.params = params;
	}

	@Override
	public Report solve(OPPCPlexModel model) throws SolverException {
		Report report = new Report();
		IloCplex cplex;
		
		try {
			cplex = new IloCplex();
		} catch (IloException exc) {
			throw new SolverException("Error while initiating CPlex: " + exc.getMessage());
		}		
		
		
		try {
			cplex.setModel(model.getCPlexModel());
		} catch (IloException exc) {
			throw new SolverException("Error while copying model: " + exc.getMessage());
		}
		
		try {
			if (cplex.solve()) {
				report.setStatus(cplex.getStatus());
				report.setObjectiveValue(cplex.getObjValue());
				report.setSolutions(cplex.getValues((IloNumVar[])model.getVariables().toArray()));
			}
		} catch (IloException exc) {
			throw new SolverException("Error while solving model: " + exc.getMessage());
		}
		
		return report;
	}

	

}
