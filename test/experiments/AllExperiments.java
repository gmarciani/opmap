package experiments;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	ExperimentRandom.class,
	ExperimentModelCreation.class,
	ExperimentModelResolution.class
})
public class AllExperiments {

}
