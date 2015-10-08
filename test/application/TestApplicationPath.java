package application;

import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import control.exceptions.ModelException;
import model.application.Application;
import model.application.opnode.OPPath;
import sample.SampleApplication;

public class TestApplicationPath {
	
	@Rule 
	public TestName name = new TestName();
	
	@Before
	public void testInfo() {
		System.out.println("\n/********************************************************************************");
		System.out.println(" * TEST: " + this.getClass().getSimpleName() + " " + name.getMethodName());
		System.out.println(" ********************************************************************************/\n");
	}

	@Test 
	public void allPaths() throws ModelException {
		Application app = SampleApplication.deterministic();
		
		System.out.println(app.toPrettyString());
		
		Set<OPPath> paths = app.getAllOperationalPaths();
		
		for (OPPath oppath : paths)
			System.out.println(oppath.toPrettyString());
		
	}	

}
