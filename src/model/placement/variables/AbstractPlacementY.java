package model.placement.variables;

import java.util.HashMap;

import ilog.concert.IloModeler;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplexModeler;
import model.commons.Quadruple;

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

}
