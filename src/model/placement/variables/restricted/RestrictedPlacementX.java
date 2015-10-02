package model.placement.variables.restricted;

import java.util.Set;

import ilog.concert.IloException;
import ilog.cplex.IloCplexModeler;
import model.application.operator.OPNode;
import model.architecture.node.EXNode;
import model.commons.Pair;
import model.placement.variables.AbstractPlacementX;

public class RestrictedPlacementX extends AbstractPlacementX {

	private static final long serialVersionUID = 3153857530306257405L;

	public RestrictedPlacementX(Set<OPNode> opnodes, Set<EXNode> exnodes) throws IloException {
		super();
		super.modeler = new IloCplexModeler();
		for (OPNode opnode : opnodes) {
			for (EXNode exnode : opnode.getPinnables()) {
				int i = opnode.getId();
				int u = exnode.getId();
				super.put(new Pair<Integer, Integer>(opnode.getId(), exnode.getId()), super.modeler.boolVar("X[" + i + "][" + u + "]"));
			}				
		}	
	}

}
