package architecture;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestArchitectureCreation.class,
	TestArchitectureFactory.class
})
public class AllTestsArchitecture {

}
