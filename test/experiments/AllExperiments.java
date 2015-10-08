package experiments;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	ExperimentModelCreation.class,
	TestExperimentStandard.class,
	TestExperimentRestricted.class,
	TestExperimentConservative.class,
	TestExperimentAlternative.class
})
public class AllExperiments {

}
