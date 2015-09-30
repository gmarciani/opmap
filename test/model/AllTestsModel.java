package model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import model.application.AllTestsApplication;
import model.architecture.AllTestsArchitecture;
import model.placement.AllTestsPlacement;

@RunWith(Suite.class)
@SuiteClasses({
	AllTestsApplication.class,
	AllTestsArchitecture.class,
	AllTestsPlacement.class
})
public class AllTestsModel {

}
