package dk.meem;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.json.JSONObject;
import org.junit.Test;

public class DataParserTest {
	private final String testinputA = "{\n" + 
			"  'data' : [\n" + 
			"    { \"title\":\"Gmail\", \"userid\":\"dummy@gmail.com\", \"password\":\"unsafe\", \"url\":\"https://accounts.google.com/\"},\n" + 
			"    { \"title\":\"Facebook\", \"userid\":\"dummy@gmail.com\", \"password\":\"alsoUnsafe\", \"url\":\"https://www.facebook.com/\"}\n" + 
			"  ]\n" + 
			"}";

	private final String testinputB = "{ \"title\":\"mail\" }";
	

	@Test
	public void test1() {
		
		try {
			DataParser parser = new DataParser(testinputA);
			String data = parser.getDecryptedData();
			assertTrue(data.length() > 0);
		} catch (Exception ex) {
			fail("Exception: " + ex.getMessage());
		}

	}
	
	@Test
	public void test2() {
				
		try {
			DataParser parser = new DataParser(testinputA);
			Object[][] data = parser.getTabledata();
			assertEquals(data.length, 2);
			assertEquals(data[0].length, 4);
			
			assertEquals(data[0][0], "Gmail");
			assertEquals(data[0][1], "dummy@gmail.com");
			assertEquals(data[0][2], "unsafe");
			assertEquals(data[0][3], "https://accounts.google.com/");

			assertEquals(data[1][0], "Facebook");
			assertEquals(data[1][1], "dummy@gmail.com");
			assertEquals(data[1][2], "alsoUnsafe");
			assertEquals(data[1][3], "https://www.facebook.com/");
		} catch (UnsupportedEncodingException ue) {
			fail("Exception: " + ue.getMessage());
		} catch (IOException ie) {
			fail("Exception: " + ie.getMessage());
		}
	}
	
	@Test
	public void test3() {
				
		try {
			DataParser parser = new DataParser(testinputA);
			Object[][] data = parser.getTabledata();
			assertEquals(data.length, 2);
			assertEquals(data[0].length, 4);
			
			parser.addEntry();
			Object[][] data2 = parser.getTabledata();
			assertEquals(data2.length, 3);
			
		} catch (UnsupportedEncodingException ue) {
			fail("Exception: " + ue.getMessage());
		} catch (IOException ie) {
			fail("Exception: " + ie.getMessage());
		}
	}

}
