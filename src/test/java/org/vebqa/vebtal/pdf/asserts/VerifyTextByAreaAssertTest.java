package org.vebqa.vebtal.pdf.asserts;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.vebqa.vebtal.pdf.PDFDriver;

@RunWith(BlockJUnit4ClassRunner.class)
public class VerifyTextByAreaAssertTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Rule
	public PDFDriver dut = new PDFDriver().setFilePath("./src/test/java/resource/Testtext_Area.pdf");

	@Test
	public void checkIfEntireTextIsAvailabeInSpecificArea() {
		VerifyTextByAreaAssert.assertThat(dut).hasText("This is a text.").atPage(1).inArea(350, 220, 65, 20).check();
	}

	@Test
	public void checkThatTextFragmentIsAvailabeInSpecificArea() {
		VerifyTextByAreaAssert.assertThat(dut).hasText("text.").atPage(1).inArea(390, 220, 25, 20).check();
	}

	@Test
	public void failWhileTextIsNotAvailabeInSpecifiedArea() {
		thrown.expect(AssertionError.class);
//		thrown.expectMessage(
//				startsWith("Expected text <This is a text.> is not availabe in the located area. Instead found: <"));

		VerifyTextByAreaAssert.assertThat(dut).hasText("This is a text.").atPage(1).inArea(1, 1, 65, 20).check();
	}

	@Test
	public void failWhileOnlyTextFragmentIsAvailabeInSpecificArea() {
		thrown.expect(AssertionError.class);
		thrown.expectMessage(
				"Expected text <This is a text.> is not availabe in the located area. Instead found: <text. \r\n>");

		VerifyTextByAreaAssert.assertThat(dut).hasText("This is a text.").atPage(1).inArea(390, 220, 25, 20).check();
	}

}
