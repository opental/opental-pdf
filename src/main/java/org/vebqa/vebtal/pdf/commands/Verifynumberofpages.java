package org.vebqa.vebtal.pdf.commands;

import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.FailedResponse;
import org.vebqa.vebtal.model.PassedResponse;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PdfDriver;
import org.vebqa.vebtal.pdfrestserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, command = "verifyNumberOfPages", hintValue = "<int>")
public class Verifynumberofpages extends AbstractCommand {

	public Verifynumberofpages(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ASSERTION;
	}

	@Override
	public Response executeImpl(Object aDocument) {

		PdfDriver driver = (PdfDriver)aDocument;
		
		if (!driver.isLoaded()) {
			return new FailedResponse("No document loaded.");
		}

		if (this.value.isEmpty()) {
			return new FailedResponse("No expected value given.");
		}
		
		if (driver.getNumberOfPages() !=  Integer.parseInt(this.value)) {
			return new FailedResponse("Expected number of pages: <" + this.value + ">, but found: <" + driver.getNumberOfPages() + ">");
		}
		return new PassedResponse("Document has expected number of pages: " + this.value);
	}
}