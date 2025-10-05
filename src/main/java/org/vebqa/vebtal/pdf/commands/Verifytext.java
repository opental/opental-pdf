package org.vebqa.vebtal.pdf.commands;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
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

@Keyword(module = PdfTestAdaptionPlugin.ID, 
         command = "verifyText", 
         hintTarget = "page=<number>",
         hintValue = "<text>")
public class Verifytext extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(Verifytext.class);

	public Verifytext(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ASSERTION;
	}

	@Override
	public Response executeImpl(Object aDocument) {

		PdfDriver driver = (PdfDriver)aDocument;
		
		if (!driver.isLoaded()) {
			return new FailedResponse("No document loaded.");
		}

		int testPage = -1;
		
		if (target != null && !target.contentEquals("")) {
			String[] token = target.split("=");
			testPage = Integer.parseInt(token[1]);
		}
		String pageText = null;
		try {
			InputStream inputStream = new ByteArrayInputStream(driver.getContentStream());
			PDDocument pdf = Loader.loadPDF(new RandomAccessReadBuffer(inputStream));
			int countPages = pdf.getNumberOfPages();
			logger.info("successfully loaded {} pages", countPages);
			PDFTextStripper stripper = new PDFTextStripper();
		    if (testPage > 0 && countPages < testPage) {
			    return new FailedResponse("page to test (" + testPage + ") is higher than number of pages ("+ countPages +")");
			}
			if (testPage > 0) {
				stripper.setStartPage(testPage);
				stripper.setEndPage(testPage);
			}
			pageText = stripper.getText(pdf);
		} catch (IOException e) {
			logger.error("Error while stripping text from pdf document!", e);
		}
		if (pageText == null) {
			return new FailedResponse("No text in focument");
		}
		if (!pageText.contains(value)) {
			return new FailedResponse("Did not find expected text <" + value + ">");
		}

		return new PassedResponse("Expected text <" + value + "> found");
	}
}