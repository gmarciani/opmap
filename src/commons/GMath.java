package commons;

public class GMath {

	private GMath() {}
	
	public static final double getAverage(final long values[]) {
		double average = 0.0;
		
		for (long value : values)
			average += value;
		
		average /= values.length;
		
		return average;
	}
	
	public static final double getAverage(final double values[]) {
		double average = 0.0;
		
		for (double value : values)
			average += value;
		
		average /= values.length;
		
		return average;
	}

}
