package model.placement.variable.restricted;

import java.util.Set;

import commons.Pair;
import ilog.concert.IloException;
import ilog.cplex.IloCplexModeler;
import model.application.opnode.OPNode;
import model.architecture.exnode.EXNode;
import model.placement.variable.AbstractPlacementX;

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
