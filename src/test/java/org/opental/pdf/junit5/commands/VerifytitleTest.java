package org.opental.pdf.junit5.commands;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PdfDriver;
import org.vebqa.vebtal.pdf.commands.Verifytitle;

public class VerifytitleTest {

	private static final Logger logger = LoggerFactory.getLogger(VerifytitleTest.class);
	
	@Test
	public void verifyTitle() {
		
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
		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("Successfully found title: Test Title");
		
		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
	
	@Test
	public void verifyTitleFailWithoutTitle() {
		
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
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Document does not have a title. Attribute is null!");
		
		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
	
	@Test
	public void verifyTitleMismatch() {
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
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Expected title was <Uhm> but found <Test Title>.");
		
		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
}