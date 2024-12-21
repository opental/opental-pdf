package org.vebqa.vebtal.pdf.commands;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PDFDriver;

public class CloseTest {

	@Rule
	public final PDFDriver dut = new PDFDriver().setFilePath("./src/test/java/resource/LoremIpsum_3Pages.pdf");

	@Test
	public void closePdfFile() {
		// create command to test
		Close cmd = new Close("close", "", "");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("Successfully removes SUT from memory.");

		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}

}