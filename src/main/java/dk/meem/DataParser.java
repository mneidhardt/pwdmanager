package dk.meem;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/* Class that understands the format that usernames, passwords etc is stored in.
 * Currently only JSON.
 */
public class DataParser {
	
	/** Reads an encrypted password file stored as JSON.
	 * @param encryptedfilename
	 * @param password
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public Object[][] getFromJSON(String decrypteddata) throws UnsupportedEncodingException, IOException  {
		JSONObject rawdata = new JSONObject(decrypteddata);
		JSONArray data = rawdata.getJSONArray("data");
		
		int columns = 0;
		
		if (data.length() > 0) {
			columns = data.getJSONObject(0).length();
			if (columns < 1) {
				throw new IOException("Input file is corrupted.");
			}
		} else {
			throw new IOException("No data in input file.");
		}
		Object[][] result = new Object[data.length()][columns];
		
		for (int i=0; i<data.length(); i++) {
			JSONObject pwd = data.getJSONObject(i);
			result[i][0] = this.getString(pwd, "title");
			result[i][1] = this.getString(pwd, "userid");
			result[i][2] = this.getString(pwd, "password");
			result[i][3] = this.getString(pwd, "url");
		}

		return result;
	}
	
	public String getString(JSONObject obj, String key) {
		if (obj.has(key)) {
			return obj.getString(key);
		} else {
			return "";
		}
	}
}
