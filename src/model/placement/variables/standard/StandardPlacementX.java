package model.placement.variables.standard;

import java.util.Set;

import ilog.concert.IloException;
import model.application.operator.OPNode;
import model.architecture.node.EXNode;
import model.commons.Pair;
import model.placement.variables.AbstractPlacementX;

public class StandardPlacementX extends AbstractPlacementX {

	private static final long serialVersionUID = -3129577587033542804L;

	public StandardPlacementX(Set<OPNode> opnodes, Set<EXNode> exnodes) throws IloException {
		super();
		for (OPNode opnode : opnodes) {
			for (EXNode exnode : exnodes) {
				int i = opnode.getId();
				int u = exnode.getId();
				super.put(new Pair<Integer, Integer>(opnode.getId(), exnode.getId()), super.modeler.boolVar("X[" + i + "][" + u + "]"));
			}				
		}					
	}

}
