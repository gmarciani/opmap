package model.optmodel;

import control.exceptions.ModelException;
import ilog.cplex.IloCplex;
import model.application.Application;
import model.architecture.Architecture;

public interface OPPModel {
	
	public String getName();
	
	public Application getApplication();
	
	public Architecture getArchitecture();
	
	public IloCplex getCPlex();

	public void compile(Application app, Architecture arc) throws ModelException;
	
}
