package dk.meem;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/* Class that holds the decrypted data.
 * It also understands the format that usernames, passwords etc is stored in.
 * Currently only JSON.
 */
public class DataParser {
	private final JSONArray arraydata;
	
	/** Reads an encrypted password file stored as JSON.
	 * @param encryptedfilename
	 * @param password
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	
	public DataParser(String decrypteddata) {
		JSONObject rawdata = new JSONObject(decrypteddata);
		this.arraydata = rawdata.getJSONArray("data");		
	}
	
	public Object[][] getTabledata() throws UnsupportedEncodingException, IOException  {
		int columns = 0;
		
		if (arraydata.length() > 0) {
			columns = arraydata.getJSONObject(0).length();
			if (columns < 1) {
				throw new IOException("Input file is corrupted.");
			}
		} else {
			throw new IOException("No data in input file.");
		}
		
		return populateArray(arraydata, columns);
	}
	
	public Object[][] populateArray(JSONArray data, int columns) {
		Object[][] result = new Object[data.length()][columns];
		
		for (int i=0; i<data.length(); i++) {
			JSONObject entry = data.getJSONObject(i);
			result[i][0] = entry.opt("title");
			result[i][1] = entry.opt("userid");
			result[i][2] = entry.opt("password");
			result[i][3] = entry.opt("url");
		}

		return result;
	}
		
	public void addEntry() {
		JSONObject newentry = new JSONObject();
        newentry.put("title", "");
        newentry.put("userid", "");
        newentry.put("password", "");
        newentry.put("url", "");
        
        this.arraydata.put(newentry);
	}
	
	public String getDecryptedData() {
		JSONObject data = new JSONObject();
		data.put("data", this.arraydata);
		return data.toString();
	}
}
