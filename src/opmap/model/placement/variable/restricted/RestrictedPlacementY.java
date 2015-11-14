package opmap.model.placement.variable.restricted;

import ilog.concert.IloException;
import opmap.model.application.Application;
import opmap.model.application.DStream;
import opmap.model.architecture.Architecture;
import opmap.model.architecture.Link;
import opmap.model.placement.variable.AbstractPlacementY;

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
