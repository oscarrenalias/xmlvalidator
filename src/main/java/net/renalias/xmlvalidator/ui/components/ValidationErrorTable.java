package net.renalias.xmlvalidator.ui.components;

public class ValidationErrorTable extends NicerJTable {

	public ValidationErrorTable() {
		super();
		init();
	}

	protected void init() {
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
