package org.vebqa.vebtal.pdf.asserts;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.vebqa.vebtal.pdf.PDFDriver;

public class VerifyTextAssertTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Rule
	public final PDFDriver dut = new PDFDriver().setFilePath("./src/test/java/resource/LoremIpsum_3Pages.pdf");

	@Test
	public void findTextInPdf() {
		VerifyTextAssert.assertThat(dut).hasText("Duis autem").check();
	}

	@Test
	public void failFindingTextInPdf() {
		thrown.expect(AssertionError.class);
		// thrown.expectMessage(startsWith("Expected text <Duis autem Entenhausen> not found in the content <"));

		VerifyTextAssert.assertThat(dut).hasText("Duis autem Entenhausen").check();
	}
	
	@Test
	public void findTextAtPage_1() {
		VerifyTextAssert.assertThat(dut).hasText("Marker Page 1").atPage(1).check();
	}

	@Test
	public void findTextAtPage_2() {
		VerifyTextAssert.assertThat(dut).hasText("Marker Page 2").atPage(2).check();
	}

	@Test
	public void findTextAtPage_3() {
		VerifyTextAssert.assertThat(dut).hasText("Marker Page 3").atPage(3).check();
	}

	@Test
	public void failFindingTextInPage() {
		thrown.expect(AssertionError.class);
		// thrown.expectMessage(startsWith("Expected text <Marker Page 2> not found in the content <"));

		VerifyTextAssert.assertThat(dut).hasText("Marker Page 2").atPage(1).check();
	}

}