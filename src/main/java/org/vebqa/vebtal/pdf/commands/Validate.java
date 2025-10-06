package org.vebqa.vebtal.pdf.commands;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.preflight.ValidationResult;
import org.apache.pdfbox.preflight.ValidationResult.ValidationError;
import org.apache.pdfbox.preflight.parser.PreflightParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.FailedResponse;
import org.vebqa.vebtal.model.PassedResponse;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdfrestserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, 
         command = "validate",
         hintTarget = "path/to/doc.pdf")
public class Validate extends AbstractCommand {

	public static final Logger logger = LoggerFactory.getLogger( Validate.class);
	
	public Validate(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ASSERTION;
	}

	@Override
	public Response executeImpl(Object driver) {
		ValidationResult result = null;
		
		try {
			result = PreflightParser.validate(new File(this.target));
		} catch (IOException e) {
			logger.warn("Could not validate pdf: {}", e.getMessage());
			return new FailedResponse("Could not validate pdf: " + e.getMessage());
		}
		
		if (!result.isValid()) {
			StringBuilder errorMsg = new StringBuilder();
			int issueCount = 0;
			for (ValidationError error : result.getErrorsList()) {
			    issueCount++;
				if (issueCount > 1) {
					errorMsg.append('\n');
				}
				errorMsg.append(issueCount);
				errorMsg.append(": ");
		        errorMsg.append(error.getErrorCode());
		        errorMsg.append(" : ");
		        errorMsg.append(error.getDetails());
		        if (error.getPageNumber() != null) {
		            errorMsg.append(" on page ");
		            errorMsg.append(error.getPageNumber() + 1);
		        }
		    }
			return new FailedResponse(errorMsg.toString());
		}
		return new PassedResponse("File is a valid PDF/A-1v document.");
	}
}
