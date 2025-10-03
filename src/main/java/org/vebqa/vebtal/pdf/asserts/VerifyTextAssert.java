package org.vebqa.vebtal.pdf.asserts;

import java.io.IOException;

import org.apache.pdfbox.text.PDFTextStripper;
import org.assertj.core.api.AbstractAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.pdf.PDFDriver;

/**
 * Special assertion class - inherits from AbstractAssert!
 * 
 * @author doerges
 *
 */
public class VerifyTextAssert extends AbstractAssert<VerifyTextAssert, PDFDriver> {

	private static final Logger logger = LoggerFactory.getLogger(VerifyTextAssert.class);

	private int page = -1;
	private String textToFind = null;

	/**
	 * Constructor assertion class, PDF filename ist the object we want to make
	 * assertions on.
	 * 
	 * @param aPdfToTest a resource to test
	 */
	public VerifyTextAssert(PDFDriver aPdfToTest) {
		super(aPdfToTest, VerifyTextAssert.class);
	}

	/**
	 * A fluent entry point to our specific assertion class, use it with static
	 * import.
	 * 
	 * @param aDoc our document to test
	 * @return new object
	 */
	public static VerifyTextAssert assertThat(PDFDriver aDoc) {
		return new VerifyTextAssert(aDoc);
	}

	/**
	 * Part of assertion - configure
	 * 
	 * @param someText some text we are expecting
	 * @return self
	 */
	public VerifyTextAssert hasText(String someText) {
		this.textToFind = someText;
		return this;
	}

	/**
	 * Part of assertion - configure
	 * 
	 * @param aPageNumber the page number we want to test
	 * @return self
	 */
	public VerifyTextAssert atPage(int aPageNumber) {
		this.page = aPageNumber;
		return this;
	}

	/**
	 * A specific assertion
	 * 
	 * @return self
	 */
	public VerifyTextAssert check() {
		// check that we really have a pdf document defined and loaded.
		isNotNull();

		if (this.actual.getDocument() == null) {
			failWithMessage("No document loaded!");
			return this;
		}

		String pageText = "";

		try {
			PDFTextStripper stripper = new PDFTextStripper();
			if (page != -1) {
				stripper.setStartPage(page);
				stripper.setEndPage(page);
			}
			pageText = stripper.getText(this.actual.getDocument());
		} catch (IOException e) {
			logger.error("Error while stripping text from pdf document!", e);
			failWithMessage("Cannot read data from file <%s>, maybe it is invalid!", actual);
		}
		if (pageText == null) {
			failWithMessage("Expected text is <%s>, but the page was empty.", this.textToFind);
		}
		if (!pageText.contains(this.textToFind)) {
			failWithMessage("Expected text <%s> not found in the content.", this.textToFind);
		}

		// Hint for designing and creating tests: we want to print the coordinates of
		// the text finding.
		return this;
	}
}