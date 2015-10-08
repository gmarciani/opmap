package commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.util.FastMath;

public final class GMath {
	
	public static double randomNormal(RandomDataGenerator rnd, final double min, final double max, final double variance) {
		double value;
		
		if (min == max)
			return min;
		
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
		
		if (min == max)
			return FastMath.round(min);
		
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
		
		if (min == max)
			return (int) FastMath.round(min);
		
		if (variance == 0.0) {
			value = (int) FastMath.round(rnd.nextUniform(min, max));
		} else {
			do {
				value = (int) FastMath.round(rnd.nextGaussian((max + min)/2, variance));
			} while (value < min || value > max);
		}
		
		return value;
	}
	
	public static Object randomElement(RandomDataGenerator rnd, Collection<?> collection) {
		return rnd.nextSample(collection, 1)[0];
	}
	
	public static List<Object> randomElements(RandomDataGenerator rnd, Collection<?> collection, int k) {
		int sampleSize = Math.min(collection.size(), k);
		List<Object> elements = new ArrayList<Object>(sampleSize);
		Object[] elemArray = rnd.nextSample(collection, sampleSize);
		
		for (Object obj : elemArray)
			elements.add(obj);
		
		return elements;
	}

}
