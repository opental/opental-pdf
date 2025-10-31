package org.opental.pdf.commands;

import org.opental.core.annotations.Keyword;
import org.opental.core.command.AbstractCommand;
import org.opental.core.model.CommandType;
import org.opental.core.model.FailedResponse;
import org.opental.core.model.Response;
import org.opental.pdf.PdfDriver;
import org.opental.pdf.restserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, 
         command = "verifyFieldText", 
         hintTarget = "name=<partial name>", 
         hintValue = "<text>")
public class Verifyfieldtext extends AbstractCommand {
	
	public Verifyfieldtext(String aCommand, String aTarget, String aValue) {
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
		
		String[] parts = this.target.split(";");
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
		
		String tValue = pdfDriver.getValueByFieldName(name);
		if (tValue == null)  {
			return new Response.Builder()
			    .setCode(Response.FAILED)
			    .setMessage("Field " + name + " not found")
			    .build();
		}
		if (tValue != null && tValue.contentEquals(this.value)) {
			tResp = new Response.Builder()
			    .setCode(Response.PASSED)
			    .setMessage("value of field matches given text: " + this.value)
			    .build();
		} else {
			tResp = new Response.Builder()
			    .setCode(Response.FAILED)
			    .setMessage("Field should be " + this.value + ", but is: " + tValue)
			    .build();
		}
		
		return tResp;
	}
}
