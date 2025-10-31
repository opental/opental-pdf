package org.opental.pdf.junit5.commands;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.opental.core.model.Response;
import org.opental.pdf.PdfDriver;
import org.opental.pdf.commands.Verifysubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class VerifysubjectTest {

	private static final Logger logger = LoggerFactory.getLogger(VerifysubjectTest.class);

	// public final PDFDriver dut_ns = new PDFDriver().setFilePath("./src/test/java/resource/LoremIpsum500.pdf");

	@Test
	void verifySubject() {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/SampleFile.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		// create command to test
		Verifysubject cmd = new Verifysubject("verifySubject", "Test Document", "");
		Response result = cmd.executeImpl(dut);
		
		// create a green result object
		Response resultCheck = new Response.Builder()
		    .setCode(Response.PASSED)
		    .setMessage("Successfully found subject: Test Document")
		    .build();
		
		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
	
	@Test
	void verifySubjectFailWithoutSubject() {
		PdfDriver dut_ns = null;
		try {
			dut_ns = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum500.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}

		// create command to test
		Verifysubject cmd = new Verifysubject("verifySubject", "Test", "");
		Response result = cmd.executeImpl(dut_ns);
		
		// create a green result object
		Response resultCheck = new Response.Builder()
		    .setCode(Response.FAILED)
		    .setMessage("Document does not have a title. Attribute is null!")
		    .build();
		
		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
	
	@Test
	void verifySubjectMismatch() {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/SampleFile.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}

		// create command to test
		Verifysubject cmd = new Verifysubject("verifySubject", "Testing", "");
		Response result = cmd.executeImpl(dut);
		
		// create a green result object
		Response resultCheck = new Response.Builder()
		    .setCode(Response.FAILED)
		    .setMessage("Expected subject: \"Testing\", but found: \"Test Document\"")
		    .build();
		
		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
}