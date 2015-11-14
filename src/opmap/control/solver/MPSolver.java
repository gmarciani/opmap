package opmap.control.solver;

import java.time.Clock;
import java.time.Instant;

import ilog.concert.IloException;
import opmap.control.exception.SolverException;
import opmap.model.placement.Report;
import opmap.model.placement.optmodel.OPPModel;

public class MPSolver extends AbstractOPPSolver {

	public MPSolver() throws SolverException {
		super();
	}
	
	@Override
	public boolean solve(OPPModel model) {		
		boolean solved = false;
		
		if (!super.DBG)
			model.getCPlex().setOut(null);
		
		try {
			solved = model.getCPlex().solve();			
		} catch (IloException exc) {
			System.err.println("EXCEPTION IN SOLVER: " + exc.getMessage());
			return false;
		}
		
		return solved;
	}
	
	@Override
	public Report solveAndReport(OPPModel model) throws SolverException {
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
			System.err.println("EXCEPTION IN SOLVER: " + exc.getMessage());
		}
		
		return report;
	}

}
