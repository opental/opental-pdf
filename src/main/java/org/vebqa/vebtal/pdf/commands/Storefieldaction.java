package org.vebqa.vebtal.pdf.commands;

import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PdfDriver;
import org.vebqa.vebtal.pdfrestserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, command = "storeFieldAction", hintTarget = "name=<partial name>", hintValue = "<buffer>")
public class Storefieldaction extends AbstractCommand {
	
	public Storefieldaction(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACCESSOR;
	}

	@Override
	public Response executeImpl(Object driver) {
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
		
		String tValue = pdfDriver.getActionByFieldName(name);
		if (tValue != null && !tValue.contentEquals("")) {
			tResp.setCode(Response.PASSED);
			tResp.setMessage(tValue);
			tResp.setStoredKey(this.value);
			tResp.setStoredValue(tValue);
		} else {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Field has no action");
		}
		
		return tResp;
	}
}
