package net.renalias.xmlvalidator.ui.components;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.LinkedList;
import java.util.List;

/**
 * Generates an event whenever the content of the inner document changes
 */
public class CustomJTextField extends JTextField implements DocumentListener {
	List<ContentChangeListener> listeners;

	public CustomJTextField(int length) {
		super(length);

		listeners = new LinkedList<ContentChangeListener>();
		setEditable(true);
		this.getDocument().addDocumentListener(this);
	}

	public void addContentChangeListener(ContentChangeListener listener) {
		listeners.add(listener);
	}

	protected void fireContentChangeEvent(DocumentEvent e) {
		for(ContentChangeListener listener : listeners) {
			listener.onContentChange(e, this);
		}
	}

	public void insertUpdate(DocumentEvent event) {
		fireContentChangeEvent(event);
	}

	public void removeUpdate(DocumentEvent event) {
		fireContentChangeEvent(event);
	}

	public void changedUpdate(DocumentEvent event) {
		fireContentChangeEvent(event);
	}
}
