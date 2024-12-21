package org.vebqa.vebtal.pdf.commands;

import java.io.IOException;

import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PDFDriver;
import org.vebqa.vebtal.pdfrestserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, command = "close")
public class Close extends AbstractCommand {

	public Close(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACTION;
	}

	@Override
	public Response executeImpl(Object aDocument) {
		
		PDFDriver driver = (PDFDriver)aDocument;

		Response tResp = new Response();

		try {
			driver.close();
		} catch (IOException e) {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Error while closing pdf file: " + e.getMessage());
			return tResp;
		}
		
		tResp.setCode(Response.PASSED);
		tResp.setMessage("Successfully removes SUT from memory.");
		
		return tResp;
	}
}
