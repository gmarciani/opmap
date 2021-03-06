package opmap.model.placement.variable.standard;


import ilog.concert.IloException;
import opmap.model.application.Application;
import opmap.model.application.OPNode;
import opmap.model.architecture.Architecture;
import opmap.model.architecture.EXNode;
import opmap.model.placement.variable.AbstractPlacementX;

public class StandardPlacementX extends AbstractPlacementX {

	private static final long serialVersionUID = -3129577587033542804L;

	public StandardPlacementX(Application app, Architecture arc) throws IloException {
		super(app, arc);
		for (OPNode opnode : app.vertexSet()) {
			for (EXNode exnode : arc.vertexSet()) {
				int i = opnode.getId();
				int u = exnode.getId();
				super.addVariable(i, u);
			}				
		}					
	}

}
