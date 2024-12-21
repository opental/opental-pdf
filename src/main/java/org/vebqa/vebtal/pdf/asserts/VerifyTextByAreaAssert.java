package org.vebqa.vebtal.pdf.asserts;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.assertj.core.api.AbstractAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.pdf.Area;
import org.vebqa.vebtal.pdf.PDFDriver;

/**
 * Special assertion class - inherits from AbstractAssert!
 * 
 * @author doerges
 *
 */
public class VerifyTextByAreaAssert extends AbstractAssert<VerifyTextByAreaAssert, PDFDriver> {

	private static final Logger logger = LoggerFactory.getLogger(VerifyTextByAreaAssert.class);

	private String text;
	private int pageNumber;
	private Area area;
	private int areaX;
	private int areaY;
	private int areaWidth;
	private int areaHeight;

	/**
	 * Constructor assertion class, PDF filename ist the object we want to make
	 * assertions on.
	 * 
	 * @param aPdfToTest a document to test
	 */
	public VerifyTextByAreaAssert(PDFDriver aPdfToTest) {
		super(aPdfToTest, VerifyTextByAreaAssert.class);
	}

	/**
	 * A fluent entry point to our specific assertion class, use it with static
	 * import.
	 * 
	 * @param aPdfToTest a document we want to test
	 * @return new object
	 */
	public static VerifyTextByAreaAssert assertThat(PDFDriver aPdfToTest) {
		return new VerifyTextByAreaAssert(aPdfToTest);
	}

	/**
	 * A configuration
	 * 
	 * @param someText some text we are expecting
	 * @return self
	 */
	public VerifyTextByAreaAssert hasText(String someText) {
		this.text = someText;

		return this;
	}

	/**
	 * A configuration
	 * 
	 * @param x      coordinate x
	 * @param y      coordinate y
	 * @param width  width of the area
	 * @param height height of the area
	 * @return self
	 */
	public VerifyTextByAreaAssert inArea(int x, int y, int width, int height) {
		this.areaX = x;
		this.areaY = y;
		this.areaWidth = width;
		this.areaHeight = height;
		return this;
	}

	/**
	 * A configuration
	 * 
	 * @param anArea specify the area to test
	 * @return self
	 */
	public VerifyTextByAreaAssert inArea(Area anArea) {
		this.areaX = (int) anArea.getRectangle().getX();
		this.areaY = (int) anArea.getRectangle().getY();
		this.areaWidth = (int) anArea.getRectangle().getWidth();
		this.areaHeight = (int) anArea.getRectangle().getHeight();
		return this;
	}

	/**
	 * A configuration
	 * 
	 * @param pageNumber a page number we want to test
	 * @return self
	 */
	public VerifyTextByAreaAssert atPage(int pageNumber) {
		this.pageNumber = pageNumber;
		return this;
	}

	/**
	 * A specific assertion
	 * 
	 * @return self
	 */
	public VerifyTextByAreaAssert check() {
		// check that we really have a pdf filename defined.
		isNotNull();

		this.area = new Area(this.pageNumber, this.areaX, this.areaY, this.areaHeight, this.areaWidth);

		String areaText = null;
		try {
			PDFTextStripperByArea textStripper = new PDFTextStripperByArea();
			textStripper.setSortByPosition(true);
			textStripper.addRegion("test", area.getRectangle());

			PDPage page = this.actual.getDocument().getPage(area.getPage());
			textStripper.extractRegions(page);
			areaText = textStripper.getTextForRegion("test");
			logger.info("Extracted text from area: {}", areaText);
		} catch (IOException e) {
			failWithMessage("Cannot extract text from area!");
		}

		if (!areaText.contains(this.text)) {
			failWithMessage("Expected text <%s> is not availabe in the located area. Instead found: <%s>", this.text,
					areaText);
		}

		return this;
	}
}
