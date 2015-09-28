package model.solver.mp;

import control.exceptions.SolverException;
import ilog.concert.IloException;
import model.optmodel.OPPModel;
import model.report.Report;
import model.solver.AbstractOPPSolver;

public class MPSolver extends AbstractOPPSolver {

	public MPSolver() throws SolverException {
		super();
	}
	
	@Override
	public Report solve(OPPModel model) throws SolverException {
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
