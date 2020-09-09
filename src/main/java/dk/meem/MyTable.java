package dk.meem;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.concurrent.ExecutionException;

public class MyTable extends JPanel {
	private static final long serialVersionUID = 5024032558086870774L;
    private boolean ALLOW_COLUMN_SELECTION = false;
    private boolean ALLOW_ROW_SELECTION = true;
    private static final long delaybeforeclipboardcleared = 25;
    final JTable table;

    public MyTable(String[] columnnames, Object[][] data, final String[] passwords) {
        table = new JTable(new PwdTableModel(columnnames, data));
        table.setAutoCreateRowSorter(true);
        table.setPreferredScrollableViewportSize(new Dimension(700, 170));
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
}