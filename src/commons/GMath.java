package commons;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.util.FastMath;

public final class GMath {
	
	public static double randomNormal(RandomDataGenerator rnd, final double min, final double max, final double variance) {
		double value;
		if (variance == 0.0) {
			value = rnd.nextUniform(min, max);
		} else {
			do {
				value = rnd.nextGaussian((max + min)/2, variance);
			} while (value < min || value > max);
		}
		return value;
	}
	
	public static long randomNormalLong(RandomDataGenerator rnd, final double min, final double max, final double variance) {
		long value;
		if (variance == 0.0) {
			value = FastMath.round(rnd.nextUniform(min, max));
		} else {
			do {
				value = FastMath.round(rnd.nextGaussian((max + min)/2, variance));
			} while (value < min || value > max);
			
		}
		return value;
	}
	
	public static int randomNormalInt(RandomDataGenerator rnd, final double min, final double max, final double variance) {
		int value;
		if (variance == 0.0) {
			value = (int) FastMath.round(rnd.nextUniform(min, max));
		} else {
			do {
				value = (int) FastMath.round(rnd.nextGaussian((max + min)/2, variance));
			} while (value < min || value > max);
		}
		return value;
	}

}
