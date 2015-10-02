package commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public final class Randomizer {

	private Randomizer() {}
	
	public static boolean rndBoolean(Random rnd, final double positivity) {
		return Math.random() < positivity;
	}
	
	public static int rndInteger(Random rnd, final int lb, final int ub) {			
		return rnd.nextInt(ub) + lb;
	}
	
	public static double rndDouble(Random rnd, final double lb, final double ub) {		
		return lb + (ub - lb) * rnd.nextDouble();
	}
	
	public static <T> List<T> rndItems(Random rnd, Set<T> set, final int n) {
		List<T> list = new ArrayList<T>(set);
		return rndItems(rnd, list, n);
	}
	
	public static <T> T rndItem(Random rnd, Set<T> set) {
		List<T> list = new ArrayList<T>(set);
		return rndItem(rnd, list);
	}
	
	public static <T> List<T> rndItems(Random rnd, List<T> list, final int n) {
		List<T> items = new ArrayList<T>();
		
		while (items.size() < n) {
			T item = rndItem(rnd, list);
			if (!items.contains(item))
				items.add(item);
		}
		
		return items;
	}
	
	public static <T> T rndItem(Random rnd, List<T> list) {
		int elem = rndInteger(rnd, 0, list.size());
		return list.get(elem);
	}

}
