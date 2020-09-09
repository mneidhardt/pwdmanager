package dk.meem;

import java.security.SecureRandom;


public class PasswordGenerator {
	SecureRandom rnd = new SecureRandom();
	Characterset charset = new Characterset("");
	

	public PasswordGenerator() {
		charset.dump();
	}

	public char[] generate(int length) {
        SecureRandom random = new SecureRandom();

	    char pwd[] = new char[length];

	    for (int i=0; i<length; i++) {
	    	pwd[i] = charset.get(random.nextInt(charset.size()));
	    }
	    
	    return pwd;
	}

}

