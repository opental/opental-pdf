package org.opental.pdf.junit5.asserts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.pdf.PDFDriver;
import org.vebqa.vebtal.pdf.asserts.VerifyTextAssert;

public class VerifyTextAssertTest {

	private final static Logger logger = LoggerFactory.getLogger(VerifyTextAssertTest.class);
	
	@Test
	public void findTextInPdf() {
		PDFDriver dut = null;
		try {
			dut = new PDFDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		VerifyTextAssert.assertThat(dut).hasText("Duis autem").check();
	}

	@Test
	public void failFindingTextInPdf() {
	    AssertionError exception =
	            assertThrows(AssertionError.class, () -> {
	        		PDFDriver dut = null;
	        		try {
	        			dut = new PDFDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
	        		} catch (IOException e) {
	        			logger.error("Could not load", e);
	        		}
	            	VerifyTextAssert.assertThat(dut).hasText("Duis autem Entenhausen").check();
	            });
	        assertEquals("Expected text <Duis autem Entenhausen> not found in the content.", exception.getMessage());

	}
	
	@Test
	public void findTextAtPage_1() {
		PDFDriver dut = null;
		try {
			dut = new PDFDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}

		VerifyTextAssert.assertThat(dut).hasText("Marker Page 1").atPage(1).check();
	}

	@Test
	public void findTextAtPage_2() {
		PDFDriver dut = null;
		try {
			dut = new PDFDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}

		VerifyTextAssert.assertThat(dut).hasText("Marker Page 2").atPage(2).check();
	}

	@Test
	public void findTextAtPage_3() {
		PDFDriver dut = null;
		try {
			dut = new PDFDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}

		VerifyTextAssert.assertThat(dut).hasText("Marker Page 3").atPage(3).check();
	}

	@Test
	public void failFindingTextInPage() {
	    AssertionError exception =
	            assertThrows(AssertionError.class, () -> {
	        		PDFDriver dut = null;
	        		try {
	        			dut = new PDFDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
	        		} catch (IOException e) {
	        			logger.error("Could not load", e);
	        		}
	            	VerifyTextAssert.assertThat(dut).hasText("Marker Page 2").atPage(1).check();
	            });
	        assertEquals("Expected text <Marker Page 2> not found in the content.", exception.getMessage());

		
	}

}