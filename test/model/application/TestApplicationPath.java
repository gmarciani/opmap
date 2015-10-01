package model.application;

import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import control.exceptions.ModelException;
import model.application.Application;
import model.application.operator.OPNode;
import model.application.operator.OPPath;
import model.application.operator.OPRole;

public class TestApplicationPath {

	@Test 
	public void aPath() throws ModelException {
		Application app;
		
		app = getSampleApp();
		
		OPNode source = app.getSources().stream().findAny().get();
		
		Set<OPPath> paths = app.getOperationalPaths(source);
		
		System.out.println(paths);
		
		for (OPPath oppath : paths)
			System.out.println(oppath.toPrettyString());
		
		for (OPPath oppath : paths) {
			String str = "#oppath# nodes:";
			Iterator<OPNode> iter = oppath.getNodes().iterator();
			while (iter.hasNext()) {
				OPNode opnode = iter.next();
				str += opnode.getId();
				if (iter.hasNext())
					str += "|";
			}
			System.out.println(str);
		}
	}
	
	private static Application getSampleApp() throws ModelException {
		Application app = new Application("Sample DSP Application", "Created manually");
		
		OPNode node1 = new OPNode(0, OPRole.SRC, "prod", x -> new Long(1000), 1, 1.0);
		OPNode node2 = new OPNode(1, OPRole.PIP, "selection1", x -> x/2, 1, 1.0);
		OPNode node3 = new OPNode(2, OPRole.PIP, "selection2", x -> x/2, 1, 1.0);
		OPNode node4 = new OPNode(3, OPRole.SNK, "cons1", x -> new Long(1), 1, 1.0);
		OPNode node5 = new OPNode(4, OPRole.SNK, "cons2", x -> new Long(1), 1, 1.0);
				
		app.addOperational(node1);
		app.addOperational(node2);
		app.addOperational(node3);
		app.addOperational(node4);
		app.addOperational(node5);
		
		app.addStream(node1, node2);
		app.addStream(node1, node3);
		app.addStream(node2, node3);
		app.addStream(node2, node4);
		app.addStream(node3, node4);
		app.addStream(node3, node5);
		
		return app;
	}

}
