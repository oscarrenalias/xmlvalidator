import net.renalias.xmlvalidator.core.ValidationError;
import net.renalias.xmlvalidator.core.XmlValidator;
import junit.framework.TestCase;
import org.junit.Test;
import org.xml.sax.SAXParseException;

public class ValidatorTest extends TestCase {

	// this should work without an issue
	@Test
	public void testValidator() {
		XmlValidator validator = new XmlValidator();
		assertTrue(validator.validate(
				TestHelper.getTestFile("shiporder.xsd"),
				TestHelper.getTestFile("shiporder-OK.xml")
		));
	}

	// this should throw validation errors
	@Test
	public void testValidationError() {
		XmlValidator validator = new XmlValidator();
		boolean result = validator.validate(
				TestHelper.getTestFile("shiporder.xsd"),
				TestHelper.getTestFile("shiporder-FAIL.xml")
		);
		assertFalse("The validation did not return an error", result);
	}

	// when having validation errors, we've got the line and column available
	@Test
	public void testValidationErrorData() {
		XmlValidator validator = new XmlValidator();
		boolean result = validator.validate(
				TestHelper.getTestFile("shiporder.xsd"),
				TestHelper.getTestFile("shiporder-FAIL.xml")
		);
		assertFalse("The validation did not return an error", result);

		for (ValidationError error : validator.getValidationErrors()) {
			SAXParseException saxParseException = (SAXParseException) error.getOriginalException();
			assertTrue("Line number was not available", saxParseException.getLineNumber() > 0);
			assertTrue("Column number was not available", saxParseException.getColumnNumber() > 0);
			assertTrue("There were no validation errors reported", validator.getValidationErrors().size() > 0);
		}
	}

	// this should fail
	@Test
	public void testFileNotFound() {
		XmlValidator validator = new XmlValidator();
		boolean result = validator.validate(
				TestHelper.getTestFile("shiporder.xsd"),
				TestHelper.getTestFile("DOES NOT EXIST")
		);
		assertFalse("The validation did not return an error", result);
	}
}
