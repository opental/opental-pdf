package org.vebqa.vebtal.junit3.pdf.commands;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PDFDriver;
import org.vebqa.vebtal.pdf.commands.Verifycreationdate;

public class VerifycreationdateTest {

	@Rule
	public final PDFDriver dut = new PDFDriver().setFilePath("./src/test/java/resource/LoremIpsum_3Pages.pdf");

	@Test
	public void verifyCreationDate() {
		// create command to test
		Verifycreationdate cmd = new Verifycreationdate("verifyCreationDate", "2019-03-05-16-36-26", "");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("Creation Date successfully matched!");

		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}

	@Test
	public void verifyCreationDateFailWithIncorrectDateFormat() {
		// create command to test
		Verifycreationdate cmd = new Verifycreationdate("verifyCreationDate", "2019-03-05 16-36-26", "");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Cannot parse data: 2019-03-05 16-36-26");

		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}

	@Test
	public void verifyCreationDateFailWithMismatch() {
		// create command to test
		Verifycreationdate cmd = new Verifycreationdate("verifyCreationDate", "2018-03-05-16-36-26", "");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Expected creation date: <2018-03-05-16-36-26>, but found: <2019-03-05-16-36-26>");

		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}

}