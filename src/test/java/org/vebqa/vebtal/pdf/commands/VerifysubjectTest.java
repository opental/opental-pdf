package org.vebqa.vebtal.pdf.commands;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PDFDriver;

public class VerifysubjectTest {

	@Rule
	public final PDFDriver dut = new PDFDriver().setFilePath("./src/test/java/resource/SampleFile.pdf");

	@Rule
	public final PDFDriver dut_ns = new PDFDriver().setFilePath("./src/test/java/resource/LoremIpsum500.pdf");

	@Test
	public void verifySubject() {
		// create command to test
		Verifysubject cmd = new Verifysubject("verifySubject", "Test Document", "");
		Response result = cmd.executeImpl(dut);
		
		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("Successfully found subject: Test Document");
		
		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}
	
	@Test
	public void verifySubjectFailWithoutSubject() {
		// create command to test
		Verifysubject cmd = new Verifysubject("verifySubject", "Test", "");
		Response result = cmd.executeImpl(dut_ns);
		
		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Document does not have a title. Attribute is null!");
		
		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}
	
	@Test
	public void verifySubjectMismatch() {
		// create command to test
		Verifysubject cmd = new Verifysubject("verifySubject", "Testing", "");
		Response result = cmd.executeImpl(dut);
		
		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Expected subject: \"Testing\", but found: \"Test Document\"");
		
		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}
	
}