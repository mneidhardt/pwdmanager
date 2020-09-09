package dk.meem;

import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.json.JSONObject;
import org.junit.Test;

public class SymmetricEncrypterTest {

	@Test
	public void test() {
		//fail("Not yet implemented");
	
		String s =  "{ \"data\" : " +
				"[" +
				"{ \"title\":\"Gmail\", \"userid\":\"dummy@gmail.com\", \"password\":\"unsafe\", \"url\":\"https://accounts.google.com/\"}," + 
				"{ \"title\":\"Facebook\", \"userid\":\"dummy@facebook.com\", \"password\":\"alsoUnsafe\", \"url\":\"https://www.facebook.com/\"}" +
				"]" +
				"}";
		
		// Verify that we can create a JSON Object.
		JSONObject jo = new JSONObject(s);
		assertTrue(jo instanceof JSONObject);

		SymmetricEncrypter se;
		
		try {
			int secretlength = 32;
			byte[] password = "unsafepwd".getBytes();
			se = new SymmetricEncrypter(secretlength);
			try {
				byte[] enc = se.encrypt(s, password);
				byte[] dec = se.decrypt(enc, password);

				assertEquals(s, new String(dec, "UTF-8"));
				
				String testfilename = "symmetric.json.enc";
				FileOutputStream fos = new FileOutputStream(testfilename);
				fos.write(enc);
				fos.close();
				
				byte[] enc2 = Files.readAllBytes(Paths.get(testfilename));
				byte[] dec2 = se.decrypt(enc2, password);
				assertEquals(s, new String(dec2, "UTF-8"));
				
				byte[] test = {'x'};
				byte[] fixed = se.fixSecret(test);
				assertEquals(fixed.length, secretlength);
				byte[] fixed2 = Arrays.copyOf(test, 100);
				assertEquals(fixed2.length, 100);
				assertEquals(se.fixSecret(fixed2).length, secretlength);
								
			} catch (InvalidKeyException e) {
				fail(e.getMessage());
			} catch (BadPaddingException e) {
				fail(e.getMessage());
			} catch (IllegalBlockSizeException e) {
				fail(e.getMessage());
			} catch (IOException e) {
				fail(e.getMessage());
			}
		} catch (NoSuchAlgorithmException e) {
			fail(e.getMessage());
		} catch (NoSuchPaddingException e) {
				fail(e.getMessage());
		} catch (UnsupportedEncodingException e) {
				fail(e.getMessage());
		}
	}
	
	@Test
	public void test2() {
		char[] charpwd = {'u', 'n', 's', 'a', 'f', 'e', 'p', 'w', 'd' };
		
		byte[] password1 = "unsafepwd".getBytes();

		ByteBuffer buffer = Charset.forName("UTF-8").encode(CharBuffer.wrap(charpwd));
		byte[] password2 = new byte[buffer.limit()];
		buffer.get(password2);
		if (password1.length == password2.length) {		
			for (int i=0; i<password1.length; i++) {
				if (password1[i] != password2[i]) {
					assert(false);
				}
			}
		} else {
			assert(false);
		}
	}

}
