package org.vebqa.vebtal.pdf.commands;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.FailedResponse;
import org.vebqa.vebtal.model.PassedResponse;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.Area;
import org.vebqa.vebtal.pdf.PdfDriver;
import org.vebqa.vebtal.pdfrestserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, 
         command = "storeTextByArea", 
         hintTarget = "page=;x=;y=;height=;width=", 
         hintValue = "<variable>")
public class Storetextbyarea extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(Storetextbyarea.class);

	public Storetextbyarea(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACCESSOR;
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
		

		if (target == null || target.contentEquals("") || value == null || value.contentEquals("")) {
			return new FailedResponse("Command needs target and value data to work!");
		} else {
			// target to area
			Area area;
			try {
				area = new Area(target);
			} catch (Exception e) {
				return new FailedResponse("Could not create area definition!");
			}

			String areaText = null;

			try {
				PDFTextStripperByArea textStripper = new PDFTextStripperByArea();
				textStripper.setSortByPosition(true);
				// area of interest will get the logical name "test"
				textStripper.addRegion("test", area.getRectangle());

				InputStream inputStream = new ByteArrayInputStream(
						driver.getContentStream());
				
				PDDocument pdf = Loader.loadPDF(new RandomAccessReadBuffer(inputStream));
						
				PDPage page = pdf.getPage(area.getPage());
				textStripper.extractRegions(page);
				// get text from our area of interest
				areaText = textStripper.getTextForRegion("test");
				logger.info("Extracted text from area: {}", areaText);
			} catch (IOException e) {
				return new FailedResponse("Cannot handle pdf source: " + e.getMessage());
			}

			if (areaText == null) {
				return new FailedResponse("Unable to find text in area. Area is empty!");
			}
		}
		return new PassedResponse("Text found in given area.");
	}
}