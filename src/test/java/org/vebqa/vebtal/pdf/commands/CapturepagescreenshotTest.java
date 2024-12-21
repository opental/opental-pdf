package org.vebqa.vebtal.pdf.commands;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PDFDriver;

public class CapturepagescreenshotTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder() {
		@Override
		public void create() throws IOException {
			super.create();
			assertNotNull(this.getRoot().exists());
		}
	};

	@Rule
	public final PDFDriver dut = new PDFDriver().setFilePath("./src/test/java/resource/LoremIpsum_3Pages.pdf");

	@Test
	public void captureScreenshotOfGivenPage() {
		String file = folder.getRoot().toString() + "\\pageScreenshot.png";
		Capturepagescreenshot cmd = new Capturepagescreenshot("capturePageScreenshot", "page=2", file);
		Response result = cmd.executeImpl(dut);

		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("Successfully written data to file: " + file);

		assertThat(resultCheck, samePropertyValuesAs(result));
	}

	@Test
	public void captureScreenshotFailWhileSavingImage() {
		Capturepagescreenshot cmd = new Capturepagescreenshot("capturePageScreenshot", "page=2",
				"C:\\Users\\noSuchUser\\screenshot.png");
		Response result = cmd.executeImpl(dut);

		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Could not write Image to file: C:\\Users\\noSuchUser\\screenshot.png");

		assertThat(resultCheck, samePropertyValuesAs(result));
	}

}