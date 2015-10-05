package optmodel;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestStandard.class,
	TestRestricted.class,
	TestConservative.class,
	TestAlternative.class	 
})
public class AllTestsOptModel {

}
