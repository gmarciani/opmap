package model.optmodel;

import java.util.Collection;
import java.util.List;

import control.exceptions.ModelException;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.concert.IloObjective;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;
import model.application.Application;
import model.architecture.Architecture;

public abstract class AbstractOPPModel implements OPPModel {
	
	private Application app;
	private Architecture arc;	
	
	private IloNumVar[][] X;
	private IloNumVar[][][][] Y;
	
	protected List<IloNumVar> variables;
	protected List<IloRange> constraints;
	protected IloObjective objective;
	protected IloCplex cplex;

	public AbstractOPPModel(Application app, Architecture arc) throws ModelException {
		super();
		this.setApplication(app);
		this.setArchitecture(arc);
		this.compile(this.app, this.arc);
	}
	
	public AbstractOPPModel() {}
	
	@Override
	public Application getApplication() {
		return this.app;
	}
	
	private void setApplication(Application app) {
		this.app = app;
	}
	
	@Override
	public Architecture getArchitecture() {
		return this.arc;
	}
	
	private void setArchitecture(Architecture arc) {
		this.arc = arc;
	}
	
	@Override
	public abstract void compile(Application app, Architecture arc) throws ModelException;

	public IloNumVar getXVariable(long id, long id2) {
		// TODO Auto-generated method stub
		return null;
	}

	public IloNumVar getYVariable(long i, long j, long u, long v) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<IloNumVar> getVariables() {
		return this.variables;
	}
	
	protected void setVariables(List<IloNumVar> variables) {
		this.variables.clear();
		this.variables.addAll(variables);
	}
	
	public List<IloRange> getConstraints() {
		return this.constraints;
	}
	
	protected void setConstraints(Collection<IloRange> constraints) {
		this.constraints.clear();
		this.constraints.addAll(constraints);
	}
	
	public IloObjective getObjective() {
		return this.objective;
	}
	
	protected void setObjective(IloObjective objective) {
		this.objective = objective;
	}
	
	public IloCplex getCPlexModel() {
		return this.cplex;
	}
	
	protected void compile() throws ModelException {
		for (IloNumVar variable : this.getVariables()) {
			try {
				this.cplex.add(variable);
			} catch (IloException exc) {
				throw new ModelException("Error while compiling variables: " + exc.getMessage());
			}
		}			
		
		for (IloRange constraint : this.getConstraints()) {
			try {
				this.cplex.addRange(constraint.getLB(), constraint.getExpr(), constraint.getUB());
			} catch (IloException exc) {
				throw new ModelException("Error while compiling constraint: " + exc.getMessage());
			}
			
		}
		
		try {
			this.cplex.addObjective(this.getObjective().getSense(), this.getObjective().getExpr());
		} catch (IloException exc) {
			throw new ModelException("Error while compiling objective: " + exc.getMessage());
		}				
	}

	public IloNumVar[][] getX() {
		return X;
	}

	public void setX(IloNumVar[][] x) {
		X = x;
	}

	public IloNumVar[][][][] getY() {
		return Y;
	}

	public void setY(IloNumVar[][][][] y) {
		Y = y;
	}

}
