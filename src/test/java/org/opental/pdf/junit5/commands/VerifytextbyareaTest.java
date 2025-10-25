package org.opental.pdf.junit5.commands;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PdfDriver;
import org.vebqa.vebtal.pdf.commands.Verifytextbyarea;

class VerifytextbyareaTest {

	private static final Logger logger = LoggerFactory.getLogger(VerifytextbyareaTest.class);

	@Test
	void verifyTextByArea() {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/Testtext_Area.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}

		// create command to test
		Verifytextbyarea cmd = new Verifytextbyarea("verifyTextByArea", "page=1;x=350;y=220;height=20;width=65", "This is a text.");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("Text found in given area.");

		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
	
	@Test
	void verifyTextByAreaFailWithMismatch() {
		
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/Testtext_Area.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		// create command to test
		Verifytextbyarea cmd = new Verifytextbyarea("verifyTextByArea", "page=1;x=350;y=220;height=65;width=20", "This is a text.");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Unable to find text in area! Result is: This\\r\\n");

		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
	
	@Test
	void verifyTextByAreaFailWhileValueIsNull() {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/Testtext_Area.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		
		// create command to test
		Verifytextbyarea cmd = new Verifytextbyarea("verifyTextByArea", "page=1;x=350;y=220;height=20;width=65", "");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Command needs target and value data to work!");

		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
	
	@Test
	void verifyTextByAreaFailWhileTargetIsNull() {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/Testtext_Area.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		
		// create command to test
		Verifytextbyarea cmd = new Verifytextbyarea("verifyTextByArea", "", "This is a text.");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Command needs target and value data to work!");

		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
}