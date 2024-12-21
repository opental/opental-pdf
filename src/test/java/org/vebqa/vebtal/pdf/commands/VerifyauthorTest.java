package org.vebqa.vebtal.pdf.commands;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PDFDriver;

public class VerifyauthorTest {

	@Rule
	public final PDFDriver dut = new PDFDriver().setFilePath("./src/test/java/resource/LoremIpsum_3Pages.pdf");

	@Rule
	public final PDFDriver dut_na = new PDFDriver().setFilePath("./src/test/java/resource/LoremIpsum500.pdf");

	@Test
	public void verifyAuthor() {
		// create command to test
		Verifyauthor cmd = new Verifyauthor("verifyAuthor", "Dörges, Karsten", "");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("Successfully found author: Dörges, Karsten");

		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}

	@Test
	public void verifyAuthorMismatch() {
		// create command to test
		Verifyauthor cmd = new Verifyauthor("verifyAuthor", "Radjindirin, Nithiyaa", "");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Expected author: \"Radjindirin, Nithiyaa\", but found: \"Dörges, Karsten\"");

		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}

	@Test
	public void verifyAuthorFailWithoutAuthorName() {
		// create command to test
		Verifyauthor cmd = new Verifyauthor("verifyAuthor", "Dörges, Karsten", "");
		Response result = cmd.executeImpl(dut_na);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Document does not have author name. Attribute is null!");

		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}

}
