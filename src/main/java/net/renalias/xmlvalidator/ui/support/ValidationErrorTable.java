package net.renalias.xmlvalidator.ui.support;

import net.renalias.xmlvalidator.ui.support.NicerJTable;

import javax.swing.text.Element;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ValidationErrorTable extends NicerJTable {

	public ValidationErrorTable() {
		super();
	}

	public void setModel(ValidationErrorsTableModel validationErrorsTableModel) {
		super.setModel(validationErrorsTableModel);

		// set preferred column sizes
		getColumnModel().getColumn(0).setPreferredWidth(50);
		getColumnModel().getColumn(1).setPreferredWidth(50);
		getColumnModel().getColumn(2).setPreferredWidth(50);
		getColumnModel().getColumn(3).setPreferredWidth(500);
	}
}
