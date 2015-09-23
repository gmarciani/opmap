package model.optmodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import control.exceptions.ModelException;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.concert.IloObjective;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

public class AbstractCPlexModel implements CPlexModel {
	
	protected List<IloNumVar> variables;
	protected List<IloRange> constraints;
	protected IloObjective objective;
	protected IloCplex cplex;

	public AbstractCPlexModel() {
		try {
			this.cplex = new IloCplex();
		} catch (IloException exc) {
			exc.getMessage();
		}
		
		this.variables = new ArrayList<IloNumVar>();
		this.constraints = new ArrayList<IloRange>();
	}
	
	@Override
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
	
	@Override
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
				this.cplex.addRange(constraint.getLB(), constraint.getExpr(), constraint.getUB(), constraint.getName());
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
	
	@Override 
	public String toString() {
		try {
			return "Objetive:" + this.getObjective().getSense().toString() + " " + this.getObjective().getExpr().toString() + ";" +
				   "Constraints:" + this.getConstraints().toString() + ";" + 
					"Variables:" + this.getVariables().toString();
		} catch (IloException exc) {
			System.err.println(exc.getMessage());
			return null;
		}		
	}

}
