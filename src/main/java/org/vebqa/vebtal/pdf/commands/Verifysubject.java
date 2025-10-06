package org.vebqa.vebtal.pdf.commands;

import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.FailedResponse;
import org.vebqa.vebtal.model.PassedResponse;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PdfDriver;
import org.vebqa.vebtal.pdfrestserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, 
         command = "verifySubject", 
         hintValue = "<text>")
public class Verifysubject extends AbstractCommand {

	public Verifysubject(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ASSERTION;
	}

	@Override
	public Response executeImpl(Object aDocument) {
		
		if (!this.validate()) {
			return new FailedResponse("Input doesnt match validation pattern.");
		}
		
		PdfDriver driver = (PdfDriver) aDocument;
		
		if (!driver.isLoaded()) {
			return new FailedResponse("No document loaded.");
		}

		if (driver.getSubject() == null) {
            return new FailedResponse("Document does not have a title. Attribute is null!");
		}
		
		if (!driver.getSubject().contains(this.value)) {
			return new FailedResponse("Expected subject: \"" + this.value + "\", but found: \"" + driver.getSubject() + "\"");
		}
		return new PassedResponse("Successfully found subject");
	}
}