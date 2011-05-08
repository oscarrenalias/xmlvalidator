package net.renalias.xmlvalidator.ui.components;

import net.renalias.file.FileHelper;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import java.util.LinkedList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class EditorTab extends JPanel implements ActionListener {

	RSyntaxTextArea messageData;
	JTextField schemaFileName;
	CustomJTextField messageFileName;
	JButton openMessageButton, openSchemaButton, validateButton;
	JFileChooser fileChooser;
	List<ValidateButtonClickedListener> validateButtonClickedListenerList;

	public EditorTab() {
		super(new BorderLayout());
		add(buildSourceTab());
		add(buildFileSelectors(), BorderLayout.PAGE_START);

		validateButtonClickedListenerList = new LinkedList<ValidateButtonClickedListener>();
	}

	public void addValidateButtonClickedListener(ValidateButtonClickedListener listener) {
		validateButtonClickedListenerList.add(listener);
	}

	private Component buildSourceTab() {
		messageData = new RSyntaxTextArea(25, 90);
		messageData.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
		messageData.setEditable(true);
		RTextScrollPane messageDataScrollPane = new RTextScrollPane(messageData);
		messageDataScrollPane.setLineNumbersEnabled(true);
	    return(messageDataScrollPane);
	}

	private Component buildFileSelectors() {

		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("."));


		JLabel labelSchema = new JLabel("XSD schema file: ");
		schemaFileName = new JTextField(25);
		schemaFileName.setEditable(false);
		openSchemaButton = new JButton("...", null);
		openSchemaButton.addActionListener(this);

		JLabel labelMessage = new JLabel("Message file: ");
		messageFileName = new CustomJTextField(25);
		messageFileName.addContentChangeListener(new ContentChangeListener() {
			public void onContentChange(DocumentEvent e, CustomJTextField textField) {
				try {
					messageData.setText(FileHelper.readFile(messageFileName.getText()));
					messageData.setCaretPosition(0);
				} catch (IOException ex) {
					System.err.println(e.toString());
					ex.printStackTrace();
				}
			}
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

		return(topPanel);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == openMessageButton) {   // load message file
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				if (FileHelper.isReadable(fileChooser.getSelectedFile().getAbsolutePath()) &&
						FileHelper.isFile(fileChooser.getSelectedFile().getAbsolutePath())) {
					// update the file name in the text field
					messageFileName.setText(fileChooser.getSelectedFile().getAbsolutePath());
					// no need to do anything else, the text field's change event listener will
					// take care of loading the file into the text area
                    //validationList.setModel(new ValidationErrorsTableModel());
				} else {
					JOptionPane.showMessageDialog(null, "File cannot be loaded", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} else if (e.getSource() == openSchemaButton) { // load schema file
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				schemaFileName.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		} else if (e.getSource() == validateButton) {   // run the validation
			fireValidateButtonClicked();
		}

		// we can enable the validation button if there's two files already selected
		if (!schemaFileName.getText().equals("") && !messageFileName.getText().equals(""))
			validateButton.setEnabled(true);
	}

	protected void fireValidateButtonClicked() {
		for(ValidateButtonClickedListener validateButtonClickedListener : validateButtonClickedListenerList) {
			validateButtonClickedListener.onValidateButtonClicked();
		}
	}

	public String getSchemaFile() {
		return(schemaFileName.getText());
	}

	public String getMessageFile() {
		return(messageFileName.getText());
	}

	public String getMessageText() {
		return(messageData.getText());
	}

	public RSyntaxTextArea getMessageData() {
		return(messageData);
	}
}