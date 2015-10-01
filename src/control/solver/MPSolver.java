package control.solver;

import control.exceptions.SolverException;
import ilog.concert.IloException;
import model.placement.Report;
import model.placement.optmodel.OPPModel;

public class MPSolver extends AbstractOPPSolver {

	public MPSolver() throws SolverException {
		super();
	}
	
	@Override
	public Report solve(OPPModel model) throws SolverException {
		Report report = null;
		double start, end;
		
		try {
			start = model.getCPlex().getCplexTime();
			if (model.getCPlex().solve()) {
				System.out.println("SOLVED!");
				end = model.getCPlex().getCplexTime();
				report = new Report(model, start, end);
			}				
		} catch (IloException exc) {
			throw new SolverException("Error while solving model: " + exc.getMessage());
		}
		
		return report;
	}

}
