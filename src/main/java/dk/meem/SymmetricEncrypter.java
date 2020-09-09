package dk.meem;

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
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONArray;
import org.json.JSONObject;

/* Based on this:
https://www.mkyong.com/java/java-symmetric-key-cryptography-example/
*/
public class SymmetricEncrypter {
	private Cipher cipher;
	private final int length;
	private final String algorithm = "AES";

	public SymmetricEncrypter()
			throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException {
		this.length = 32;
		this.cipher = Cipher.getInstance(this.algorithm);
	}

	public SymmetricEncrypter(int length)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException {
		this.length = length;
		this.cipher = Cipher.getInstance(this.algorithm);
	}

	public byte[] fixSecret(byte[] s) {
		return Arrays.copyOf(s, this.length);
	}

	public byte[] encrypt(String s, byte[] password)
			throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
		SecretKeySpec secretKey = new SecretKeySpec(fixSecret(password), this.algorithm);
		this.cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		return this.cipher.doFinal(s.getBytes("UTF-8"));
	}

	public byte[] decrypt(byte[] b, byte[] password)
			throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
		SecretKeySpec secretKey = new SecretKeySpec(fixSecret(password), this.algorithm);
		this.cipher.init(Cipher.DECRYPT_MODE, secretKey);
		return this.cipher.doFinal(b);
	}
	
	public Object[][] decryptPasswordfile(String filename, char[] password)
			throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
		
		ByteBuffer buffer = Charset.forName("UTF-8").encode(CharBuffer.wrap(password));
		byte[] password2 = new byte[buffer.limit()];
		buffer.get(password2);

		byte[] enc2 = Files.readAllBytes(Paths.get(filename));
		byte[] dec2 = this.decrypt(enc2, password2);
		JSONObject rawdata = new JSONObject(new String(dec2, "UTF-8"));
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
			result[i][0] = pwd.get("title");
			result[i][1] = pwd.get("userid");
			result[i][2] = pwd.get("password");
			result[i][3] = pwd.get("url");
		}

		return result;
	}
}