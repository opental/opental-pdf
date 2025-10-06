package org.vebqa.vebtal.pdf.commands;

import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.FailedResponse;
import org.vebqa.vebtal.model.PassedResponse;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PdfDriver;
import org.vebqa.vebtal.pdfrestserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, 
         command = "type", 
         hintTarget = "name=<partial name>", 
         hintValue = "<value>")
public class Type extends AbstractCommand {
	
	public Type(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACTION;
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
		
		String name = "";
		
		String[] parts = target.split(";");
		for (String part : parts) {
			String[] subParts = part.split("=");
			switch (subParts[0]) {
			case "name":
				name = subParts[1];
				break;
			default:
				break;
			}
		}		
		
		boolean result = driver.setValueByFieldName(name, this.value);
		if (!result) {
			return new FailedResponse("cannot write to field");
		}
		
		return new PassedResponse("value: " + this.value + " written to field");
	}
}
