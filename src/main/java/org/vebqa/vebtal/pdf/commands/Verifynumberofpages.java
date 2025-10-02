package org.vebqa.vebtal.pdf.commands;

import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PDFDriver;
import org.vebqa.vebtal.pdfrestserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, command = "verifyNumberOfPages", hintTarget = "<int>")
public class Verifynumberofpages extends AbstractCommand {

	public Verifynumberofpages(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ASSERTION;
	}

	@Override
	public Response executeImpl(Object aDocument) {

		PDFDriver driver = (PDFDriver)aDocument;

		Response tResp = new Response();

		if (driver.getNumberOfPages() == Integer.parseInt(target)) {
			tResp.setCode(Response.PASSED);
			tResp.setMessage("Document has expected number of pages: " + target);
		} else {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Expected number of pages: <" + target + ">, but found: <" + driver.getNumberOfPages() + ">");
		}
		return tResp;
	}
}