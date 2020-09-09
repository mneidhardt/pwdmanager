package dk.meem;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class DeferredTask {

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public void clearClipboardAfterDelay(long delay, String stringtodelete) throws ExecutionException, InterruptedException {
		final String str = stringtodelete;
		
		final Runnable beeper = new Runnable() {
			public void run() {
				StringSelection nothing = new StringSelection(null);
                final Clipboard cp = Toolkit.getDefaultToolkit().getSystemClipboard();
                
                if (str.equals(clipboardContents())) {
                	cp.setContents(nothing,  nothing);
                	System.out.println("My string, so I deleted cp. " + 
                			new Timestamp(System.currentTimeMillis()));
                } else {
                	System.out.println("Not my string. " +
                			new Timestamp(System.currentTimeMillis()));
                }
			}
		};
		ScheduledFuture<?> scheduledFuture = scheduler.schedule(beeper, delay, SECONDS);
		scheduler.shutdown();
	}

	public String clipboardContents(){
	    try {
	        return (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
	    } catch (HeadlessException e) {
	    	return null;
	    } catch (UnsupportedFlavorException e) {
	    	return null;
	    } catch (IOException e) {
	    	return null;
	    }
	}
}
