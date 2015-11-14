package opmap.model.placement.optmodel;

import ilog.cplex.IloCplex;
import opmap.control.exception.ModelException;
import opmap.model.application.Application;
import opmap.model.architecture.Architecture;
import opmap.model.placement.variable.PlacementX;
import opmap.model.placement.variable.PlacementY;

public interface OPPModel {
	
	public String getName();
	
	public Application getApplication();
	
	public Architecture getArchitecture();
	
	public PlacementX getPlacementX();
	
	public PlacementY getPlacementY();
	
	public IloCplex getCPlex();

	public void compile(Application app, Architecture arc) throws ModelException;
	
}
