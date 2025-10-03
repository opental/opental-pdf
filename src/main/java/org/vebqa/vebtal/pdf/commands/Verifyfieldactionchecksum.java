package org.vebqa.vebtal.pdf.commands;

import org.apache.commons.codec.digest.DigestUtils;
import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PdfDriver;
import org.vebqa.vebtal.pdfrestserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, command = "verifyFieldActionChecksum", hintTarget = "name=<partial name>", hintValue = "<checksum>")
public class Verifyfieldactionchecksum extends AbstractCommand {
	
	public Verifyfieldactionchecksum(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ASSERTION;
	}

	@Override
	public Response executeImpl(Object driver) {
		PdfDriver pdfDriver = (PdfDriver)driver;

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
			tResp.setCode(Response.FAILED);
			tResp.setMessage("No script found.");
			return tResp;
		}
		
		String tCheckSum = DigestUtils.sha256Hex(tActionCode);
		System.out.println(tCheckSum);
		if (tCheckSum != null && tCheckSum.contains(this.value)) {
			tResp.setCode(Response.PASSED);
			tResp.setMessage("Script checksum ok.");
		} else {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Expected checksum [" + tCheckSum + "] but is [" + tCheckSum + "]");
		}
		
		return tResp;
	}
}
