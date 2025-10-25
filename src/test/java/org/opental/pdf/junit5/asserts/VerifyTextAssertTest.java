package org.opental.pdf.junit5.asserts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.pdf.PdfDriver;
import org.vebqa.vebtal.pdf.asserts.VerifyTextAssert;

class VerifyTextAssertTest {

	private static final Logger logger = LoggerFactory.getLogger(VerifyTextAssertTest.class);
	
	@Test
	void findTextInPdf() {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		VerifyTextAssert.assertThat(dut).hasText("Duis autem").check();
	}

	@Test
	void failFindingTextInPdf() {
	    AssertionError exception =
	            assertThrows(AssertionError.class, () -> {
	        		PdfDriver dut = null;
	        		try {
	        			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
	        		} catch (IOException e) {
	        			logger.error("Could not load", e);
	        		}
	            	VerifyTextAssert.assertThat(dut).hasText("Duis autem Entenhausen").check();
	            });
	        assertEquals("Expected text <Duis autem Entenhausen> not found in the content.", exception.getMessage());

	}
	
	@Test
	void findTextAtPage_1() {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}

		VerifyTextAssert.assertThat(dut).hasText("Marker Page 1").atPage(1).check();
	}

	@Test
	void findTextAtPage_2() {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}

		VerifyTextAssert.assertThat(dut).hasText("Marker Page 2").atPage(2).check();
	}

	@Test
	void findTextAtPage_3() {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}

		VerifyTextAssert.assertThat(dut).hasText("Marker Page 3").atPage(3).check();
	}

	@Test
	void failFindingTextInPage() {
	    AssertionError exception =
	            assertThrows(AssertionError.class, () -> {
	        		PdfDriver dut = null;
	        		try {
	        			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
	        		} catch (IOException e) {
	        			logger.error("Could not load", e);
	        		}
	            	VerifyTextAssert.assertThat(dut).hasText("Marker Page 2").atPage(1).check();
	            });
	        assertEquals("Expected text <Marker Page 2> not found in the content.", exception.getMessage());

		
	}

}