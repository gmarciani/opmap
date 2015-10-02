package architecture;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import model.architecture.Architecture;
import model.architecture.ArchitectureFactory;
import sample.Default;

public class TestArchitectureFactory {
	
	@Rule public TestName name = new TestName();
	
	@Before
	public void testInfo() {
		System.out.println("\n/********************************************************************************");
		System.out.println(" * TEST: " + this.getClass().getSimpleName() + " " + name.getMethodName());
		System.out.println(" ********************************************************************************/\n");
	}

	@Test
	public void byNodes() {
		ArchitectureFactory arcFactory = new ArchitectureFactory();
		int exnodes = Default.RNDEXNODES;
		
		Architecture arc = arcFactory.setName("Random Architecture")
									 .setDescription("Created randomly with exnodes=" + exnodes)
				 					 .setNodes(exnodes)
				 					 .create();
		
		System.out.println(arc.toPrettyString());
	}

}
