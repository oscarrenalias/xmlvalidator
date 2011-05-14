package net.renalias.xmlvalidator.ui.components;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import java.awt.*;

public class XMLTextArea extends RSyntaxTextArea {
	public XMLTextArea(int rows, int cols) {
		super(rows, cols);
		setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
		setEditable(true);
		setMarkAllHighlightColor(Color.YELLOW);
	}
}
