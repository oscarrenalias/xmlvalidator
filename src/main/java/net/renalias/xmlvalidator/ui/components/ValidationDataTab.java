package net.renalias.xmlvalidator.ui.components;

import net.renalias.xmlvalidator.core.ValidationError;

import java.util.LinkedList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ValidationDataTab extends JPanel implements ElementSelectedListener {

	ValidationErrorTable validationList;
	JTextArea validationErrorData;
	List<ElementDoubleClickedListener> doubleClickedListenerList;
	List<ElementSelectedListener> selectedListenerList;

	public ValidationDataTab() {
		super(new BorderLayout());

		doubleClickedListenerList = new LinkedList<ElementDoubleClickedListener>();
		selectedListenerList = new LinkedList<ElementSelectedListener>();

		build();
	}

	protected void build() {
		// validation error list
		//JPanel validationPanel = new JPanel(new BorderLayout());
		validationList = new ValidationErrorTable();
		validationList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane validationListScrollPane = new JScrollPane(validationList);
		add(validationListScrollPane, BorderLayout.CENTER);

		// validation error data display
		validationErrorData = new JTextArea(10, 20);
		validationErrorData.setLineWrap(true);
		validationErrorData.setWrapStyleWord(true);
		validationErrorData.setEditable(false);
		JScrollPane validationErrorDataScrollPane = new JScrollPane(validationErrorData);
		add(validationErrorDataScrollPane, BorderLayout.PAGE_END);

		// and a mouse listener for catching single and double clicks
		validationList.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					// if double-clicking, jump to the correct position in the source file window
					ValidationErrorsTableModel listModel = (ValidationErrorsTableModel) validationList.getModel();
					ValidationError validationError = listModel.getValidationErrorList().get(validationList.getSelectedRow());
					fireElementSelectedEvent(validationError);
				} else if (e.getClickCount() == 2) {
					// if double-clicking, jump to the correct position in the source file window
					ValidationErrorsTableModel listModel = (ValidationErrorsTableModel) validationList.getModel();
					ValidationError validationError = listModel.getValidationErrorList().get(validationList.getSelectedRow());
					fireElementDoubleClickedEvent(validationError);
				}
			}

			// these events are not needed
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});

		addElementSelectedListener(this);
	}

	public void addElementSelectedListener(ElementSelectedListener listener) {
		selectedListenerList.add(listener);
	}

	public void addElementDoubleClickedListener(ElementDoubleClickedListener listener) {
		doubleClickedListenerList.add(listener);
	}

	public void fireElementSelectedEvent(ValidationError validationError) {
		for(ElementSelectedListener listener : selectedListenerList) {
			listener.onElementSelected(validationError);
		}
	}

	public void fireElementDoubleClickedEvent(ValidationError validationError) {
		for(ElementDoubleClickedListener listener : doubleClickedListenerList) {
			listener.onElementDoubleClicked(validationError);
		}
	}

	public void onElementSelected(ValidationError element) {
		String message = "Line: " + element.getLine()
				+ "\nColumn: " + element.getColumn()
				+ "\n" + element.getOriginalException().getMessage();

		validationErrorData.setText(message);
		validationErrorData.setCaretPosition(0);
	}

	public void setValidationErrors(List<ValidationError> validationErrors) {
		// move the validation errors to the data table
		ValidationErrorsTableModel listModel = new ValidationErrorsTableModel(validationErrors);
		validationList.setModel(listModel);
		validationErrorData.setText("");
	}

	public void clearValidationErrors() {
		setValidationErrors(new LinkedList<ValidationError>());
	}
}
