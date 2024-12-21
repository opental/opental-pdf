package org.vebqa.vebtal.pdf.commands;

import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PDFDriver;
import org.vebqa.vebtal.pdfrestserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, command = "verifyFieldEmpty", hintTarget = "name=<partial name>")
public class Verifyfieldempty extends AbstractCommand {
	
	public Verifyfieldempty(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ASSERTION;
	}

	@Override
	public Response executeImpl(Object driver) {
		PDFDriver pdfDriver = (PDFDriver)driver;

		Response tResp = new Response();
		
		String name = "";
		
		String[] parts = target.split(";");
		for (String part : parts) {
			String[] subParts = part.split("=");
			switch (subParts[0]) {
			case "name":
				name = subParts[1];
				break;
			default:
				break;
			}
		}		
		
		String value = pdfDriver.getValueByFieldName(name);
		if (value == null || value.contentEquals("")) {
			tResp.setCode(Response.PASSED);
			tResp.setMessage("value of field is empty");
		} else {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Field should be empty, but is: " + value);
		}
		
		return tResp;
	}

}
