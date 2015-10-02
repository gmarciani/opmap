package application;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import model.application.Application;
import model.application.ApplicationFactory;
import sample.Default;

public class TestApplicationFactory {
	
	@Rule public TestName name = new TestName();
	
	@Before
	public void testInfo() {
		System.out.println("\n/********************************************************************************");
		System.out.println(" * TEST: " + this.getClass().getSimpleName() + " " + name.getMethodName());
		System.out.println(" ********************************************************************************/\n");
	}

	@Test
	public void byNodes() {
		ApplicationFactory appFactory = new ApplicationFactory();
		int opnodes = Default.RNDOPNODES;
		
		Application app = appFactory.setName("Sample Application")
									.setDescription("Created randomly with opnodes=" + opnodes)
									.setNodes(opnodes)
									.create();
		
		System.out.println(app.toPrettyString());
	}

}
