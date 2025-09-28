package org.vebqa.vebtal.pdf.commands;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.preflight.ValidationResult;
import org.apache.pdfbox.preflight.ValidationResult.ValidationError;
import org.apache.pdfbox.preflight.parser.PreflightParser;
import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdfrestserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, command = "validate", hintTarget = "path/to/current.png")
public class Validate extends AbstractCommand {

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
			e.printStackTrace();
		}
		
		Response tResp = new Response();
		
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
		                errorMsg.append(" on page " + (error.getPageNumber() + 1));
		            }
		        }
			tResp.setCode(Response.FAILED);
			tResp.setMessage(errorMsg.toString());
			return tResp;
		}
		
		tResp.setCode(Response.PASSED);
		tResp.setMessage("File is a valid PDF/A-1v document.");
		return tResp;
	}

}
