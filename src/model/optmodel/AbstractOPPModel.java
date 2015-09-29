package model.optmodel;

import control.exceptions.ModelException;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import model.application.Application;
import model.architecture.Architecture;

public abstract class AbstractOPPModel implements OPPModel {
	
	protected Application app;
	protected Architecture arc;	
	protected IloCplex cplex;

	public AbstractOPPModel(Application app, Architecture arc) throws ModelException {
		this.setApplication(app);
		this.setArchitecture(arc);
		try {
			this.cplex = new IloCplex();
		} catch (IloException exc) {
			throw new ModelException("Error while creating model: " + exc.getMessage());
		}
	}
	
	@Override
	public Application getApplication() {
		return this.app;
	}
	
	protected void setApplication(Application app) {
		this.app = app;
	}
	
	@Override
	public Architecture getArchitecture() {
		return this.arc;
	}
	
	protected void setArchitecture(Architecture arc) {
		this.arc = arc;
	}
	
	@Override
	public IloCplex getCPlex() {
		return this.cplex;
	}
	
	@Override
	public abstract void compile(Application app, Architecture arc) throws ModelException;
	
	@Override
	public String toString() {
		return "OPPModel: " + this.getCPlex().getName() + "\n" + 
			   "objective: " + this.getCPlex().getObjective();
				
	}

}
