package net.renalias.xmlvalidator.ui;

import net.renalias.xmlvalidator.core.ValidationError;
import net.renalias.xmlvalidator.core.XmlValidator;
import net.renalias.xmlvalidator.ui.components.EditorTab;
import net.renalias.xmlvalidator.ui.components.ElementDoubleClickedListener;
import net.renalias.xmlvalidator.ui.components.ValidateButtonClickedListener;
import net.renalias.xmlvalidator.ui.components.ValidationDataTab;
import net.renalias.xmlvalidator.ui.support.CaretHelper;

import javax.swing.*;
import java.awt.*;

public class XmlValidatorUI extends JPanel implements ValidateButtonClickedListener, ElementDoubleClickedListener {
	static private final String newline = "\n";
	JTabbedPane tabbedPane;
	protected ValidationDataTab validationDataTab;
	protected EditorTab editorTab;

	public XmlValidatorUI() {
		super(new BorderLayout());
		// tabbed pane for validation info
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		editorTab = new EditorTab();
		tabbedPane.addTab("Source", null, editorTab, "Source message data");
		validationDataTab = new ValidationDataTab();
		tabbedPane.addTab("Validation", null, validationDataTab, "Validation error list");
		add(tabbedPane, BorderLayout.CENTER);

		editorTab.addValidateButtonClickedListener(this);
		validationDataTab.addElementDoubleClickedListener(this);
	}

	public void setSchemaFileName(String schemaFile) {
		//schemaFileName.setText(schemaFile);
	}

	public void setMessageFileName(String messageFile) {
		//messageFileName.setText(messageFile);
	}

	public void setValidateButton(boolean enabled) {
		//validateButton.setEnabled(enabled);
	}

	public void onValidateButtonClicked() {
		// TODO: move this to a separate thread so that the UI is not blocked
		XmlValidator validator = new XmlValidator();
		boolean result = validator.validateString(editorTab.getSchemaFile(), editorTab.getMessageText());

		if (!result) {
			// switch the tab to the validation error list tab
			validationDataTab.setValidationErrors(validator.getValidationErrors());
			tabbedPane.setSelectedIndex(1);
		} else {
			validationDataTab.clearValidationErrors();
			JOptionPane.showMessageDialog(null, "No validation errors were found", "Success", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public void onElementDoubleClicked(ValidationError element) {
		// jump to the right place in the text field and activate the tab
		CaretHelper.goToLineColumm(editorTab.getMessageData(), element.getLine(), element.getColumn());
		tabbedPane.setSelectedIndex(0);
	}
}