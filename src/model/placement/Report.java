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
		IloNumVar X[][] = this.model.getPlacementX().getVector();	
		String solution = "";
		for (int i = 0; i < X.length; i++) {
			for (int u = 0; u < X[i].length; u++) {
				IloNumVar var = X[i][u];
				try {
					if (this.model.getCPlex().getValue(var) == 1)
						solution += "opnode " + i + " placed on exnode " + u + "\n";
				} catch (IloException exc) {
					solution += "ERROR for opnode " + i + " on exnode " + u + ": " + exc.getMessage() + "\n";
				}
			}
		}
		
		String str = String.format("#report#\nmodel:%s\ndate:%s\nelapsed:%d (ms)\nstatus:%s\nobjective:%f\napp:%s (%s)\narc:%s (%s)\nsolution:\n%s",
				this.getName(),
				this.getDate(),
				this.getElapsedMillis(),
				this.getStatus(),
				this.getObjectiveValue(),
				this.model.getApplication().getName(),
				this.model.getApplication().getDescription(),
				this.model.getArchitecture().getName(),
				this.model.getArchitecture().getDescription(),
				solution);
		
		return str;
	}
	
	@Override 
	public String toString() {
		String str = String.format("Report(model:%s|date:%s|elapsed:%f (ms)|status:%s|obj:%f)",
				this.getName(),
				this.getDate(),
				this.getElapsedMillis(),
				this.getStatus(),
				this.getObjectiveValue());
		
		return str;
	}

}
