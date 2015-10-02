package model.placement.variable;

import java.util.HashMap;

import commons.Quadruple;
import ilog.concert.IloModeler;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplexModeler;

public class AbstractPlacementY extends HashMap<Quadruple<Integer, Integer, Integer, Integer>, IloNumVar> implements PlacementY {

	private static final long serialVersionUID = 8522906130382682450L;
	
	protected IloModeler modeler;
	
	public AbstractPlacementY() {
		super();
		this.modeler = new IloCplexModeler();
	}

	@Override
	public IloNumVar get(final int i, final int j, final int u, final int v) {
		return super.get(new Quadruple<Integer, Integer, Integer, Integer>(i, j, u, v));
	}
	
	@Override
	public String toString() {
		return "Y(" + super.toString() + ")";
	}

}
