package model.placement.variables;

import ilog.concert.IloNumVar;

public interface PlacementY {
	
	public IloNumVar get(final int i, final int j, final int u, final int v);

}
