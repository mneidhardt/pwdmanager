package dk.meem;

import java.util.ArrayList;
import java.util.List;

public class Characterset {
	List<Character> charset = new ArrayList<Character>();	
	String type = null;
	
	public Characterset(String type) {
		this.type = type;
		this.setCharset();
	}	
    
	public void setCharset() {
		this.charset.clear();

		// Add lowercase chars:

		for (int i = 97; i < 97 + 26; i++) {
			charset.add((char) i);
		}
		charset.add('æ');
		charset.add('ø');
		charset.add('å');
		// Add uppercase:

		for (int i = 65; i < 65 + 26; i++) {
			charset.add((char) i);
		}
		charset.add('Æ');
		charset.add('Ø');
		charset.add('Å');
		// Add numbers:

		for (int i = 48; i < 48 + 10; i++) {
			charset.add((char) i);
		}
		// Add specials:
		for (int i = 33; i < 48; i++) {
			charset.add((char) i);
		}
		for (int i = 58; i < 65; i++) {
			charset.add((char) i);
		}
	}

    public List<Character> getCharset() {
    	return this.charset;
    }
    
    public char get(int i) {
    	return charset.get(i);
    }
    
    public int size() {
    	return charset.size();
    }
    
    public void clear() {
    	this.charset.clear();
    }
    
    public void dump() {
    	System.out.print("Type: " + this.type + "=[");
    	for (Character c : this.charset) {
    		System.out.print(c);
    	}
    	System.out.println("]");
    }

}

