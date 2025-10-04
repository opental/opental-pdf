package org.opental.pdf.junit5.asserts;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.pdf.PdfDriver;
import org.vebqa.vebtal.pdf.asserts.VerifyMetaDataAssert;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VerifyMetaDataAssertTest {

	private static final Logger logger = LoggerFactory.getLogger(VerifyMetaDataAssertTest.class);
	
	public final PdfDriver dut_nt = new PdfDriver().setFilePath("./src/test/java/resource/LoremIpsum500.pdf");

	@Test
	public void checkIfDocumentHasGivenNoOfPages() {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		VerifyMetaDataAssert.assertThat(dut).hasNumberOfPages(3);
	}
	
	@Test
	public void checkIfDocumentDoesNotHaveGivenNoOfPages() {

	    AssertionError exception =
	            assertThrows(AssertionError.class, () -> {
	        		PdfDriver dut = null;
	        		try {
	        			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
	        		} catch (IOException e) {
	        			logger.error("Could not load", e);
	        		}

	            	VerifyMetaDataAssert.assertThat(dut).hasNumberOfPages(5);
	            });
	        assertEquals("Expected no. of pages <5> but was <3>.", exception.getMessage());
	}
	
	@Test
	public void checkIfDocumentHasGivenAuthor() {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}

		VerifyMetaDataAssert.assertThat(dut).hasAuthor("Dörges, Karsten");
	}
	
	@Test
	public void checkIfDocumentDoesNotHaveTheGivenAuthor() {
		
		AssertionError exception =
	            assertThrows(AssertionError.class, () -> {
	        		PdfDriver dut = null;
	        		try {
	        			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
	        		} catch (IOException e) {
	        			logger.error("Could not load", e);
	        		}

	        		VerifyMetaDataAssert.assertThat(dut).hasAuthor("Radjindirin, Nithiyaa");
	            });
	        assertEquals("Expected author is <Radjindirin, Nithiyaa> but was <Dörges, Karsten>", exception.getMessage());
	}
	
	@Test
	public void checkIfDocumentHasGivenCreator() {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}

		VerifyMetaDataAssert.assertThat(dut).hasCreator("Microsoft® Word 2010");
	}
	
	@Test
	public void checkIfDocumentDoesNotHaveGivenCreator() {

		AssertionError exception =
	            assertThrows(AssertionError.class, () -> {
	        		PdfDriver dut = null;
	        		try {
	        			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
	        		} catch (IOException e) {
	        			logger.error("Could not load", e);
	        		}

	            	VerifyMetaDataAssert.assertThat(dut).hasCreator("WRONG_CREATOR");
	            });
	        assertEquals("Expected creator is <WRONG_CREATOR> but was <Microsoft® Word 2010>", exception.getMessage());
	}
	
	@Test
	public void checkIfDocumentHasGivenTitle() {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}

		VerifyMetaDataAssert.assertThat(dut).hasTitle("Test Title");
	}
	
	@Test
	public void checkIfDocumentDoesNotHaveTheGivenTitle() {

		AssertionError exception =
	            assertThrows(AssertionError.class, () -> {
	        		PdfDriver dut = null;
	        		try {
	        			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
	        		} catch (IOException e) {
	        			logger.error("Could not load", e);
	        		}

	            	VerifyMetaDataAssert.assertThat(dut).hasTitle("WRONG TITLE");
	            });
	        assertEquals("Expected title is <WRONG TITLE> but was <Test Title>", exception.getMessage());

	}
	
	@Test
	public void checkIfDocumentHasNoTitle() {

		AssertionError exception =
	            assertThrows(AssertionError.class, () -> {
	        		PdfDriver dut = null;
	        		try {
	        			dut = new PdfDriver().setFilePath("./src/test/resources/EmptyFile.pdf").load();
	        		} catch (IOException e) {
	        			logger.error("Could not load", e);
	        		}

	            	VerifyMetaDataAssert.assertThat(dut).hasTitle("WRONG TITLE");
	            });
	        assertEquals("Expected title is <WRONG TITLE> but there is no title object.", exception.getMessage());
	}
	
	@Test
	public void checkIfDocumentIsNotTrapped() {
		
		AssertionError exception =
	            assertThrows(AssertionError.class, () -> {
	        		PdfDriver dut = null;
	        		try {
	        			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
	        		} catch (IOException e) {
	        			logger.error("Could not load", e);
	        		}

	        		VerifyMetaDataAssert.assertThat(dut).isTrapped("true");
	            });
	        assertEquals("Expected trapped is <true> but was <false>", exception.getMessage());
	}	
}
