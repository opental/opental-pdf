package org.vebqa.vebtal.pdf.commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.FailedResponse;
import org.vebqa.vebtal.model.PassedResponse;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PdfDriver;
import org.vebqa.vebtal.pdfrestserver.PdfTestAdaptionPlugin;

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