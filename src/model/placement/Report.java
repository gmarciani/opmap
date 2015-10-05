package model.placement;

import java.time.Instant;
import java.util.Date;
import control.exceptions.SolverException;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex.Status;
import model.placement.optmodel.OPPModel;

public class Report {
	
	private OPPModel model;
	private Date date;
	private long elapsedSeconds;
	private long elapsedMillis;
	
	public Report(OPPModel model, final Instant start, final Instant end) throws SolverException {		
		this.model = model;		
		this.date = Date.from(start);
		this.elapsedSeconds = end.getEpochSecond() - start.getEpochSecond();
		this.elapsedMillis = end.toEpochMilli() - start.toEpochMilli();
	}	
	
	public String getName() {
		return this.model.getName();
	}
	
	public Date getDate() {
		return this.date;
	}
	
	public Status getStatus() {
		try {
			return this.model.getCPlex().getStatus();
		} catch (IloException e) {
			return null;
		}
	}
	
	public double getObjectiveValue() {
		try {
			return this.model.getCPlex().getObjValue();
		} catch (IloException e) {
			return 0.0;
		}
	}

	public long getElapsedSeconds() {
		return this.elapsedSeconds;
	}
	
	public long getElapsedMillis() {
		return this.elapsedMillis;
	}
	
	public String toPrettyString() {
		String str = "# Report #\n";
		
		str += "mod: " + this.getName() + "\n";
		str += "dat: " + this.getDate() + "\n";
		str += "tim: " + this.getElapsedSeconds() + "s (" + this.getElapsedMillis() + "ms)\n";
		str += "sta: " + this.getStatus() + "\n";
		str += "obj: " + this.getObjectiveValue() + "\n";
		str += "app: " + this.model.getApplication().getName() + "\n";
		str += "\topnodes: " + this.model.getApplication().vertexSet().size() + "\n";
		str += "\tstreams: " + this.model.getApplication().edgeSet().size() + "\n";
		str += "arc: " + this.model.getArchitecture().getName() + "\n";
		str += "\texnodes: " + this.model.getArchitecture().vertexSet().size() + "\n";
		str += "\tlolinks: " + this.model.getArchitecture().edgeSet().size() + "\n";
		str += "X:" + "\n";
		
		IloNumVar X[][] = this.model.getPlacementX().getVector();
		
		for (int i = 0; i < X.length; i++) {
			for (int u = 0; u < X[i].length; u++) {
				IloNumVar var = X[i][u];
				try {
					if (this.model.getCPlex().getValue(var) == 1)
						str += "\topnode " + i + " placed on exnode " + u + "\n";
				} catch (IloException exc) {
					str += "\tERROR for opnode " + i + " on exnode " + u + ": " + exc.getMessage() + "\n";
				}
			}
		}
		
		return str;
	}
	
	@Override 
	public String toString() {
		return "Report(" +
			   "mod:" + this.getName() + "|" +
			   "dat:" + this.getDate() + "|" +
			   "tim:" + this.getElapsedMillis() + "ms|" +
			   "sta:" + this.getStatus() + "|" +
			   "obj:" + this.getObjectiveValue() + ")";
	}

}
