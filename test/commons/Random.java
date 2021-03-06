package commons;

import static org.junit.Assert.*;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import commons.GMath;

public class Random {
	
	@Rule 
	public TestName name = new TestName();
	
	@Before
	public void testInfo() {
		System.out.println("\n/********************************************************************************");
		System.out.println(" * TEST: " + this.getClass().getSimpleName() + " " + name.getMethodName());
		System.out.println(" ********************************************************************************/\n");
	}

	@Test
	public void normalDouble() {
		double min = 10.0;
		double max = 100.0;
		double var = 1.0;
		double value;
		int reps = 10000000;
		RandomDataGenerator rnd = new RandomDataGenerator();
		
		for (int rep = 1; rep <= reps; rep++) {
			value = GMath.randomNormal(rnd, min, max, var);
			if (rep%100000 == 0)
				System.out.println(rep);
			if (value < min || value > max) fail("Random value outside interval: " + value + " (rep:" + rep + ")");
		}
	}
	
	@Test
	public void normalInt() {
		double min = 10.0;
		double max = 100.0;
		double var = 1.0;
		double value;
		int reps = 10000000;
		RandomDataGenerator rnd = new RandomDataGenerator();
		
		for (int rep = 1; rep <= reps; rep++) {
			value = GMath.randomNormalInt(rnd, min, max, var);
			if (rep%100000 == 0)
				System.out.println(rep);
			if (value < min || value > max) fail("Random value outside interval: " + value + " (rep:" + rep + ")");
		}
	}

}
