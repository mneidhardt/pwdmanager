package dk.meem;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Runcommand {
	private String cmd;
	
	public Runcommand(String cmd) throws IOException {
		this.cmd = cmd;
	}
	
	/* Virker ikke - gpg kan ikke tage både pwd og text fra stdin...
       Jeg kan i hvert fald ikke finde ud af hvordan...
	public static String encrypt(String unencryptedfile, String password) throws UnsupportedEncodingException, IOException {
	
		String encryptedfile = unencryptedfile + ".gpg";
		String[] args = {"--batch", "--passphrase-fd", "0", "-c", "-o", encryptedfile, unencryptedfile};
		
		return executeCommand(args, password);
	}
	*/
	
	public String decrypt(String encryptedfile, String password) throws UnsupportedEncodingException, IOException {
		if (Files.isRegularFile(Paths.get(encryptedfile)) & Files.isReadable(Paths.get(encryptedfile))) {
			String[] args = {"--batch", "--passphrase-fd", "0", "-d", encryptedfile};
			return executeCommand(args, password);
		} else {
			throw new IOException("Input file not found: " + encryptedfile);
		}
	}
	
	private String executeCommand(String[] args, String password) throws UnsupportedEncodingException, IOException {
		Executor exec = new DefaultExecutor();

		CommandLine cl = new CommandLine(cmd);
		cl.addArguments(args);

		ByteArrayInputStream input = new ByteArrayInputStream(password.getBytes("UTF-8"));
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		exec.setStreamHandler(new PumpStreamHandler(output, null, input));
		exec.execute(cl);

		return output.toString("UTF-8");
	}

}
