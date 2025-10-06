package org.vebqa.vebtal.pdf.commands;

import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.FailedResponse;
import org.vebqa.vebtal.model.PassedResponse;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PdfDriver;
import org.vebqa.vebtal.pdfrestserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, command = "verifyAuthor", hintValue = "<string>")
public class Verifyauthor extends AbstractCommand {

	public Verifyauthor(String aCommand, String aTarget, String aValue) {
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
		
		if (driver.getAuthor() == null) {
			return new FailedResponse("Document does not have author name. Attribute is null!");
		}
		
		if (this.value.isEmpty()) {
			return new FailedResponse("No expected author given.");
		}

		if (!driver.getAuthor().contains(this.value)) {
			return new FailedResponse("Expected author: \"" + this.value + "\", but found: \"" + driver.getAuthor() + "\"");
		}

		return new PassedResponse("Author successfully found.");
	}
}