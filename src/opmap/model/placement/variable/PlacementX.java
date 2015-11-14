package opmap.model.placement.variable;

import ilog.concert.IloNumVar;

public interface PlacementX {
	
	public IloNumVar get(final int i, final int u);
	
	public IloNumVar[][] getVector();

}
