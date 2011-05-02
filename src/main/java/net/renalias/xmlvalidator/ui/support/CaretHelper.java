package net.renalias.xmlvalidator.ui.support;

import javax.swing.*;
import javax.swing.text.Element;

public class CaretHelper {
	public static void goToLineColumm(JTextArea component, int line, int column) {
		Element root = component.getDocument().getDefaultRootElement();
		int offset = 0;
        int lineStart;
        if(line < component.getLineCount()) {
            lineStart = root.getElement(line - 1).getStartOffset();
		    offset = lineStart + (column - 1);
        }
        else {
            // in case we're asking to jump to a line that doesn't exist
            lineStart = root.getElement(component.getLineCount()-1).getStartOffset();
            offset = lineStart;
        }
		component.setCaretPosition(offset);
	}
}