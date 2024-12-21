package org.vebqa.vebtal.pdf.commands;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PDFDriver;

public class VerifynumberofpagesTest {

	@Rule
	public final PDFDriver dut = new PDFDriver().setFilePath("./src/test/java/resource/LoremIpsum_3Pages.pdf");

	@Test
	public void verifyNumberOfPages() {
		// create command to test
		Verifynumberofpages cmd = new Verifynumberofpages("verifyNumberOfPages", "3", "");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("Document has expected number of pages: 3");

		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}
	
	@Test
	public void verifyNumberOfPagesFailWithMismatch() {
		// create command to test
		Verifynumberofpages cmd = new Verifynumberofpages("verifyNumberOfPages", "1", "");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Expected number of pages: <1>, but found: <3>");

		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}

}