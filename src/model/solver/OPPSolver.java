package model.solver;

import control.exceptions.SolverException;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import model.optmodel.OPPModel;
import model.report.Report;

public interface OPPSolver {
	
	public IloCplex getCPlex();
	
	public OPPModel getModel();
	
	default public Report solve(final OPPModel model) throws SolverException {
		Report report = null;
		double start, end;
		
		try {
			start = model.getCPlexModel().getCplexTime();
			if (model.getCPlexModel().solve()) {
				end = model.getCPlexModel().getCplexTime();
				report = new Report(model, start, end);
			}				
		} catch (IloException exc) {
			throw new SolverException("Error while solving model: " + exc.getMessage());
		}
		
		return report;
	}

}
