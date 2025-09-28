package org.opental.pdf.junit5.commands;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PDFDriver;
import org.vebqa.vebtal.pdf.commands.Verifysubject;

public class VerifysubjectTest {

	private static final Logger logger = LoggerFactory.getLogger(VerifysubjectTest.class);

	// public final PDFDriver dut_ns = new PDFDriver().setFilePath("./src/test/java/resource/LoremIpsum500.pdf");

	@Test
	public void verifySubject() {
		PDFDriver dut = null;
		try {
			dut = new PDFDriver().setFilePath("./src/test/resources/SampleFile.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		// create command to test
		Verifysubject cmd = new Verifysubject("verifySubject", "Test Document", "");
		Response result = cmd.executeImpl(dut);
		
		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("Successfully found subject: Test Document");
		
		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
	
	@Test
	public void verifySubjectFailWithoutSubject() {
		PDFDriver dut_ns = null;
		try {
			dut_ns = new PDFDriver().setFilePath("./src/test/resources/LoremIpsum500.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}

		// create command to test
		Verifysubject cmd = new Verifysubject("verifySubject", "Test", "");
		Response result = cmd.executeImpl(dut_ns);
		
		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Document does not have a title. Attribute is null!");
		
		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
	
	@Test
	public void verifySubjectMismatch() {
		PDFDriver dut = null;
		try {
			dut = new PDFDriver().setFilePath("./src/test/resources/SampleFile.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}

		// create command to test
		Verifysubject cmd = new Verifysubject("verifySubject", "Testing", "");
		Response result = cmd.executeImpl(dut);
		
		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Expected subject: \"Testing\", but found: \"Test Document\"");
		
		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
}