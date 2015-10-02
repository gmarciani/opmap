package experiment;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TestStandardExperiment.class,
	TestRestrictedExperiment.class,
	TestAlternativeExperiment.class
})
public class AllTestsExperiment {

}
