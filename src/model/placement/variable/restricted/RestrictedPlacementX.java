package model.placement.variable.restricted;

import java.util.Set;
import java.util.stream.Collectors;

import ilog.concert.IloException;
import model.application.Application;
import model.application.opnode.OPNode;
import model.architecture.Architecture;
import model.architecture.exnode.EXNode;
import model.placement.variable.AbstractPlacementX;

public class RestrictedPlacementX extends AbstractPlacementX {

	private static final long serialVersionUID = 3153857530306257405L;

	public RestrictedPlacementX(Application app, Architecture arc) throws IloException {
		super(app, arc);
		for (OPNode opnode : app.vertexSet()) {
			Set<EXNode> admittables = arc.vertexSet().stream().filter(exnode -> opnode.isPinnableOn(exnode)).collect(Collectors.toSet());
			for (EXNode exnode : admittables) {
				int i = opnode.getId();
				int u = exnode.getId();
				super.addVariable(i, u);
			}				
		}	
	}

}
