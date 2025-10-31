package org.opental.pdf.commands;

import org.apache.commons.codec.digest.DigestUtils;
import org.opental.core.annotations.Keyword;
import org.opental.core.command.AbstractCommand;
import org.opental.core.model.CommandType;
import org.opental.core.model.FailedResponse;
import org.opental.core.model.Response;
import org.opental.pdf.PdfDriver;
import org.opental.pdf.restserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, 
         command = "verifyFieldActionChecksum", 
         hintTarget = "name=<partial name>", 
         hintValue = "<checksum>")
public class Verifyfieldactionchecksum extends AbstractCommand {
	
	public Verifyfieldactionchecksum(String aCommand, String aTarget, String aValue) {
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

		
		String tActionCode = pdfDriver.getActionByFieldName(name, action);
		
		if (tActionCode == null || tActionCode.contentEquals("")) {
			return new Response.Builder()
			    .setCode(Response.FAILED)
			    .setMessage("No script found.")
			    .build();
		}
		
		String tCheckSum = DigestUtils.sha256Hex(tActionCode);
		logger.info(tCheckSum);
		if (tCheckSum != null && tCheckSum.contains(this.value)) {
			tResp = new Response.Builder()
			            .setCode(Response.PASSED)
			            .setMessage("Script checksum ok.")
			            .build();
		} else {
			tResp = new Response.Builder()
			            .setCode(Response.FAILED)
			            .setMessage("Expected checksum [" + tCheckSum + "] but is [" + tCheckSum + "]")
			            .build();
		}
		
		return tResp;
	}
}
