package commons;

import control.exceptions.ModelException;
import model.application.Application;
import model.application.ApplicationFactory;
import model.application.operator.OPNode;
import model.application.operator.OPRole;

public final class SampleApplication {

	private SampleApplication() {}
	
	public static Application getDeterministicSample() throws ModelException {
		Application app = new Application("Sample Application", "Created manually");
		
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
	
	public static Application getRandomSample() {
		return getRandomSample(Default.RNDOPNODES);
	}
	
	public static Application getRandomSample(final int opnodes) {
		ApplicationFactory appFactory = new ApplicationFactory();	
		
		Application app = appFactory.setName("Sample Application")
									.setDescription("Created randomly with opnodes=" + opnodes)
									.setNodes(opnodes)
									.create();
		
		return app;
	}

}
