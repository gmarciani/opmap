package control.solver.mp;

import java.time.Clock;
import java.time.Instant;

import control.exceptions.SolverException;
import control.solver.AbstractOPPSolver;
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
		Clock clk = Clock.systemDefaultZone();
		Instant start, end;
		
		if (!super.DBG)
			model.getCPlex().setOut(null);
		
		try {
			start = clk.instant();
			if (model.getCPlex().solve()) {
				end = clk.instant();
				report = new Report(model, start, end);
			}				
		} catch (IloException exc) {
			System.err.println("EXCEPTION IN SOLVER");
			throw new SolverException("Error while solving model: " + exc.getMessage());
		}
		
		return report;
	}

}
