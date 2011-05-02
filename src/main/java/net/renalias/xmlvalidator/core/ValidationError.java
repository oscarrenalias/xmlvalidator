package net.renalias.xmlvalidator.core;

import org.xml.sax.SAXParseException;

public class ValidationError extends Exception {

	public enum ValidationExceptionSeverity {WARNING, ERROR, FATAL};

	ValidationExceptionSeverity severity;
	Exception exception;

	public ValidationError(Exception exception, ValidationExceptionSeverity severity) {
		this.exception = exception;
		this.severity = severity;
	}

	public ValidationExceptionSeverity getSeverity() {
		return severity;
	}

	public Exception getOriginalException() {
		return (exception);
	}

	public int getLine() {
		int line = 0;
		if(getOriginalException().getClass().getSimpleName().equals(("SAXParseException")))
			line = ((SAXParseException)getOriginalException()).getLineNumber();

		return(line);
	}

	public int getColumn() {
		int column = 0;
		if(getOriginalException().getClass().getSimpleName().equals(("SAXParseException")))
			column = ((SAXParseException)getOriginalException()).getColumnNumber();

		return(column);
	}

	@Override
	public String toString() {
		return "ValidationError{" +
				"severity=" + severity +
				", exception=" + exception +
				", line=" + getLine() +
				", column=" + getColumn() +
				'}';
	}
}
