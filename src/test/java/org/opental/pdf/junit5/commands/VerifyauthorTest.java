package org.opental.pdf.junit5.commands;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PDFDriver;
import org.vebqa.vebtal.pdf.commands.Verifyauthor;

public class VerifyauthorTest {

	private static final Logger logger = LoggerFactory.getLogger(VerifyauthorTest.class);
	
	@Test
	public void verifyAuthor() {
		
		PDFDriver dut = null;
		try {
			dut = new PDFDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		
		// create command to test
		Verifyauthor cmd = new Verifyauthor("verifyAuthor", "Dörges, Karsten", "");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("Successfully found author: Dörges, Karsten");

		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}

	@Test
	public void verifyAuthorMismatch() {
		
		PDFDriver dut = null;
		try {
			dut = new PDFDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		
		// create command to test
		Verifyauthor cmd = new Verifyauthor("verifyAuthor", "Radjindirin, Nithiyaa", "");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Expected author: \"Radjindirin, Nithiyaa\", but found: \"Dörges, Karsten\"");

		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}

	@Test
	public void verifyAuthorFailWithoutAuthorName() {
		
		PDFDriver dut_na = null;
		try {
			dut_na = new PDFDriver().setFilePath("./src/test/resources/LoremIpsum500.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		
		// create command to test
		Verifyauthor cmd = new Verifyauthor("verifyAuthor", "Dörges, Karsten", "");
		Response result = cmd.executeImpl(dut_na);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Document does not have author name. Attribute is null!");

		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
}