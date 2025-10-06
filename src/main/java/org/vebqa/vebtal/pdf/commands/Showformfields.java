package org.vebqa.vebtal.pdf.commands;

import java.io.IOException;

import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.FailedResponse;
import org.vebqa.vebtal.model.PassedResponse;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PdfDriver;
import org.vebqa.vebtal.pdfrestserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, command = "showFormFields")
public class Showformfields extends AbstractCommand {

	public Showformfields(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACTION;
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

		String output = "";
		try {
			output = driver.getFieldsInformation(driver.getDocument());
		} catch (IOException e) {
			return new FailedResponse("Could not get field information: " + e.getMessage());
		}

		if (output == null) {
            return new FailedResponse("Document contains no form field(s)");
		}
		
		return new PassedResponse(output);
	}
}
