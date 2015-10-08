package sample;

import control.exceptions.GeneratorException;
import model.application.Application;
import model.application.ApplicationGenerator;
import model.application.opnode.OPNode;
import model.application.opnode.OPRole;

public final class SampleApplication {

	private SampleApplication() {}
	
	/********************************************************************************
	 * Deterministic
	 ********************************************************************************/	
	public static Application deterministic() {
		Application app = new Application("Sample Application", "Created manually");
		
		OPNode node1 = new OPNode(0, OPRole.SRC, "src1", x -> Default.OPNODE_SRC_FLOW, 	 Default.OPNODE_RESOURCES, Default.OPNODE_SPEED);
		OPNode node2 = new OPNode(1, OPRole.PIP, "opr11", x -> x * Default.OPNODE_PIP_TRANS, Default.OPNODE_RESOURCES, Default.OPNODE_SPEED);
		OPNode node3 = new OPNode(2, OPRole.PIP, "opr2", x -> x * Default.OPNODE_PIP_TRANS, Default.OPNODE_RESOURCES, Default.OPNODE_SPEED);
		OPNode node4 = new OPNode(3, OPRole.SNK, "snk1", x -> Default.OPNODE_SNK_FLOW, 	 Default.OPNODE_RESOURCES, Default.OPNODE_SPEED);
		OPNode node5 = new OPNode(4, OPRole.SNK, "snk2", x -> Default.OPNODE_SNK_FLOW, 	 Default.OPNODE_RESOURCES, Default.OPNODE_SPEED);
				
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

	
	/********************************************************************************
	 * Random: Uniform distribution of settings
	 ********************************************************************************/	
	public static Application randomUniform() {
		ApplicationGenerator appFactory = new ApplicationGenerator();	
		
		Application app = null;
		try {
			app = appFactory.setName("Sample Application")
										.setDescription("Created randomly (uniform)")
										.setSRC(1)
										.setPIP(3)
										.setSNK(1)
										.setOPNodeConnectivity(0.3, 0.5)
										.setSRCProd(1000.0, 10000.0)
										.setPIPCons(0.3, 0.5)
										.setSNKCons(0.2, 0.4)
										.setOPNodeResources(1, 2)
										.setOPNodeSpeed(1000.0, 2000.0)
										.create();
		} catch (GeneratorException exc) {
			exc.printStackTrace();
		}
		
		return app;
	}
	
	
	/********************************************************************************
	 * Random: Normal distribution of settings (mean, variance)
	 ********************************************************************************/	
	public static Application randomNormal() {
		ApplicationGenerator appFactory = new ApplicationGenerator();
		
		Application app = null;
		try {
			app = appFactory.setName("Sample Application")
										.setDescription("Created randomly (gaussian)")
										.setSRC(1)
										.setPIP(3)
										.setSNK(1)
										.setOPNodeConnectivity(0.3, 0.5, 2.0)
										.setSRCProd(1000.0, 10000.0, 2.0)
										.setPIPCons(0.3, 0.5, 2.0)
										.setSNKCons(0.2, 0.4, 2.0)
										.setOPNodeResources(1, 2, 2.0)
										.setOPNodeSpeed(1000.0, 2000.0, 2.0)
										.create();
		} catch (GeneratorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return app;
	}

}
