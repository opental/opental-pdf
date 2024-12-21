package org.vebqa.vebtal.pdf.commands;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PDFDriver;
import org.vebqa.vebtal.pdfrestserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, command = "verifyText", hintTarget = "page=")
public class Verifytext extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(Verifytext.class);

	public Verifytext(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ASSERTION;
	}

	/**
	 * | command | target | value | 
	 * 
	 * | verifyText | | text | 
	 * 
	 * | verifyText | page=n | text |
	 */
	@Override
	public Response executeImpl(Object aDocument) {

		PDFDriver driver = (PDFDriver)aDocument;

		Response tResp = new Response();

		if (target == null || target.contentEquals("")) {
			Matcher<PDFDriver> matcher = driver.containsText(value);

			if (!driver.isLoaded()) {
				tResp.setCode(Response.FAILED);
				tResp.setMessage("No SUT loaded yet. Cannot test against null.");
			} else if (matcher.matches(driver)) {
				tResp.setCode(Response.PASSED);
				tResp.setMessage("Successfully found text: " + value);
			} else {
				tResp.setCode(Response.FAILED);
				tResp.setMessage("Cannot find text: " + value);
			}
		} else {
			// resolve target
			String[] token = target.split("=");
			// token[0] ist page
			String pageText = null;
			try {
				PDFTextStripper stripper = new PDFTextStripper();
				stripper.setStartPage(Integer.parseInt(token[1]));
				stripper.setEndPage(Integer.parseInt(token[1]));
				
				InputStream inputStream = new ByteArrayInputStream(driver.getContentStream());
				PDDocument pdf = PDDocument.load(inputStream);
				pageText = stripper.getText(pdf);
			} catch (IOException e) {
				logger.error("Error while stripping text from pdf document!", e);
			}
			if (pageText != null && pageText.contains(value)) {
				tResp.setCode(Response.PASSED);
				tResp.setMessage("Expected text <" + value + "> found in page: " + token[1]);
			} else {
				tResp.setCode(Response.FAILED);
				tResp.setMessage("Did not find expected text <" + value + "> in page: " + token[1]);
			}
		}
		return tResp;
	}
}