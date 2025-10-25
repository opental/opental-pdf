package org.opental.pdf.junit5.commands;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PdfDriver;
import org.vebqa.vebtal.pdf.commands.Close;

class CloseTest {

	public final PdfDriver dut = new PdfDriver().setFilePath("./src/test/java/resource/LoremIpsum_3Pages.pdf");

	@Test
	void closePdfFile() {
		// create command to test
		Close cmd = new Close("close", "", "");
		Response result = cmd.executeImpl(dut);

		// create a green result object
		Response resultCheck = new Response();
		resultCheck.setCode(Response.PASSED);
		resultCheck.setMessage("Successfully removes SUT from memory.");

		// check
		assertThat(resultCheck).usingRecursiveComparison().isEqualTo(result);
	}
}