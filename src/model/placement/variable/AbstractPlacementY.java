package model.placement.variable;

import java.io.Serializable;
import ilog.concert.IloException;
import ilog.concert.IloModeler;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplexModeler;
import model.application.Application;
import model.architecture.Architecture;

public class AbstractPlacementY implements PlacementY, Serializable {

	private static final long serialVersionUID = 8522906130382682450L;
	
	protected IloModeler modeler;
	protected IloNumVar Y[][][][];
	
	public AbstractPlacementY(Application app, Architecture arc) {
		this.Y = new IloNumVar[app.vertexSet().size()][app.vertexSet().size()][arc.vertexSet().size()][arc.vertexSet().size()];
		this.modeler = new IloCplexModeler();
	}
	
	public void addVariable(final int i, final int j, final int u, final int v) throws IloException {
		this.Y[i][j][u][v] = this.modeler.boolVar("Y[" + i + "][" + j + "][" + u + "][" + v + "]");
	}

	@Override
	public IloNumVar get(final int i, final int j, final int u, final int v) {
		return this.Y[i][j][u][v];
	}
	
	@Override
	public String toString() {
		return "Y(" + super.toString() + ")";
	}

}
