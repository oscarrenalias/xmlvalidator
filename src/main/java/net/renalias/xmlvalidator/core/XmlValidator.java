package net.renalias.xmlvalidator.core;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

public class XmlValidator {

	// used to keep track of the validation errors
	List<ValidationError> validationErrors = new LinkedList<ValidationError>();

	public class XmlValidatorErrorHandler implements ErrorHandler {

		public void handleError(SAXParseException exception, ValidationError.ValidationExceptionSeverity severity) {
			validationErrors.add(new ValidationError(exception, severity));
		}

		public void warning(SAXParseException exception) throws SAXException {
			handleError(exception, ValidationError.ValidationExceptionSeverity.WARNING);
		}

		public void error(SAXParseException exception) throws SAXException {
			handleError(exception, ValidationError.ValidationExceptionSeverity.ERROR);
		}

		public void fatalError(SAXParseException exception) throws SAXException {
			handleError(exception, ValidationError.ValidationExceptionSeverity.FATAL);
		}
	}

    public boolean validateString(String schemaFile, String xmlContent) {
		// delete any previous errors, in case the same object is being reused
        // TODO: refactor the code
		validationErrors.clear();
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		boolean result = true;

		try {
			File schemaLocation = new File(schemaFile);
			Schema schema = factory.newSchema(schemaLocation);
			Validator validator = schema.newValidator();
			validator.setErrorHandler(new XmlValidatorErrorHandler());
			Source source = new StreamSource(new StringReader(xmlContent));
			validator.validate(source);
		} catch (Exception ex) {
			validationErrors.add(new ValidationError(ex, ValidationError.ValidationExceptionSeverity.FATAL));
			result = false;
		} finally {
			return (result && validationErrors.size() == 0);
		}
    }

	/**
	 * Parses the file and throws the original exception that was thrown by the validator; however it is
	 * advisable that if an exception is generated during parsing, that method getValidationException is used
	 * to retrieve the complete list of all exceptions
	 *
	 * @param schemaFile
	 * @param xmlFile
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 */
	public boolean validate(String schemaFile, String xmlFile) {
		// delete any previous errors, in case the same object is being reused
		validationErrors.clear();
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		boolean result = true;

		try {
			File schemaLocation = new File(schemaFile);
			Schema schema = factory.newSchema(schemaLocation);
			Validator validator = schema.newValidator();
			validator.setErrorHandler(new XmlValidatorErrorHandler());
			Source source = new StreamSource(xmlFile);
			validator.validate(source);
		} catch (Exception ex) {
			validationErrors.add(new ValidationError(ex, ValidationError.ValidationExceptionSeverity.FATAL));
			result = false;
		} finally {
			return (result && validationErrors.size() == 0);
		}
	}

	public List<ValidationError> getValidationErrors() {
		return validationErrors;
	}
}