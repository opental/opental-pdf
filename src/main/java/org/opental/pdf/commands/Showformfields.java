package org.opental.pdf.commands;

import java.io.IOException;

import org.opental.core.annotations.Keyword;
import org.opental.core.command.AbstractCommand;
import org.opental.core.model.CommandType;
import org.opental.core.model.FailedResponse;
import org.opental.core.model.PassedResponse;
import org.opental.core.model.Response;
import org.opental.pdf.PdfDriver;
import org.opental.pdf.restserver.PdfTestAdaptionPlugin;

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
