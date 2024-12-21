package org.vebqa.vebtal.pdf.commands;

import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PDFDriver;
import org.vebqa.vebtal.pdfrestserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, command = "verifyFieldScript", hintTarget = "name=<partial name>;script=CFKV")
public class Verifyfieldscript extends AbstractCommand {
	
	public Verifyfieldscript(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ASSERTION;
	}

	@Override
	public Response executeImpl(Object driver) {
		PDFDriver pdfDriver = (PDFDriver)driver;

		Response tResp = new Response();
		
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
		
		if (action.contains(PDFDriver.ACTION_CALCULATE)) {
			action = PDFDriver.ACTION_CALCULATE;
		}
		if (action.contains(PDFDriver.ACTION_FORMAT)) {
			action = PDFDriver.ACTION_FORMAT;
		}
		if (action.contains(PDFDriver.ACTION_KEYSTROKE)) {
			action = PDFDriver.ACTION_KEYSTROKE;
		}
		if (action.contains(PDFDriver.ACTION_VALIDATE)) {
			action = PDFDriver.ACTION_VALIDATE;
		}
		
		boolean hasAction = pdfDriver.hasAdditionalActionByFieldName(name, action);
		if (hasAction) {
			tResp.setCode(Response.PASSED);
			tResp.setMessage("Field has the additional action: " + action);
		} else {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Field does not have expected additional action: " + action);
		}
		
		return tResp;
	}
}
