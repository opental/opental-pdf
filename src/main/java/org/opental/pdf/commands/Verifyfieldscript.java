package org.opental.pdf.commands;

import org.opental.core.annotations.Keyword;
import org.opental.core.command.AbstractCommand;
import org.opental.core.model.CommandType;
import org.opental.core.model.FailedResponse;
import org.opental.core.model.Response;
import org.opental.pdf.PdfDriver;
import org.opental.pdf.restserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, 
         command = "verifyFieldScript", 
         hintTarget = "name=<partial name>;script=<CFKV>")
public class Verifyfieldscript extends AbstractCommand {
	
	public Verifyfieldscript(String aCommand, String aTarget, String aValue) {
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
		String action = "";
		
		String[] parts = this.target.split(";");
		for (String part : parts) {
			String[] subParts = part.split("=");
			switch (subParts[0]) {
			case "name":
				name = subParts[1];
				break;
			case "action":
				action = subParts[1];
				action = action.toLowerCase().trim();
				break;

			default:
				break;
			}
		}		
		
		if (action.contains(PdfDriver.ACTION_CALCULATE)) {
			action = PdfDriver.ACTION_CALCULATE;
		}
		if (action.contains(PdfDriver.ACTION_FORMAT)) {
			action = PdfDriver.ACTION_FORMAT;
		}
		if (action.contains(PdfDriver.ACTION_KEYSTROKE)) {
			action = PdfDriver.ACTION_KEYSTROKE;
		}
		if (action.contains(PdfDriver.ACTION_VALIDATE)) {
			action = PdfDriver.ACTION_VALIDATE;
		}
		
		boolean hasAction = pdfDriver.hasAdditionalActionByFieldName(name, action);
		if (hasAction) {
			tResp = new Response.Builder()
			            .setCode(Response.PASSED)
			            .setMessage("Field has the additional action: " + action)
			            .build();
		} else {
			tResp = new Response.Builder()
			            .setCode(Response.FAILED)
			            .setMessage("Field does not have expected additional action: " + action)
			            .build();
		}
		
		return tResp;
	}
}
