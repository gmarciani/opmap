package model.placement.optmodel;

import control.exceptions.ModelException;
import ilog.cplex.IloCplex;
import model.application.Application;
import model.architecture.Architecture;
import model.placement.variable.PlacementX;
import model.placement.variable.PlacementY;

public interface OPPModel {
	
	public String getName();
	
	public Application getApplication();
	
	public Architecture getArchitecture();
	
	public PlacementX getPlacementX();
	
	public PlacementY getPlacementY();
	
	public IloCplex getCPlex();

	public void compile(Application app, Architecture arc) throws ModelException;
	
}
