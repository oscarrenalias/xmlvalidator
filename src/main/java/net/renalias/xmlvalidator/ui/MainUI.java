package net.renalias.xmlvalidator.ui;

import net.renalias.file.FileHelper;
import net.renalias.xmlvalidator.ui.support.NicerJTable;
import net.renalias.xmlvalidator.ui.support.CaretHelper;
import net.renalias.xmlvalidator.core.ValidationError;
import net.renalias.xmlvalidator.core.XmlValidator;
import net.renalias.xmlvalidator.ui.support.ValidationErrorTable;
import net.renalias.xmlvalidator.ui.support.ValidationErrorsTableModel;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

public class MainUI extends JPanel implements ActionListener {
	static private final String newline = "\n";
	JButton openMessageButton, openSchemaButton, validateButton;
	JFileChooser fileChooser;
	NicerJTable dataTable;
	JTabbedPane tabbedPane;
	JTextField schemaFileName, messageFileName;
	ValidationErrorTable validationList;
	RSyntaxTextArea messageData;
    JTextArea validationErrorData;

	public MainUI() {
		super(new BorderLayout());
		// Create a file chooser
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("."));

		JLabel labelSchema = new JLabel("XSD schema file: ");
		schemaFileName = new JTextField(25);
		schemaFileName.setEditable(false);
		openSchemaButton = new JButton("...", null);
		openSchemaButton.addActionListener(this);

		JLabel labelMessage = new JLabel("Message file: ");
		messageFileName = new JTextField(25);
		messageFileName.setEditable(true);
		messageFileName.getDocument().addDocumentListener(new DocumentListener() {
			public void updateMessageData() {
				try {
					messageData.setText(FileHelper.readFile(messageFileName.getText()));
					messageData.setCaretPosition(0);
				} catch (IOException e) {
					System.err.println(e.toString());
					e.printStackTrace();
				}
			}

			public void insertUpdate(DocumentEvent e) {
				updateMessageData();
			}

			public void removeUpdate(DocumentEvent e) {
				updateMessageData();
			}

			public void changedUpdate(DocumentEvent e) {				}
		});
		openMessageButton = new JButton("...", null);
		openMessageButton.addActionListener(this);

		//For layout purposes, put the buttons in a separate panel
		JPanel topPanel = new JPanel(new GridLayout(3, 1));

		JPanel schemaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		schemaPanel.add(labelSchema);
		schemaPanel.add(schemaFileName);
		schemaPanel.add(openSchemaButton);

		JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		messagePanel.add(labelMessage);
		messagePanel.add(messageFileName);
		messagePanel.add(openMessageButton);

		topPanel.add(schemaPanel);
		topPanel.add(messagePanel);

		validateButton = new JButton("Validate");
		JPanel validatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		validatePanel.add(validateButton);
		validateButton.setEnabled(false);
		validateButton.addActionListener(this);
		topPanel.add(validatePanel);

		//Add the buttons and the log to this panel.
		add(topPanel, BorderLayout.PAGE_START);

		// message source text area
		messageData = new RSyntaxTextArea(25, 90);
		messageData.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
		messageData.setEditable(true);
		RTextScrollPane messageDataScrollPane = new RTextScrollPane(messageData);
		messageDataScrollPane.setLineNumbersEnabled(true);
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Source", null, messageDataScrollPane, "Source message data");

		// validation error list
        JPanel validationPanel = new JPanel(new BorderLayout());

		validationList = new ValidationErrorTable();
		validationList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane validationListScrollPane = new JScrollPane(validationList);
		validationPanel.add(validationListScrollPane, BorderLayout.CENTER);
        //tabbedPane.addTab("Validation", null, validationListScrollPane, "Validation error list");
        tabbedPane.addTab("Validation", null, validationPanel, "Validation error list");

        // extra space to show the error information from the model
        validationErrorData = new JTextArea(10, 20);
        validationErrorData.setLineWrap(true);
        validationErrorData.setWrapStyleWord(true);
        validationErrorData.setEditable(false);
        JScrollPane validationErrorDataScrollPane = new JScrollPane(validationErrorData);
        validationPanel.add(validationErrorDataScrollPane, BorderLayout.PAGE_END);

		add(tabbedPane, BorderLayout.CENTER);
	}

	public void setSchemaFileName(String schemaFile) {
		schemaFileName.setText(schemaFile);
	}

	public void setMessageFileName(String messageFile) {
		messageFileName.setText(messageFile);
	}

	public void setValidateButton(boolean enabled) {
		validateButton.setEnabled(enabled);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == openMessageButton) {   // load message file
			int returnVal = fileChooser.showOpenDialog(MainUI.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				if (FileHelper.isReadable(fileChooser.getSelectedFile().getAbsolutePath()) &&
						FileHelper.isFile(fileChooser.getSelectedFile().getAbsolutePath())) {
					// update the file name in the text field
					messageFileName.setText(fileChooser.getSelectedFile().getAbsolutePath());
					// no need to do anything else, the text field's change event listener will
					// take care of loading the file into the text area
                    validationList.setModel(new ValidationErrorsTableModel());
				} else {
					JOptionPane.showMessageDialog(null, "File cannot be loaded", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} else if (e.getSource() == openSchemaButton) { // load schema file
			int returnVal = fileChooser.showOpenDialog(MainUI.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				schemaFileName.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		} else if (e.getSource() == validateButton) {   // run the validation
			// TODO: move this to a separate thread so that the UI is not blocked
			XmlValidator validator = new XmlValidator();
			//boolean result = validator.validate(schemaFileName.getText(), messageFileName.getText());
            boolean result = validator.validateString(schemaFileName.getText(), messageData.getText());
			if (!result) {
				// move the validation errors to the data table
				ValidationErrorsTableModel listModel = new ValidationErrorsTableModel(validator.getValidationErrors());
				validationList.setModel(listModel);
				// and a mouse listener for catching single and double clicks
				validationList.addMouseListener(new MouseListener() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 1) {
							// if double-clicking, jump to the correct position in the source file window
							ValidationErrorsTableModel listModel = (ValidationErrorsTableModel)validationList.getModel();
							ValidationError validationError = listModel.getValidationErrorList().get(validationList.getSelectedRow());

                            String message = "Line: " + validationError.getLine()
                                             + "\nColumn: " + validationError.getColumn()
                                             + "\n" + validationError.getOriginalException().getMessage();

                            validationErrorData.setText(message);
                            validationErrorData.setCaretPosition(0);
						}
						else if (e.getClickCount() == 2) {
							// if double-clicking, jump to the correct position in the source file window
							ValidationErrorsTableModel listModel = (ValidationErrorsTableModel)validationList.getModel();
							ValidationError validationError = listModel.getValidationErrorList().get(validationList.getSelectedRow());

							// jump to the right place in the text field and activate the tab
							CaretHelper.goToLineColumm(messageData, validationError.getLine(), validationError.getColumn());
							tabbedPane.setSelectedIndex(0);
						}
					}
                    // these events are not needed
					public void mousePressed(MouseEvent e) {}
					public void mouseReleased(MouseEvent e) {}
					public void mouseEntered(MouseEvent e) {}
					public void mouseExited(MouseEvent e) {}
				});
				// switch the tab to the validation error list tab
				tabbedPane.setSelectedIndex(1);
			} else {
				validationList.setModel(new ValidationErrorsTableModel());
                validationErrorData.setText("");
				JOptionPane.showMessageDialog(null, "No validation errors were found", "Success", JOptionPane.INFORMATION_MESSAGE);
			}
		}

		// we can enable the validation button if there's two files already selected
		if (!schemaFileName.getText().equals("") && !messageFileName.getText().equals(""))
			validateButton.setEnabled(true);
	}

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
		MainUI mainUI = new MainUI();
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