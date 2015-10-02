package model.placement.variable;

import java.util.HashMap;

import commons.Pair;
import ilog.concert.IloModeler;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplexModeler;

public class AbstractPlacementX extends HashMap<Pair<Integer, Integer>, IloNumVar> implements PlacementX {

	private static final long serialVersionUID = -93260223464486469L;
	
	protected IloModeler modeler;
	
	public AbstractPlacementX() {
		super();
		this.modeler = new IloCplexModeler();
	}
	
	@Override
	public IloNumVar get(final int i, final int u) {
		return super.get(new Pair<Integer, Integer>(i, u));
	}	
	
	@Override
	public String toString() {
		return "X(" + super.toString() + ")";
	}

}
