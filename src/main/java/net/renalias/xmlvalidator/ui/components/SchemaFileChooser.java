package net.renalias.xmlvalidator.ui.components;

import net.renalias.file.FileHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SchemaFileChooser extends JDialog implements ActionListener, PropertyChangeListener {

	JTextField fileField;
	JButton selectButton;
	JFileChooser fileChooser = new JFileChooser();
	JOptionPane content;
	String[] buttons = { "Validate", "Cancel" };
	public enum SchemaFileChooserReturnValue { OK, CANCEL };
	SchemaFileChooserReturnValue returnValue;

	public SchemaFileChooser(Frame frame) {
		super(frame, "Validate with schema file", true);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		JPanel mainPanel = new JPanel(new BorderLayout());
		JLabel label = new JLabel("Please select a schema file for validation:");
		mainPanel.add(label, BorderLayout.PAGE_START);

		JPanel panel = new JPanel();
		fileField = new JTextField(35);
		selectButton = new JButton("...");
		selectButton.addActionListener(this);
		panel.add(fileField);
		panel.add(selectButton);
		mainPanel.add(panel, BorderLayout.PAGE_END);

		content = new JOptionPane(mainPanel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, buttons, buttons[0]);
		content.addPropertyChangeListener(this);
		setContentPane(content);

		pack();
	}

	public SchemaFileChooserReturnValue showDialog() {
		setVisible(true);
		return(returnValue);
	}

	public String getSchemaFile() {
		return (fileField.getText());
	}

	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getSource() == selectButton) {
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				if (FileHelper.isReadable(fileChooser.getSelectedFile().getAbsolutePath()) &&
						FileHelper.isFile(fileChooser.getSelectedFile().getAbsolutePath())) {
					fileField.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		} else {
			System.out.println(actionEvent);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();

		if (isVisible() && (e.getSource() == content) && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
				JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
			Object value = content.getValue();

			if (value == JOptionPane.UNINITIALIZED_VALUE) {
				//ignore reset
				return;
			}

			//Reset the JOptionPane's value.
			//If you don't do this, then if the user
			//presses the same button next time, no
			//property change event will be fired.
			content.setValue(JOptionPane.UNINITIALIZED_VALUE);

			if(value.equals(buttons[0])) {
				returnValue = SchemaFileChooser.SchemaFileChooserReturnValue.OK;
			}
			else {
				returnValue = SchemaFileChooser.SchemaFileChooserReturnValue.CANCEL;
			}

			setVisible(false);
		}
	}
}
