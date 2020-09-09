package dk.meem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class MenuBar implements ActionListener {
	
	PwdManager pwdman;
	
	public MenuBar(PwdManager pm) {
		this.pwdman = pm;
	}
	
	public JMenuBar getMenuBar() {
		JMenuBar menubar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.getAccessibleContext().setAccessibleDescription(
		        "The only menu in this program that has menu items");
		menubar.add(fileMenu);
		
		JMenuItem newEntry = new JMenuItem("New entry");
		newEntry.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
		newEntry.addActionListener(this);
		fileMenu.add(newEntry);

		/*JMenuItem fileSave = new JMenuItem("Save file");
		fileSave.addActionListener(this);
		fileMenu.add(fileSave);*/
		
		JMenuItem genPwd = new JMenuItem("Generate password");
		genPwd.addActionListener(this);
		fileMenu.add(genPwd);
		
		JMenuItem fileExit = new JMenuItem("Exit");
		fileExit.addActionListener(this);
		fileMenu.add(fileExit);

		return menubar;
	}

	public void actionPerformed(ActionEvent e) {
	      System.out.println("Action performed: " + e.getActionCommand());
	      
	      if (e.getActionCommand() == "Exit") {
	          System.exit(0);
	      } else if (e.getActionCommand() == "New entry") {
	    	  pwdman.addPassworditem(newEntry());
	      } else if (e.getActionCommand() == "Generate password") {
	    	  generatePassword();
	      }
    }

	public void generatePassword() {
		PasswordGenerator pwgen = new PasswordGenerator();
		char[] pwd = pwgen.generate(32);

		JTextArea ta = new JTextArea(1, 60);
        ta.setText(new String(pwd));
        ta.setWrapStyleWord(true);
        ta.setLineWrap(true);
        ta.setCaretPosition(0);
        ta.setEditable(false);
        ta.selectAll();

        JOptionPane.showMessageDialog(null, new JScrollPane(ta), "RESULT", JOptionPane.INFORMATION_MESSAGE);
	}

	public String newEntry() {
        JPanel panel = new JPanel();
        final JTextField userField = new JTextField(20);
        final JTextField titleField = new JTextField(20);
        final JPasswordField passwordField = new JPasswordField(20);
        final JTextField urlField = new JTextField(20);
        
        panel.add(new JLabel("BrugerID"));
        panel.add(userField);
        panel.add(new JLabel("Title"));
        panel.add(titleField);
        panel.add(new JLabel("Password"));
        panel.add(passwordField);
        panel.add(new JLabel("URL"));
        panel.add(urlField);
        JOptionPane pane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION) {
            @Override
            public void selectInitialValue() {
                userField.requestFocusInWindow();
            }
        };
        pane.createDialog(null, "New entry").setVisible(true);
        /*System.out.println("User =" + userField.getText());
        System.out.println("Title=" + titleField.getText());
        System.out.println("Pwd  =" + new String(passwordField.getPassword()));
        System.out.println("URL=" + urlField.getText());
        */
        
        return userField.getText();
    }
 
}
