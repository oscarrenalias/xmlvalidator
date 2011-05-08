package net.renalias.xmlvalidator.ui.components;

import net.renalias.xmlvalidator.core.ValidationError;
import org.xml.sax.SAXParseException;

import javax.swing.table.AbstractTableModel;
import java.util.LinkedList;
import java.util.List;

public class ValidationErrorsTableModel extends AbstractTableModel {

	List<ValidationError> validationErrorList;
	private String columnNames[] = { "Severity", "Line", "Column", "Description"};

	@Override
	public String getColumnName(int column) {
		return (columnNames[column]);
	}

	public ValidationErrorsTableModel(List<ValidationError> validationErrorList) {
		setValidationErrorList(validationErrorList);
	}

	public ValidationErrorsTableModel() {
		this.validationErrorList = new LinkedList<ValidationError>();
	}

	public List<ValidationError> getValidationErrorList() {
		return validationErrorList;
	}

	public void setValidationErrorList(List<ValidationError> validationErrorList) {
		this.validationErrorList = validationErrorList;
	}

	public int getRowCount() {
		return (validationErrorList.size());
	}

	public int getColumnCount() {
		return (columnNames.length);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		String value = "";
		ValidationError error = validationErrorList.get(rowIndex);
		Exception e = error.getOriginalException();

		switch(columnIndex) {
			case 0:
				value = error.getSeverity().toString();
				break;
			case 1:
				if(e.getClass().getSimpleName().equals("SAXParseException"))
					value = Integer.toString(((SAXParseException)e).getLineNumber());
				break;
			case 2:
				if(e.getClass().getSimpleName().equals("SAXParseException"))
					value = Integer.toString(((SAXParseException)e).getColumnNumber());
				break;
			case 3:
				value = e.getMessage();
				break;
		}

		return(value);
	}
}
