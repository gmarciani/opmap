package model.placement.variable.restricted;

import ilog.concert.IloException;
import model.application.Application;
import model.application.dstream.DStream;
import model.architecture.Architecture;
import model.architecture.link.Link;
import model.placement.variable.AbstractPlacementY;

public class RestrictedPlacementY extends AbstractPlacementY {

	private static final long serialVersionUID = 7227526215556119890L;

	public RestrictedPlacementY(Application app, Architecture arc) throws IloException {
		super(app, arc);
		for (DStream dstream : app.edgeSet()) {
			for (Link link : arc.getPinnablesLinks(dstream)) {
				int i = dstream.getSrc().getId();
				int j = dstream.getDst().getId();
				int u = link.getSrc().getId();
				int v = link.getDst().getId();
				super.addVariable(i, j, u, v);
			}
		}
	}

}
