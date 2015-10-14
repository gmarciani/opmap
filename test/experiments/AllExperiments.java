package experiments;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	ExperimentCompilation.class,
	ExperimentResolution.class
})
public class AllExperiments {

}
