package org.opental.pdf.junit5.commands;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PdfDriver;
import org.vebqa.vebtal.pdf.commands.Verifynumberofpages;

class VerifynumberofpagesTest {
	
    private static final Logger logger = LoggerFactory.getLogger(VerifynumberofpagesTest.class);
	
	@Test
	void verifyNumberOfPages() {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		// create command to test
		Verifynumberofpages cmd = new Verifynumberofpages("verifyNumberOfPages", "3", "");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("Document has expected number of pages: 3");

		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
	
	@Test
	void verifyNumberOfPagesFailWithMismatch() {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		
		// create command to test
		Verifynumberofpages cmd = new Verifynumberofpages("verifyNumberOfPages", "1", "");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Expected number of pages: <1>, but found: <3>");

		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
}