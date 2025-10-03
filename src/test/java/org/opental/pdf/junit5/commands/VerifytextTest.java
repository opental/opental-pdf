package org.opental.pdf.junit5.commands;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PdfDriver;
import org.vebqa.vebtal.pdf.commands.Verifytext;

public class VerifytextTest {

	private static final Logger logger = LoggerFactory.getLogger(VerifytextTest.class);

	@Test
	public void verifyTextWithPageNo() {
		
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}

		// create command to test
		Verifytext cmd = new Verifytext("verifyText", "page=1", "Marker Page 1");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("Expected text <Marker Page 1> found");

		// check
		assertThat(result).usingRecursiveComparison().isEqualTo(resultCheck);
	}

	@Test
	public void verifyTextWithoutPageNo() {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}

		// create command to test
		Verifytext cmd = new Verifytext("verifyText", "", "Marker Page 2");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("Expected text <Marker Page 2> found");

		// check
		assertThat(result).usingRecursiveComparison().isEqualTo(resultCheck);
	}

	@Test
	public void verifyTextNotFoundWithPageNo() {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}

		// create command to test
		Verifytext cmd = new Verifytext("verifyText", "page=2", "Marker Page 1");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Did not find expected text <Marker Page 1>");

		// check
		assertThat(result).usingRecursiveComparison().isEqualTo(resultCheck);
	}

	@Test
	public void verifyTextNotFoundWithoutPageNo() {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}

		// create command to test
		Verifytext cmd = new Verifytext("verifyText", "", "You can't find me!");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Did not find expected text <You can't find me!>");

		// check
		assertThat(result).usingRecursiveComparison().isEqualTo(resultCheck);
	}
}