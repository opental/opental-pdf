package org.vebqa.vebtal.pdf.commands;

import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.FailedResponse;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PdfDriver;
import org.vebqa.vebtal.pdfrestserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, command = "verifyFieldText", hintTarget = "name=<partial name>", hintValue = "<text>")
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

		Response tResp = new Response();
		
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
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Field " + name + " not found");
			return tResp;
		}
		if (tValue != null && tValue.contentEquals(this.value)) {
			tResp.setCode(Response.PASSED);
			tResp.setMessage("value of field matches given text: " + this.value);
		} else {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Field should be " + this.value + ", but is: " + tValue);
		}
		
		return tResp;
	}
}
