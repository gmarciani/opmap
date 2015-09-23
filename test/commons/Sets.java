package commons;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.junit.Test;

public class Sets {

	@Test public void test() {
		Set<String> set = new ConcurrentSkipListSet<String>();
		
		for (int i = 0; i < 10; i++)
			set.add("a");
		
		System.out.println(set);
		
		String curr;
		
		Iterator<String> iter = set.iterator();
		
		while (iter.hasNext()) {
			curr = iter.next();
			iter.remove();
			set.add(curr.toUpperCase());
		}
		
		System.out.println(set);
	}

}
