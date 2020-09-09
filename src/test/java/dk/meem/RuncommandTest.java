package dk.meem;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class RuncommandTest {

	@Test
	public void test() {
		try {
			Runcommand rc = new Runcommand("/usr/local/bin/gpg");
			String decrypteddata = rc.decrypt("pwdtest.json.gpg", "unsafepwd");
			assertNotNull(decrypteddata);
		} catch (UnsupportedEncodingException ue) {
			fail("Exception: " + ue.getMessage());
		} catch (IOException ie) {
			fail("Exception: " + ie.getMessage());
		}
	}

}
