package org.vebqa.vebtal.pdf.commands;

import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PDFDriver;
import org.vebqa.vebtal.pdfrestserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, command = "verifyTitle", hintTarget = "<string>")
public class Verifytitle extends AbstractCommand {

	public Verifytitle(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ASSERTION;
	}

	@Override
	public Response executeImpl(Object aDocument) {

		PDFDriver driver = (PDFDriver)aDocument;
		
		Response tResp = new Response();

		if (driver.title == null) {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Document does not have a title. Attribute is null!");
			return tResp;
		}
		
		if (driver.title.contains(this.target)) {
			tResp.setCode(Response.PASSED);
			tResp.setMessage("Successfully found title: " + this.target);
		} else {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Expected title was <" + this.target + "> but found <" + driver.title + ">.");
		}
		return tResp;
	}
}