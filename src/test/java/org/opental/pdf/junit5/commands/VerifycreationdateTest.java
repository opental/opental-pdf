package org.opental.pdf.junit5.commands;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PdfDriver;
import org.vebqa.vebtal.pdf.commands.Verifycreationdate;

class VerifycreationdateTest {

	private static final Logger logger = LoggerFactory.getLogger(VerifycreationdateTest.class);
	
	@Test
	void verifyCreationDate() {

		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		
		// create command to test
		Verifycreationdate cmd = new Verifycreationdate("verifyCreationDate", "2019-03-05-16-36-26", "");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("Creation Date successfully matched!");

		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}

	@Test
	void verifyCreationDateFailWithIncorrectDateFormat() {

		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		
		// create command to test
		Verifycreationdate cmd = new Verifycreationdate("verifyCreationDate", "2019-03-05 16-36-26", "");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Cannot parse data: 2019-03-05 16-36-26");

		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}

	@Test
	void verifyCreationDateFailWithMismatch() {
		
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		
		// create command to test
		Verifycreationdate cmd = new Verifycreationdate("verifyCreationDate", "2018-03-05-16-36-26", "");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Expected creation date: <2018-03-05-16-36-26>, but found: <2019-03-05-16-36-26>");

		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
}