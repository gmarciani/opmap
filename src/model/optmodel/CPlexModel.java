package model.optmodel;

import java.util.List;

import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public interface CPlexModel {
	
	public List<IloNumVar> getVariables();
	
	public IloCplex getCPlexModel();

}
