package org.opental.pdf.commands;

import org.opental.core.annotations.Keyword;
import org.opental.core.command.AbstractCommand;
import org.opental.core.model.CommandType;
import org.opental.core.model.FailedResponse;
import org.opental.core.model.PassedResponse;
import org.opental.core.model.Response;
import org.opental.pdf.PdfDriver;
import org.opental.pdf.restserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, 
         command = "verifyAuthor", 
         hintValue = "<text>")
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