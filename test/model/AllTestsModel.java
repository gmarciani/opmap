package model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestApplicationCreation.class,
	TestArchitectureCreation.class,
	TestApplicationPath.class
})
public class AllTestsModel {

}
