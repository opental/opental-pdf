package org.vebqa.vebtal.pdf.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PDFDriver;
import org.vebqa.vebtal.pdfrestserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, command = "open", hintTarget = "path/to/doc.pdf")
public class Open extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(Open.class);
	
	public Open(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACTION;
	}

	@Override
	public Response executeImpl(Object aDocument) {

		PDFDriver driver = (PDFDriver)aDocument;
		
		Response tResp = new Response();
		boolean successfullyLoaded = false;
		
		try {
			driver.load(new File(this.target));
			successfullyLoaded = true;
		} catch (NoSuchFileException e) {
			logger.error("File not found: {}.", e.getMessage());
			tResp.setCode(Response.FAILED);
			tResp.setMessage("File not found: " + e.getMessage());
		} catch (IOException e) {
			logger.error("Cannot open pdf for testing: {}", e.getMessage());
			tResp.setCode(Response.FAILED);
			tResp.setMessage(e.getMessage());
		}
		
		if ( successfullyLoaded && driver.getContentStream() == null ) {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Cannot process pdf: " + this.target);
		} else if (successfullyLoaded) {
			tResp.setCode(Response.PASSED);
			tResp.setMessage("SUT file successfully read.");
			logger.info("PDF successfully opend with {} Pages. ", driver.getNumberOfPages());
		}
		return tResp;
	}
	
}