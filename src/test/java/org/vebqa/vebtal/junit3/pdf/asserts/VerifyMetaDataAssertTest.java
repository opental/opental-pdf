package org.vebqa.vebtal.junit3.pdf.asserts;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.vebqa.vebtal.pdf.PDFDriver;
import org.vebqa.vebtal.pdf.asserts.VerifyMetaDataAssert;

public class VerifyMetaDataAssertTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Rule
	public final PDFDriver dut = new PDFDriver().setFilePath("./src/test/java/resource/LoremIpsum_3Pages.pdf");
	
	@Rule
	public final PDFDriver dut_nt = new PDFDriver().setFilePath("./src/test/java/resource/LoremIpsum500.pdf");

	@Test
	public void checkIfDocumentHasGivenNoOfPages() {
		VerifyMetaDataAssert.assertThat(dut).hasNumberOfPages(3);
	}
	
	@Test
	public void checkIfDocumentDoesNotHaveGivenNoOfPages() {
		thrown.expect(AssertionError.class);
		thrown.expectMessage("Expected no. of pages <5> but was <3>.");
		
		VerifyMetaDataAssert.assertThat(dut).hasNumberOfPages(5);
	}
	
	@Test
	public void checkIfDocumentHasGivenAuthor() {
		VerifyMetaDataAssert.assertThat(dut).hasAuthor("Dörges, Karsten");
	}
	
	@Test
	public void checkIfDocumentDoesNotHaveTheGivenAuthor() {
		thrown.expect(AssertionError.class);
		thrown.expectMessage("Expected author is <Radjindirin, Nithiyaa> but was <Dörges, Karsten>");
		
		VerifyMetaDataAssert.assertThat(dut).hasAuthor("Radjindirin, Nithiyaa");
	}
	
	@Test
	public void checkIfDocumentHasGivenCreator() {
		VerifyMetaDataAssert.assertThat(dut).hasCreator("Microsoft® Word 2010");
	}
	
	@Test
	public void checkIfDocumentDoesNotHaveGivenCreator() {
		thrown.expect(AssertionError.class);
		thrown.expectMessage("Expected creator is <WRONG_CREATOR> but was <Microsoft® Word 2010>");
		
		VerifyMetaDataAssert.assertThat(dut).hasCreator("WRONG_CREATOR");
	}
	
	@Test
	public void checkIfDocumentHasGivenTitle() {
		VerifyMetaDataAssert.assertThat(dut).hasTitle("Test Title");
	}
	
	@Test
	public void checkIfDocumentDoesNotHaveTheGivenTitle() {
		thrown.expect(AssertionError.class);
		thrown.expectMessage("Expected title is <WRONG TITLE> but was <Test Title>");

		VerifyMetaDataAssert.assertThat(dut).hasTitle("WRONG TITLE");
	}
	
	@Test
	public void checkIfDocumentHasNoTitle() {
		thrown.expect(AssertionError.class);
		thrown.expectMessage("Expected title is <WRONG TITLE> but there is no title object.");

		VerifyMetaDataAssert.assertThat(dut_nt).hasTitle("WRONG TITLE");
	}
	

}
