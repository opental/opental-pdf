package org.vebqa.vebtal.pdf.commands;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PDFDriver;

public class VerifytextbyareaTest {

	@Rule
	public final PDFDriver dut = new PDFDriver().setFilePath("./src/test/java/resource/Testtext_Area.pdf");

	@Test
	public void verifyTextByArea() {
		// create command to test
		Verifytextbyarea cmd = new Verifytextbyarea("verifyTextByArea", "page=1;x=350;y=220;height=20;width=65", "This is a text.");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("Text found in given area.");

		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}
	
	@Test
	public void verifyTextByAreaFailWithMismatch() {
		// create command to test
		Verifytextbyarea cmd = new Verifytextbyarea("verifyTextByArea", "page=1;x=350;y=220;height=65;width=20", "This is a text.");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Could not find text in area! Result is: This\\r\\n");

		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}
	
	@Test
	public void verifyTextByAreaFailWhileValueIsNull() {
		// create command to test
		Verifytextbyarea cmd = new Verifytextbyarea("verifyTextByArea", "page=1;x=350;y=220;height=20;width=65", "");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Command needs target and value data to work!");

		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}
	
	@Test
	public void verifyTextByAreaFailWhileTargetIsNull() {
		// create command to test
		Verifytextbyarea cmd = new Verifytextbyarea("verifyTextByArea", "", "This is a text.");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.FAILED);
		resultCheck.setMessage("Command needs target and value data to work!");

		// check
		assertThat(resultCheck, samePropertyValuesAs(result));
	}

}