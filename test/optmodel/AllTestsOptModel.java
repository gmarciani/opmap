package optmodel;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestAlternative.class, 
	TestRestricted.class, 
	TestStandard.class 
})
public class AllTestsOptModel {

}
