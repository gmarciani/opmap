package control;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import control.plotter.AllTestsPlotter;
import control.solver.AllTestsSolver;

@RunWith(Suite.class)
@SuiteClasses({
	AllTestsPlotter.class,
	AllTestsSolver.class
})
public class AllTestsControl {

}
