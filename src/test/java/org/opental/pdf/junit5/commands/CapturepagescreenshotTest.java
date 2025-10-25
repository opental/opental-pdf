package org.opental.pdf.junit5.commands;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PdfDriver;
import org.vebqa.vebtal.pdf.commands.Capturepagescreenshot;

class CapturepagescreenshotTest {
	
	private static final Logger logger = LoggerFactory.getLogger(CapturepagescreenshotTest.class);

	@Test
	void captureScreenshotOfGivenPage(@TempDir Path tempDir) {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		
		Path file = tempDir.resolve("pageScreenshot.png");
		Capturepagescreenshot cmd = new Capturepagescreenshot("capturePageScreenshot", "page=2", file.toString());
		Response result = cmd.executeImpl(dut);

		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("Successfully written data to file: " + file.toString());

		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}

	@Test
	void captureScreenshotFailWhileSavingImage() {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/LoremIpsum_3Pages.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		
		Capturepagescreenshot cmd = new Capturepagescreenshot("capturePageScreenshot", "page=2",
				"C:\\Users\\noSuchUser\\screenshot.png");
		Response result = cmd.executeImpl(dut);

		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Could not write Image to file: C:\\Users\\noSuchUser\\screenshot.png");

		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
}