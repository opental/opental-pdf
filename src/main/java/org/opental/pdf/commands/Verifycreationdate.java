package org.opental.pdf.commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.opental.core.annotations.Keyword;
import org.opental.core.command.AbstractCommand;
import org.opental.core.model.CommandType;
import org.opental.core.model.FailedResponse;
import org.opental.core.model.PassedResponse;
import org.opental.core.model.Response;
import org.opental.pdf.PdfDriver;
import org.opental.pdf.restserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, 
         command = "verifyCreationDate", 
         hintValue = "<yyyy-MM-dd-HH-mm-ss>")
public class Verifycreationdate extends AbstractCommand {

	public Verifycreationdate(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ASSERTION;
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

		if (driver.getCreationDate() == null) {
			return new FailedResponse("Document does not have a creation date. Attribute is null!");
		}
		
		Calendar created = driver.getCreationDate();
		
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	    Date date = null; 
	    try {
			date = sdf.parse(this.value);
		} catch (ParseException e) {
			return new FailedResponse("Cannot parse data: " + this.value);
		}
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    
		if (created.compareTo(cal) != 0) {
			return new FailedResponse("Expected creation date: <" + this.value + ">, but found: <" + sdf.format(created.getTime()) + ">");
		}
		return new PassedResponse("Creation Date successfully matched!");
	}
}