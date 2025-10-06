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

@Keyword(module = PdfTestAdaptionPlugin.ID, command = "close")
public class Close extends AbstractCommand {

	public Close(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACTION;
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
		
		try {
			driver.close();
		} catch (IOException e) {
			return new FailedResponse("Error while closing pdf file: " + e.getMessage());
		}
		
		return new PassedResponse("Document successfully removed from memory.");
	}
}
