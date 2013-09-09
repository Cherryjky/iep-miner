package ax.makila.comparableentititymining.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*; 
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.abahgat.suffixtree.Utils;

public class SuffixTreeScannerTests {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void matchingSuffixes() {
		String[] test = {
				"$c or $c?", "$c is much better than $c", "15 $c is cooler than that!", 
				"If I had 5$ for everytime I heard that, I would have $5", 
				"Two $c for the price of $c", "Peanut butter jelly", "", "$$$$$$", 
				"Coconut banana power!", "I'm almost as good in $c as in $c."};
		
		Set<String> sub0 = Utils.getSubstrings("$c or $c");
		Set<String> sub1 = Utils.getSubstrings("$c is much better than $c");
		Set<String> sub2 = Utils.getSubstrings("Two $c for the price of one $c");
		Set<String> sub3 = Utils.getSubstrings("I'm almost as good in $c as in $c.");
 		List<String> complete = new ArrayList<String>(sub0);
 		complete.addAll(sub1);
 		complete.addAll(sub2);
 		complete.addAll(sub3);
 		Iterator<String> it = complete.iterator();
		while(it.hasNext()) {
			String s = it.next();
			if(!s.matches("(^|.*?\\s)\\$c\\s.*?\\s\\$c(\\s.*?|$|[.?,;:\\-(!])")) {
				it.remove();
			}
		}
		String[] match = complete.toArray(new String[complete.size()]);
		
		assertArrayEquals(test, match);
	}
}
