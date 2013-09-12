package ax.makila.comparableentititymining.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.abahgat.suffixtree.GeneralizedSuffixTree;

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

	private Set<String> naiveSuffixGen(String[] arr, String regex) {
		Set<String> complete = new HashSet<String>();
		for(int i = 0; i < arr.length; i++) {
			String comp = "";
			String t = arr[i];
			for(int j = t.length() - 1; j >= 0; j--) {
				comp = t.charAt(j) + comp;
				complete.add(comp);
			}
		}
	
 		Iterator<String> it = complete.iterator();
		while(it.hasNext()) {
			String s = it.next();
			if(!s.matches(regex)) {
				it.remove();
			}
		}
		return complete;
	}
	
	@Test
	public void testMatchingSuffixes() {
		//Matches a string containing two $c 
		String regex0 = "(^|.*?\\s)\\$c.*?\\s\\$c[^A-Za-z0-9_$].*?$";
		//Matches a string containing two C
		@SuppressWarnings("unused")
		String regex1 = "^.*?C.*?C.*?$";
		
		@SuppressWarnings("unused")
		String[] test0 = {
			"$c or $c?", 
			"$c is much better than $c", 
			"15 $c is cooler than that!", 
			"If I had 5$ for everytime I heard that, I would have $5", 
			"Two $c for the price of $c",
			"Peanut butter jelly", 
			" ", 
			"$$$$$$ #end", 
			"Coconut banana power!", 
			"I am almost as good in $c as in $c."
		};
		
		String[] test1 = {
			"ABBCAABBAABBACAAAB",
			"CC",
			"ABCABC",
			"CAC",
			"ABBBABB",
			"ABC",
			"AAAABBABABBACC",
			""	
		};
		
		String[] test2 = {
				"#start Is it better to be unhealthy and happy $c or good looking and stressed $c? #end",
				"#start Is it better to be unhealthy and happy $c or good looking and stressed $c? #end",
				"#start Is it better to be unhealthy and happy $c or good looking and stressed $c? #end",
				"#start Is it better to be unhealthy and happy $c or good looking and stressed $c? #end",
				"#start Is it better to be unhealthy and happy $c or good looking and stressed $c? #end",
				"#start Is it better to be unhealthy and happy $c or good looking and stressed $c? #end",
				"#start Is it better to be unhealthy and happy $c or good looking and stressed $c? #end",
				"#start Is it better to be unhealthy and happy $c or good looking and stressed $c? #end",
				"#start Is it better to be unhealthy and happy $c or good looking and stressed $c? #end",
				"#start Is it better to be unhealthy and happy $c or good looking and stressed $c? #end",
				"#start Is it better to be unhealthy and happy $c or good looking and stressed $c? #end",
				"#start Is it better to be unhealthy and happy $c or good looking and stressed $c? #end",
				"#start Is it better to be unhealthy and happy $c or good looking and stressed $c? #end",
				"#start Is it better to be unhealthy and happy $c or good looking and stressed $c? #end",
				"#start Is it better to be unhealthy and happy $c or good looking and stressed $c? #end",
				"#start Is it better to be unhealthy and happy $c or good looking and stressed $c? #end",
				"#start Is it better to be unhealthy and happy $c or good looking and stressed $c? #end",
				"#start Is it better to be unhealthy and happy $c or good looking and stressed $c? #end",
				"#start are there more delicious chocolate in cookies or in old car batteris? #end",
				"#start What is better national guard or reserve? #end",
				"#start What is the better dog food pedigree or eukanuba? #end",
				"#start What do you prefer short boots or long boots for riding and why? #end",
				"#start Why are games on iPhone better than Android? #end",
				"#start What airline is better for traveling to south america american airlines or continental? #end",
				"#start What book is better, Maximum ride the angel experiment or maximum ride schools out forever? #end",
				"#start Which Burberry face of the year do you prefer, Rosie Huntington or Emma Watson? #end",
				"#start Madrid vs. Barcelona: Which city is better to shop for designer brands? #end",
				"#start London vs. Paris: Which city is better(cheaper) for shopping? #end",
				"#start In the world, which is better 'sheeple' or 'goats'? (see details please)? #end",
				"#start Do you prefer Old Spice or Axe? #end",
				"#start Green vs pink? #end",
				"#start Heaven vs hell? #end",
				"#start Catapult vs ballista? #end",
				"#start Pasta vs pizza? #end",
				"#start Sonic vs Mario? #end",
				"#start $c vs $c? #end",
				"#start $c vs $c? #end",
				"#start $c vs $c? #end",
				"#start $c vs $c? #end",
				"#start $c vs $c? #end",
				"#start $c vs $c? #end",
				"#start $c vs $c? #end",
				"#start $c vs $c? #end",
				"#start $c vs $c? #end",
				"#start $c vs $c? #end",
				"#start $c vs $c? #end",
				"#start $c vs $c? #end",
				"#start $c vs $c? #end",
				"#start $c vs $c? #end",
				"#start $c vs $c? #end",
				"#start $c vs $c? #end",
				"#start $c vs $c? #end",
				"#start $c vs $c? #end",
				"#start $c vs $c? #end",
				"#start $c vs $c? #end",
				"#start iPhone vs Galaxy? #end",
				"#start Which City Is Better, NYC or Paris? #end",
				// Non-comparable questions x 18
				"#start How to convince my parents to buy me a iphone or samsung galaxy s4? #end",
				"#start Can I buy an iPhone at the apple store and then use it for cricket? #end",
				"#start What kind of car is a total chick magnet? #end",
				"#start Is the 1998 trail blazer 4x4? #end",
				"#start What's a good anime to watch? #end",
				"#start Does it affect you big time if you don't pass the CAHSEE exam the 1st time? #end",
				"#start Whats the used value on a jackson SLAT3-7 guitar? #end",
				"#start Merger of Cooperative bank into nationalized bank? #end",
				"#start Is there a laser dome in or around NJ? #end",
				"#start how can I return me deleted emails? #end",
				"#start Anne Bradstreet's poetry? PLEASE in need of HELP? #end",
				"#start White specks in stool, should I be concerned? #end",
				"#start I believe I've seen death and god through pot? #end",
				"#start I'm really tired and I need some time managment/ staying up tips please help!? #end",
				"#start I have lots of lung mucus! Going on for 3 months now!? #end",
				"#start is muscle confusion a good topic for a research paper on mma? #end",
				"#start Is Social Security an outdated form of social welfare? Is it time to just mandate people to save at banks? #end",
				"#start What does it mean when a guy doesn't text you back? #end"
		};
		
		matchingSuffixes(test2, regex0, "test3");
		//Tests the creation of dot files
		testDotGen(test1);
	}
	
	public void matchingSuffixes(String[] arr, String regex, String testName) {
		Set<String> complete = naiveSuffixGen(arr, regex);
		GeneralizedSuffixTree tree = new GeneralizedSuffixTree();
		
		for(int i = 0; i < arr.length; i++) {
			tree.put(arr[i], i);
		}
		
		Set<String> set = tree.searchMatchingSuffix(regex);
		List<String> treeList = new ArrayList<String>(set);
		List<String> completeList = new ArrayList<String>(complete);
		
		Collections.sort(treeList);
		Collections.sort(completeList);
		
		for(int i = 0; i < treeList.size(); i++) {
			System.out.println(treeList.get(i));
		}
		try {
			assertArrayEquals(treeList.toArray(new String[treeList.size()]), completeList.toArray(new String[completeList.size()]));
		}
		catch(AssertionError ase) {
			fail("Test: " + testName + ". " + ase.getMessage());
		}
	}
	
	public void testDotGen(String[] arr) {
		GeneralizedSuffixTree tree = new GeneralizedSuffixTree();
		for(int i = 0; i < arr.length; i++) {
			tree.put(arr[i], i);
		}
		tree.createDotGraph();
	}
}
