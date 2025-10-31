package org.opental.pdf.junit5.asserts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.opental.pdf.PdfDriver;
import org.opental.pdf.asserts.VerifyTextByAreaAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class VerifyTextByAreaAssertTest {
	
	private static final Logger logger = LoggerFactory.getLogger(VerifyTextByAreaAssertTest.class);

	@Test
	void checkIfEntireTextIsAvailabeInSpecificArea() {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/Testtext_Area.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}

		VerifyTextByAreaAssert.assertThat(dut).hasText("This is a text.").atPage(1).inArea(350, 220, 65, 20).check();
	}

	@Test
	void checkThatTextFragmentIsAvailabeInSpecificArea() {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/Testtext_Area.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}

		VerifyTextByAreaAssert.assertThat(dut).hasText("text.").atPage(1).inArea(390, 220, 25, 20).check();
	}

	@Test
	void failWhileTextIsNotAvailabeInSpecifiedArea() {
	    AssertionError exception =
	            assertThrows(AssertionError.class, () -> {
	        		PdfDriver dut = null;
	        		try {
	        			dut = new PdfDriver().setFilePath("./src/test/resources/Testtext_Area.pdf").load();
	        		} catch (IOException e) {
	        			logger.error("Could not load", e);
	        		}

	        		VerifyTextByAreaAssert.assertThat(dut).hasText("This is a text.").atPage(1).inArea(1, 1, 65, 20).check();
	            });
	        assertThat(exception.getMessage()).isEqualTo("Expected text <This is a text.> is not availabe in the located area. Instead found: <\r\n>");
	}

	@Test
	void failWhileOnlyTextFragmentIsAvailabeInSpecificArea() {
	    AssertionError exception =
	            assertThrows(AssertionError.class, () -> {
	        		PdfDriver dut = null;
	        		try {
	        			dut = new PdfDriver().setFilePath("./src/test/resources/Testtext_Area.pdf").load();
	        		} catch (IOException e) {
	        			logger.error("Could not load", e);
	        		}

	        		VerifyTextByAreaAssert.assertThat(dut).hasText("This is a text.").atPage(1).inArea(390, 220, 25, 20).check();
	            });
	        assertEquals("Expected text <This is a text.> is not availabe in the located area. Instead found: <text. \r\n>", exception.getMessage());
	}
}