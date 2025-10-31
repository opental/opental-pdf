package org.opental.pdf.junit5.commands;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.opental.core.model.Response;
import org.opental.pdf.PdfDriver;
import org.opental.pdf.commands.Verifytitle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class VerifytitleTest {

	private static final Logger logger = LoggerFactory.getLogger(VerifytitleTest.class);
	
	@Test
	void verifyTitle() {
		
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		
		// create command to test
		Verifytitle cmd = new Verifytitle("verifyTitle", "Test Title", "");
		Response result = cmd.executeImpl(dut);
		
		// create a green result object
		Response resultCheck = new Response.Builder()
		    .setCode(Response.PASSED)
		    .setMessage("Successfully found title: Test Title")
		    .build();
		
		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
	
	@Test
	void verifyTitleFailWithoutTitle() {
		
		PdfDriver dut_nt = null;
		try {
			dut_nt = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum500.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		
		// create command to test
		Verifytitle cmd = new Verifytitle("verifyTitle", "Uhm", "");
		Response result = cmd.executeImpl(dut_nt);
		
		// create a green result object
		Response resultCheck = new Response.Builder()
		    .setCode(Response.FAILED)
		    .setMessage("Document does not have a title. Attribute is null!")
		    .build();
		
		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
	
	@Test
	void verifyTitleMismatch() {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		
		// create command to test
		Verifytitle cmd = new Verifytitle("verifyTitle", "Uhm", "");
		Response result = cmd.executeImpl(dut);
		
		// create a green result object
		Response resultCheck = new Response.Builder()
		    .setCode(Response.FAILED)
		    .setMessage("Expected title was <Uhm> but found <Test Title>.")
		    .build();
		
		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
}