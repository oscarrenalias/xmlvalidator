package net.renalias.xmlvalidator;

import net.renalias.xmlvalidator.ui.XmlValidatorUI;

import javax.swing.*;

public class Main {

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event dispatch thread.
	 */
	private static void createAndShowGUI(String args[]) {
		//Create and set up the window.
		JFrame frame = new JFrame("XML Validator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Add content to the window.
		XmlValidatorUI mainUI = new XmlValidatorUI();
		// we can set the file names from the command line, useful for testing
		if (args.length == 2) {
			mainUI.setSchemaFileName(args[0]);
			mainUI.setMessageFileName(args[1]);
			// activate the validation button, since it's inactive by default
			mainUI.setValidateButton(true);
		}
		frame.add(mainUI);

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(final String[] args) {
		//Schedule a job for the event dispatch thread:
		//creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI(args);
			}
		});
	}
}
