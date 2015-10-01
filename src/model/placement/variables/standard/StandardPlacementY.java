package model.placement.variables.standard;

import java.util.Set;

import ilog.concert.IloException;
import model.application.dstream.DStream;
import model.architecture.link.Link;
import model.commons.Quadruple;
import model.placement.variables.AbstractPlacementY;

public class StandardPlacementY extends AbstractPlacementY {

	private static final long serialVersionUID = 6089632423089597399L;

	public StandardPlacementY(Set<DStream> dstreams, Set<Link> links) throws IloException {
		super();
		for (DStream dstream : dstreams) {
			for (Link link : links) {
				int i = dstream.getSrc().getId();
				int j = dstream.getDst().getId();
				int u = link.getSrc().getId();
				int v = link.getDst().getId();
				super.put(new Quadruple<Integer, Integer, Integer, Integer>(i, j, u, v), super.modeler.boolVar("Y[" + i + "][" + j + "][" + u + "][" + v + "]"));
			}
		}
		
	}

}
