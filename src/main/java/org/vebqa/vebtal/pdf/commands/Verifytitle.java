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
         command = "verifyTitle", 
         hintValue = "<text>")
public class Verifytitle extends AbstractCommand {

	public Verifytitle(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ASSERTION;
	}

	@Override
	public Response executeImpl(Object aDocument) {
		
		if (!this.validate()) {
			return new FailedResponse("Input doesnt match validation pattern.");
		}

		PdfDriver driver = (PdfDriver)aDocument;
		
		if (!driver.isLoaded()) {
			return new FailedResponse("No document loaded.");
		}

		if (driver.getTitle() == null) {
			return new FailedResponse("Document does not have a title. Attribute is null!");
		}
		
		if (!driver.getTitle().contains(this.value)) {
			return new FailedResponse("Expected title was <" + this.value + "> but found <" + driver.getTitle() + ">.");
		}
		return new PassedResponse("Successfully found title.");
	}
}