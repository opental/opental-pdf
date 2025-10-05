package org.vebqa.vebtal.pdf.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.FailedResponse;
import org.vebqa.vebtal.model.PassedResponse;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PdfDriver;
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

		PdfDriver driver = (PdfDriver)aDocument;
		
		if (driver.isLoaded()) {
			return new FailedResponse("Document already loaded.");
		}
		
		try {
			driver.load(new File(this.target));
		} catch (NoSuchFileException e) {
			logger.warn("File not found: {}", e.getMessage());
			return new FailedResponse("File not found: " + e.getMessage());
		} catch (IOException e) {
			return new FailedResponse("Cannot open pdf for testing: " + e.getMessage());
		}
		
		return new PassedResponse("PDF successfully opend with " + driver.getNumberOfPages() + " Pages.");
	}	
}