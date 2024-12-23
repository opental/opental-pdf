package org.vebqa.vebtal.junit3.pdf.commands;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PDFDriver;
import org.vebqa.vebtal.pdf.commands.Verifytext;

public class VerifytextTest {

	@Rule
	public final PDFDriver dut = new PDFDriver().setFilePath("./src/test/java/resource/LoremIpsum_3Pages.pdf");

	@Test
	public void verifyTextWithPageNo() {
		// create command to test
		Verifytext cmd = new Verifytext("verifyText", "page=1", "Marker Page 1");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("Expected text <Marker Page 1> found in page: 1");

		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}

	@Test
	public void verifyTextWithoutPageNo() {
		// create command to test
		Verifytext cmd = new Verifytext("verifyText", "", "Marker Page 2");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("Successfully found text: Marker Page 2");

		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}

	@Test
	public void verifyTextNotFoundWithPageNo() {
		// create command to test
		Verifytext cmd = new Verifytext("verifyText", "page=2", "Marker Page 1");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Did not find expected text <Marker Page 1> in page: 2");

		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}

	@Test
	public void verifyTextNotFoundWithoutPageNo() {
		// create command to test
		Verifytext cmd = new Verifytext("verifyText", "", "You can't find me!");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Cannot find text: You can't find me!");

		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}

}
