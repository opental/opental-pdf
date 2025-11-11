package org.opental.pdf.commands;

import org.opental.core.annotations.Keyword;
import org.opental.core.command.AbstractCommand;
import org.opental.core.model.CommandType;
import org.opental.core.model.FailedResponse;
import org.opental.core.model.PassedResponse;
import org.opental.core.model.Response;
import org.opental.pdf.PdfDriver;
import org.opental.pdf.restserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, 
         command = "verifyNumberOfPages", 
         hintValue = "<int>")
public class Verifynumberofpages extends AbstractCommand {

	public Verifynumberofpages(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ASSERTION;
	}
	
	public Verifynumberofpages() {
		super();
	}
	
	public void setExpectedPages(int expPages) {
		this.value = String.valueOf(expPages);
	}

	public void check(Object aDocument) {
		executeImpl(aDocument);
	}
	
	@Override
	public Response executeImpl(Object aDocument) {

		if (!this.validate()) {
			return new FailedResponse("Input doesnt match validation pattern.");
		}
		
		PdfDriver driver = (PdfDriver)aDocument;
		
		if (!driver.isLoaded()) {
			return new FailedResponse("No document loaded.");
		}

		if (this.value.isEmpty()) {
			return new FailedResponse("No expected value given.");
		}
		
		if (driver.getNumberOfPages() !=  Integer.parseInt(this.value)) {
			return new FailedResponse("Expected number of pages: <" + this.value + ">, but found: <" + driver.getNumberOfPages() + ">");
		}
		return new PassedResponse("Document has expected number of pages: " + this.value);
	}
}