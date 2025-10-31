package org.opental.pdf.junit5.commands;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.opental.core.model.Response;
import org.opental.pdf.PdfDriver;
import org.opental.pdf.commands.Extractimages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ExtractimagesTest {

    private static final Logger logger = LoggerFactory.getLogger(ExtractimagesTest.class);
	
	// file
	File file = new File("Z:\\extracted-image-1.png");
	
	@Test
	void extractImages(@TempDir Path tempDir) {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/SampleFileWithImage.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		// create command to test
		Extractimages cmd = new Extractimages("extractImages", "page=1", "");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response.Builder()
		    .setCode(Response.PASSED)
		    .setMessage("Successfully extracted 1 image(s) from page: 1")
		    .build();

		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);

		// clean up saved file
		file.delete();
	}

	@Test
	void extractImagesToSpecificFolder(@TempDir Path tempDir) {
		PdfDriver dut = null;
		try {
			dut = new PdfDriver().setFilePath("./src/test/resources/SampleFileWithImage.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		// save temporary archive directory
		String directory = tempDir.getRoot().toString();

		// create command to test
		Extractimages cmd = new Extractimages("extractImages", "page=1", directory);
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response.Builder()
		    .setCode(Response.PASSED)
		    .setMessage("Successfully extracted 1 image(s) from page: 1")
		    .build();

		// check
		assertThat(new File(directory, "extracted-image-1.png").exists());
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}

	@Test
	void noImagesToExtract() {
		PdfDriver dut_ni = null;
		try {
			dut_ni = new PdfDriver().setFilePath("./src/test/resources/SampleFile.pdf").load();
		} catch (IOException e) {
			logger.error("Could not load", e);
		}
		// create command to test
		Extractimages cmd = new Extractimages("extractImages", "page=1", "");
		Response result = cmd.executeImpl(dut_ni);

		// create a green result object
		Response resultCheck = new Response.Builder()
		    .setCode(Response.PASSED)
		    .setMessage("No images found in page: 1")
		    .build();

		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
}