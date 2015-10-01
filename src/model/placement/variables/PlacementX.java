package model.placement.variables;

import ilog.concert.IloNumVar;

public interface PlacementX {
	
	public IloNumVar get(final int i, final int u);

}
