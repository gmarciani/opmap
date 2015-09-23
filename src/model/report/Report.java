package model.report;

import java.util.ArrayList;
import java.util.List;

import ilog.cplex.IloCplex.Status;

public class Report {
	
	private Status status;
	private double objectiveValue;
	private List<Double> solutions;	
	
	public Report(Status status, double objectiveValue, List<Double> solutions) {
		this.setStatus(status);
		this.setObjectiveValue(objectiveValue);		
		this.setSolutions(solutions);		
	}
	
	public Report(Status status, double objectiveValue, double[] solutions) {
		this.setStatus(status);
		this.setObjectiveValue(objectiveValue);
		this.setSolutions(solutions);		
	}
	
	public Report() {
		this.setStatus(null);
		this.setObjectiveValue(0.0);
		this.setSolutions(new ArrayList<Double>());				
	}	
	
	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public double getObjectiveValue() {
		return this.objectiveValue;
	}
	
	public void setObjectiveValue(double objectiveValue) {
		this.objectiveValue = objectiveValue;
	}
	
	public List<Double> getSolutions() {
		return this.solutions;
	}
	
	public void setSolutions(List<Double> solutions) {
		this.solutions = solutions;
	}
	
	public void setSolutions(double[] solutions) {
		int n = solutions.length;
		if (!this.solutions.isEmpty())
			this.solutions.clear();
		for (int i = 0; i < n; i++)
			this.solutions.add(solutions[i]);
	}			
	
	@Override public String toString() {
		return "Status: " + this.getStatus() + "\n" +
			   "Objective Value: " + this.objectiveValue + "\n" + 			   
			   "Solutions: " + this.getSolutions().toString() + "\n";
	}

}
