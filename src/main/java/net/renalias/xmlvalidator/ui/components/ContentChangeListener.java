package net.renalias.xmlvalidator.ui.components;

import javax.swing.event.DocumentEvent;
import java.util.EventListener;

public interface ContentChangeListener extends EventListener {
	public void onContentChange(DocumentEvent e, CustomJTextField textField);
}