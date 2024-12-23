package org.vebqa.vebtal.junit3.pdf.commands;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PDFDriver;
import org.vebqa.vebtal.pdf.commands.Extractimages;

public class ExtractimagesTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder() {
		@Override
		public void create() throws IOException {
			super.create();
			assertNotNull(this.getRoot().exists());
		}
	};

	@Rule
	public final PDFDriver dut = new PDFDriver().setFilePath("./src/test/java/resource/SampleFileWithImage.pdf");

	@Rule
	public final PDFDriver dut_ni = new PDFDriver().setFilePath("./src/test/java/resource/SampleFile.pdf");

	// file
	File file = new File("Z:\\extracted-image-1.png");

	@Ignore
	@Test
	public void extractImages() {
		// create command to test
		Extractimages cmd = new Extractimages("extractImages", "page=1", "");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("Successfully extracted 1 image(s) from page: 1");

		// check
		assertTrue(file.exists());
		assertThat(resultCheck, samePropertyValuesAs(result));

		// clean up saved file
		file.delete();
	}

	@Test
	public void extractImagesToSpecificFolder() {
		// save temporary archive directory
		String directory = folder.getRoot().toString();

		// create command to test
		Extractimages cmd = new Extractimages("extractImages", "page=1", directory);
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("Successfully extracted 1 image(s) from page: 1");

		// check
		assertTrue(new File(directory, "extracted-image-1.png").exists());
		assertThat(resultCheck, samePropertyValuesAs(result));
	}

	@Test
	public void noImagesToExtract() {
		// create command to test
		Extractimages cmd = new Extractimages("extractImages", "page=1", "");
		Response result = cmd.executeImpl(dut_ni);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("No images found in page: 1");

		// check
		assertFalse(file.exists());
		assertThat(resultCheck, samePropertyValuesAs(result));
	}

}