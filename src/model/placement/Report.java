package model.placement;

import control.exceptions.SolverException;
import ilog.concert.IloException;
import ilog.cplex.IloCplex.Status;
import model.placement.optmodel.OPPModel;

public class Report {
	
	private String name;
	private Status status;
	private double objectiveValue;
	private double time;
	
	public Report(OPPModel model, final double start, final double end) throws SolverException {		
		try {
			this.setName(model.getCPlex().getName());
			this.setStatus(model.getCPlex().getStatus());
			this.setObjectiveValue(model.getCPlex().getObjValue());
			
		} catch (IloException exc) {
			throw new SolverException("Error while initializing report: " + exc.getMessage());
		}						
		
		this.setTime(end - start);		
	}	
	
	public String getName() {
		return this.name;
	}
	
	private void setName(final String name) {
		this.name = name;
	}
	
	public Status getStatus() {
		return this.status;
	}

	public void setStatus(final Status status) {
		this.status = status;
	}
	
	public double getObjectiveValue() {
		return this.objectiveValue;
	}
	
	public void setObjectiveValue(final double objectiveValue) {
		this.objectiveValue = objectiveValue;
	}
	
	public double getTime() {
		return this.time;
	}

	private void setTime(double time) {
		this.time = time;
	}
	
	@Override 
	public String toString() {
		String str =  "Status: " + this.getStatus() + "\n" +
					  "Objective Value: " + this.getObjectiveValue() + "\n" + 
					  "Time: " + this.getTime() + "\n";
		
		return str;
	}

}
