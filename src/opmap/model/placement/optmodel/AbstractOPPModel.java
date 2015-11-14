package opmap.model.placement.optmodel;

import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import opmap.control.exception.ModelException;
import opmap.model.application.Application;
import opmap.model.architecture.Architecture;
import opmap.model.placement.variable.PlacementX;
import opmap.model.placement.variable.PlacementY;

public abstract class AbstractOPPModel implements OPPModel {
	
	protected IloCplex cplex;	
	protected Application app;
	protected Architecture arc;	
	
	protected PlacementX X;
	protected PlacementY Y;

	public AbstractOPPModel(String name, Application app, Architecture arc) throws ModelException {
		this.setApplication(app);
		this.setArchitecture(arc);
		try {
			this.cplex = new IloCplex();
			this.cplex.setName(name);
		} catch (IloException exc) {
			throw new ModelException("Error while creating model: " + exc.getMessage());
		}
	}
	
	@Override
	public String getName() {
		return this.getCPlex().getName();
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
	public PlacementX getPlacementX() {
		return this.X;
	}
	
	@Override
	public PlacementY getPlacementY() {
		return this.Y;
	}
	
	@Override
	public IloCplex getCPlex() {
		return this.cplex;
	}
	
	@Override
	public abstract void compile(Application app, Architecture arc) throws ModelException;
	
	@Override
	public String toString() {
		return "OPPModel(" + 
			   "name:" + this.getCPlex().getName() + "|" +
			   "objective:" + this.getCPlex().getObjective() + ")";			
	}

}
