package org.opental.pdf.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

import org.opental.core.annotations.Keyword;
import org.opental.core.command.AbstractCommand;
import org.opental.core.model.CommandType;
import org.opental.core.model.FailedResponse;
import org.opental.core.model.PassedResponse;
import org.opental.core.model.Response;
import org.opental.pdf.PdfDriver;
import org.opental.pdf.restserver.PdfTestAdaptionPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Keyword(module = PdfTestAdaptionPlugin.ID, 
         command = "open", 
         hintTarget = "path/to/doc.pdf")
public class Open extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(Open.class);
	
	public Open(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACTION;
	}

	@Override
	public Response executeImpl(Object aDocument) {

		if (!this.validate()) {
			return new FailedResponse("Input doesnt match validation pattern.");
		}
		
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