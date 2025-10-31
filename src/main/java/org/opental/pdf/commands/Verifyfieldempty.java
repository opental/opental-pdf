package org.opental.pdf.commands;

import org.opental.core.annotations.Keyword;
import org.opental.core.command.AbstractCommand;
import org.opental.core.model.CommandType;
import org.opental.core.model.FailedResponse;
import org.opental.core.model.Response;
import org.opental.pdf.PdfDriver;
import org.opental.pdf.restserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, 
         command = "verifyFieldEmpty", 
         hintTarget = "name=<partial name>")
public class Verifyfieldempty extends AbstractCommand {
	
	public Verifyfieldempty(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ASSERTION;
	}

	@Override
	public Response executeImpl(Object driver) {
		
		if (!this.validate()) {
			return new FailedResponse("Input doesnt match validation pattern.");
		}
		
		PdfDriver pdfDriver = (PdfDriver)driver;

		Response tResp;
		
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
			tResp = new Response.Builder()
			            .setCode(Response.PASSED)
			            .setMessage("value of field is empty")
			            .build();
		} else {
			tResp = new Response.Builder()
			            .setCode(Response.FAILED)
			            .setMessage("Field should be empty, but is: " + value)
			            .build();
		}
		
		return tResp;
	}
}
