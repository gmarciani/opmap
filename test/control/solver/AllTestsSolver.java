package control.solver;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import experiment.TestStandardModelExperiment;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestStandardModel.class ,
	TestAlternativeModel.class,
	TestStandardModelExperiment.class
})
public class AllTestsSolver {

}
