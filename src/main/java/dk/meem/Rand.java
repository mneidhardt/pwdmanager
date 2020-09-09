package dk.meem;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Just testing random number generator
 */
public class Rand {
	SecureRandom rnd = new SecureRandom();
	Map<Integer, Integer> mymap = new HashMap<Integer, Integer>();
	List<Character> charset = new ArrayList<Character>();
	
	public Rand() {
		this.setCharset();
	}
	
    public void runTest(int max, int range) {
    	for (int i=0; i<max; i++) {
            int x = rnd.nextInt(range);
            
            if (mymap.containsKey(x)) {
            	mymap.replace(x, mymap.get(x)+1);
            } else {
            	mymap.put(x,  1);
            }
    	}
    	
    	this.listMap(max, range);
    }
    
    public void setCharset() {
    	charset.clear();
    	
 		for(int i = 97; i < 97+26; i++){
    		charset.add((char)i);
    	}
    	charset.add('æ');
    	charset.add('ø');
    	charset.add('å');
 		for(int i = 65; i < 65+26; i++){
    		charset.add((char)i);
    	}
    	charset.add('Æ');
    	charset.add('Ø');
    	charset.add('Å');
 		for(int i = 48; i < 48+10; i++){
    		charset.add((char)i);
    	}
    }
    
    public int charsetSize() {
    	return charset.size();
    }
    
    public void dumpCharset() {
    	for (char c : charset) {
    		System.out.print(c);
    	}
    	System.out.println("\nSize of charset: " + charset.size());
    }
    
    public void listMap(int max, int range) {
    	double avgPerBucket = max/range;
    	System.out.println(avgPerBucket);
		for (Map.Entry<Integer, Integer> entry : mymap.entrySet()) {
			System.out.println(entry.getKey() + " = " + entry.getValue() + " Variation: " + Math.abs(avgPerBucket-entry.getValue()));
		}

    }

}
