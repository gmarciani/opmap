package model.placement.variable;

import java.io.Serializable;

import ilog.concert.IloException;
import ilog.concert.IloModeler;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplexModeler;
import model.application.Application;
import model.architecture.Architecture;

public class AbstractPlacementX implements PlacementX, Serializable {

	private static final long serialVersionUID = -93260223464486469L;
	
	protected IloModeler modeler;
	protected IloNumVar X[][];
	
	public AbstractPlacementX(Application app, Architecture arc) {
		this.X = new IloNumVar[app.vertexSet().size()][arc.vertexSet().size()];
		this.modeler = new IloCplexModeler();
	}
	
	public void addVariable(final int i, final int u) throws IloException {
		this.X[i][u] = this.modeler.boolVar("X[" + i + "][" + u + "]");
	}
	
	@Override
	public IloNumVar get(final int i, final int u) {
		return this.X[i][u];
	}	

	@Override
	public IloNumVar[][] getVector() {
		return this.X;
	}
	
	@Override
	public String toString() {
		return "X(" + super.toString() + ")";
	}

}
