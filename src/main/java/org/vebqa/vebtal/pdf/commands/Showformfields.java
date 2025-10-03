package org.vebqa.vebtal.pdf.commands;

import java.io.IOException;

import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
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
	public Response executeImpl(Object driver) {
		PdfDriver pdfDriver = (PdfDriver) driver;

		Response tResp = new Response();

		String output = "";
		try {
			output = pdfDriver.getFieldsInformation(pdfDriver.getDocument());
		} catch (IOException e) {
			tResp.setCode(Response.FAILED);
			tResp.setMessage(e.getMessage());
		}

		if (output == null) {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Document contains no form field(s)");
		} else {
			tResp.setCode(Response.PASSED);
			tResp.setMessage(output);
		}
		
		return tResp;
	}
}
