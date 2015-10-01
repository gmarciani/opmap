package model.architecture;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class TestArchitectureFactory {
	
	@Rule public TestName name = new TestName();
	
	@Before
	public void testInfo() {
		System.out.println("\n/********************************************************************************");
		System.out.println(" * TEST: " + this.getClass().getSimpleName() + " " + name.getMethodName());
		System.out.println(" ********************************************************************************/\n");
	}

	@Test
	public void simple() {
		ArchitectureFactory arcFactory = new ArchitectureFactory();
		
		Architecture arc = arcFactory.setName("Random Distributed Architecture")
									 .setDescription("Created randomly")
				 					 .setNodes(5)
				 					 .create();
		
		System.out.println(arc.toPrettyString());
	}

}
