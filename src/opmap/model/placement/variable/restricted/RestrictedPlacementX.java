package opmap.model.placement.variable.restricted;

import ilog.concert.IloException;
import opmap.model.application.Application;
import opmap.model.application.OPNode;
import opmap.model.architecture.Architecture;
import opmap.model.architecture.EXNode;
import opmap.model.placement.variable.AbstractPlacementX;

public class RestrictedPlacementX extends AbstractPlacementX {

	private static final long serialVersionUID = 3153857530306257405L;

	public RestrictedPlacementX(Application app, Architecture arc) throws IloException {
		super(app, arc);
		for (OPNode opnode : app.vertexSet()) {
			for (EXNode exnode : arc.getPinnableNodes(opnode)) {
				int i = opnode.getId();
				int u = exnode.getId();
				super.addVariable(i, u);
			}				
		}	
	}

}
