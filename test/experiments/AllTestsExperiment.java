package experiments;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TestExperimentModelCreation.class,
	TestExperimentStandard.class,
	TestExperimentRestricted.class,
	TestExperimentConservative.class,
	TestExperimentAlternative.class
})
public class AllTestsExperiment {

}
