package model.placement.variable.restricted;

import java.util.Set;

import commons.Quadruple;
import ilog.concert.IloException;
import model.application.dstream.DStream;
import model.architecture.link.Link;
import model.placement.variable.AbstractPlacementY;

public class RestrictedPlacementY extends AbstractPlacementY {

	private static final long serialVersionUID = 7227526215556119890L;

	public RestrictedPlacementY(Set<DStream> dstreams, Set<Link> links) throws IloException {
		super();
		for (DStream dstream : dstreams) {
			for (Link link : links) {
				if (!dstream.isPinnable(link))
					continue;
				int i = dstream.getSrc().getId();
				int j = dstream.getDst().getId();
				int u = link.getSrc().getId();
				int v = link.getDst().getId();
				super.put(new Quadruple<Integer, Integer, Integer, Integer>(i, j, u, v), super.modeler.boolVar("Y[" + i + "][" + j + "][" + u + "][" + v + "]"));
			}
		}
	}

}
