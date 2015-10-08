package architecture;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import model.architecture.Architecture;

public class TestArchitectureCreation {
	
	@Rule 
	public TestName name = new TestName();
	
	@Before
	public void testInfo() {
		System.out.println("\n/********************************************************************************");
		System.out.println(" * TEST: " + this.getClass().getSimpleName() + " " + name.getMethodName());
		System.out.println(" ********************************************************************************/\n");
	}

	@Test 
	public void deterministic() {		
		Architecture arc = SampleArchitecture.deterministic();
		
		System.out.println(arc.toPrettyString());
	}
	
	@Test
	public void randomUniform() {
		Architecture arc = SampleArchitecture.randomUniform();
		
		System.out.println(arc.toPrettyString());
	}
	
	@Test
	public void randomNormal() {
		Architecture arc = SampleArchitecture.randomNormal();
		
		System.out.println(arc.toPrettyString());
	}
	
}
