package architecture;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import model.architecture.Architecture;
import sample.SampleArchitecture;

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
		Architecture arc = SampleArchitecture.getDeterministicSample();
		
		System.out.println(arc.toPrettyString());
	}
	
	@Test
	public void random() {
		Architecture arc = SampleArchitecture.getRandomSample();
		
		System.out.println(arc.toPrettyString());
	}
	
}
