package org.vebqa.vebtal.pdf.commands;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PDFDriver;

public class VerifytitleTest {

	@Rule
	public final PDFDriver dut = new PDFDriver().setFilePath("./src/test/java/resource/LoremIpsum_3Pages.pdf");
	
	@Rule
	public final PDFDriver dut_nt = new PDFDriver().setFilePath("./src/test/java/resource/LoremIpsum500.pdf");

	@Test
	public void verifyTitle() {
		// create command to test
		Verifytitle cmd = new Verifytitle("verifyTitle", "Test Title", "");
		Response result = cmd.executeImpl(dut);
		
		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("Successfully found title: Test Title");
		
		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}
	
	@Test
	public void verifyTitleFailWithoutTitle() {
		// create command to test
		Verifytitle cmd = new Verifytitle("verifyTitle", "Uhm", "");
		Response result = cmd.executeImpl(dut_nt);
		
		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Document does not have a title. Attribute is null!");
		
		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}
	
	@Test
	public void verifyTitleMismatch() {
		// create command to test
		Verifytitle cmd = new Verifytitle("verifyTitle", "Uhm", "");
		Response result = cmd.executeImpl(dut);
		
		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Expected title was <Uhm> but found <Test Title>.");
		
		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}
	
}
