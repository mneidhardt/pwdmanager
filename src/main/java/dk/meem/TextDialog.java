package dk.meem;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

class TextDialog extends JDialog implements ActionListener, WindowListener {
	private static final long serialVersionUID = 3023825305098512950L;

	TextDialog(String title, boolean modal, String content) {
		//super(cr, title, modal);

		//CENTER
		JPanel centerPanel = new JPanel();
		//JTabbedPane tabbedPane = new JTabbedPane();
		centerPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED));

		JEditorPane psPane = new JEditorPane("text/plain", content);
		JScrollPane psPane2 = new JScrollPane(psPane);
		psPane2.setPreferredSize(new Dimension(1200, 1200));

        centerPanel.add(psPane2);
		getContentPane().add(centerPanel, BorderLayout.CENTER);

		//SOUTH
		JPanel southPanel = new JPanel();
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(this);
		southPanel.add(okButton);
		getContentPane().add(southPanel, BorderLayout.SOUTH);

		addWindowListener(this);
		pack();
	}

/*	public void setPostscriptCode(String psc) {
		overviewPane.setText(psc);
	}
*/

	//Implement ActionListener interface
	public void actionPerformed(ActionEvent e)
	{
		setVisible(false);
	}

	//Implement WindowListener interface
	public void windowClosing(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
}
