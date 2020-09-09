package dk.meem;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

public class PwdManager extends JPanel {
	private static final long serialVersionUID = 5024032558086870774L;
    private boolean ALLOW_COLUMN_SELECTION = false;
    private boolean ALLOW_ROW_SELECTION = true;
    private String decryptcmd = "/usr/local/bin/gpg";
    private static final String basepath = System.getProperty("user.home") + "/.pwdmanager";
    private static final String encryptedpasswordfile = basepath + "/pwd.json.gpg";
    private static final long delaybeforeclipboardcleared = 25;
    private static final int maxattempts=3;
    private static final String outcharset = "UTF-8";
    private JTable table;
    private String decrypteddata;

    //public PwdManager() throws UnsupportedEncodingException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    public PwdManager() throws IOException {
        super(new GridLayout(1,0));
        
        if ( ! setup()) {
        	System.exit(1);
        }
                
        final int pwdcolumn = 2;
        final String[] columnnames = {"Titel", "BrugerID", "Password", "URL"};

		for (int count=1; count<=maxattempts; count++) {
			try {
		        char[] masterpassword = getPassword();
		        
		        Runcommand rc = new Runcommand(decryptcmd);
		        decrypteddata = rc.decrypt(encryptedpasswordfile, new String(masterpassword));
		        DataParser parser = new DataParser();
				final Object[][] data = parser.getFromJSON(decrypteddata);
				
				/* By default I sort the array by Titel. User has option later on to sort
				*  by any column they wish.
				*/
				Arrays.sort(data, new Comparator<Object[]>() {
				    public int compare(Object[] a, Object[] b) {
				    	String s1 = (String)a[0];
				    	String s2 = (String)b[0];
				    	return s1.compareTo(s2);
				    }
				});
				
				final String[] passwords = new String[data.length];
				for (int i = 0; i < data.length; i++) {
					passwords[i] = (String) data[i][pwdcolumn];
					data[i][pwdcolumn] = "********";
				}

				createTable(columnnames, data, passwords);
				//MyTable tbl = new MyTable(columnnames, data, passwords);
				break;
			} catch (IOException iex) {
				System.err.println("count=" + count + " max=" + maxattempts);
				System.err.println(iex.getMessage());

				if (count >= maxattempts) {
					JOptionPane.showMessageDialog(null, "Too many failed attempts.");
					throw new IOException("Quitting because too many failed attempts.");
				}
			} 
		}
    }
    
    private void createTable(String[] columnnames, Object[][] data, final String[] passwords) {
        //final JTable table = new JTable(new PwdTableModel(columnnames, data));
        table = new JTable(new PwdTableModel(columnnames, data));
        table.setAutoCreateRowSorter(true);
        table.setPreferredScrollableViewportSize(new Dimension(700, 700));
        table.setFillsViewportHeight(true);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (ALLOW_ROW_SELECTION) { // true by default
            ListSelectionModel rowSM = table.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    //Ignore extra messages.
                    if (e.getValueIsAdjusting()) return;

                    ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                    if (lsm.isSelectionEmpty()) {
                        System.out.println("No rows are selected.");
                    } else {
                        int selectedRow = lsm.getMinSelectionIndex();
                        selectedRow = table.convertRowIndexToModel(selectedRow);
                        StringSelection pwd = new StringSelection(passwords[selectedRow]);
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(pwd, null);
                        
                        // Set deferred task to delete the password from clipboard after a delay.
                        // This first checks that the contents of clipboard is identical to what we put in,
                        // to avoid deleting someone else's clipboard contents.
                        try {
                        	DeferredTask bc = new DeferredTask();
                        	bc.clearClipboardAfterDelay(delaybeforeclipboardcleared, passwords[selectedRow]);
                        } catch (InterruptedException iex) {
                        	System.err.println("Failed to clear clipboard: " + iex.getMessage());
                        } catch (ExecutionException eex) {
                        	System.err.println("Failed to clear clipboard: " + eex.getMessage());
                        }
                    }
                }
            });
        } else {
            table.setRowSelectionAllowed(false);
        }

        if (ALLOW_COLUMN_SELECTION) { // false by default
            if (ALLOW_ROW_SELECTION) {
                //We allow both row and column selection, which
                //implies that we *really* want to allow individual
                //cell selection.
                table.setCellSelectionEnabled(true);
            }
            table.setColumnSelectionAllowed(true);
            ListSelectionModel colSM =
                table.getColumnModel().getSelectionModel();
            colSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    //Ignore extra messages.
                    if (e.getValueIsAdjusting()) return;

                    ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                    if (lsm.isSelectionEmpty()) {
                        System.out.println("No columns are selected.");
                    } else {
                        int selectedCol = lsm.getMinSelectionIndex();
                        System.out.println("Column " + selectedCol
                                           + " is now selected.");
                    }
                }
            });
        }
        
        
        
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        add(scrollPane);
    }
    
    private boolean setup() {
      	Path path = Paths.get(encryptedpasswordfile);
      	
    	if (Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {
    		if (Files.isRegularFile(Paths.get(decryptcmd)) & 
    				Files.isReadable(Paths.get(decryptcmd)) &
    				Files.isExecutable(Paths.get(decryptcmd))) {
    			return true;
    		} else {
    			JOptionPane.showMessageDialog(null, "Cannot find/execute command: " + decryptcmd);
    			return false;
    		}
    	} else {
        	String msg = "Password file, " + System.getProperty("line.separator") +
        			encryptedpasswordfile + System.getProperty("line.separator") +
        			"not found." + System.getProperty("line.separator") +
        			"This must be a JSON file encrypted with gpg.";

        	JOptionPane.showMessageDialog(null, msg);
    		return false;
    	}
    }
    
    public static char[] getPassword() {
        JPanel panel = new JPanel();
        final JPasswordField passwordField = new JPasswordField(20);
        panel.add(new JLabel("Password"));
        panel.add(passwordField);
        JOptionPane pane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION) {
            @Override
            public void selectInitialValue() {
                passwordField.requestFocusInWindow();
            }
        };
        pane.createDialog(null, "Password").setVisible(true);
        //return passwordField.getPassword().length == 0 ? null : new String(passwordField.getPassword());
        return passwordField.getPassword();
    }
    
    public void addPassworditem(String userid) {
    	System.out.println("Got new entry: " + userid);
    	printDebugData(table);
    }
    
    public String generateJSON() {
    	return decrypteddata;
    }

    private void printDebugData(JTable table) {
        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        javax.swing.table.TableModel model = table.getModel();

        System.out.println("Value of data: ");
        for (int i=0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j=0; j < numCols; j++) {
                System.out.print("  " + model.getValueAt(i, j));
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }
}