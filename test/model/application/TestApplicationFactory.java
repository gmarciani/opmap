package model.application;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

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
		
		Application app = appFactory.setName("Random Application")
									.setDescription("Created randomly")
									.setNodes(5)
									.create();
		
		System.out.println(app.toPrettyString());
	}

}
