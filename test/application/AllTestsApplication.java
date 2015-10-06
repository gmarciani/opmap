package application;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestApplicationCreation.class, 
	TestApplicationPath.class
})
public class AllTestsApplication {

}
