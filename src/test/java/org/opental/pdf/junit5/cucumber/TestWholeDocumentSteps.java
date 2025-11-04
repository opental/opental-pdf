package org.opental.pdf.junit5.cucumber;

import java.io.IOException;

import org.opental.pdf.PdfDriver;
import org.opental.pdf.asserts.VerifyMetaDataAssert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class TestWholeDocumentSteps {
	private String document;
	private PdfDriver dut;

	@Given("the document {string}")
	public void the_document(String aDoc) {
		document = aDoc;
	}

	@When("i open the document")
	public void i_open_the_document() {
		try {
			this.dut =  new PdfDriver().setFilePath(document).load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Then("i expect {string} as title")
	public void i_expect_as_title(String expectedTitle) {
		VerifyMetaDataAssert
		    .assertThat(this.dut)
		    .hasTitle(expectedTitle);
	}
}
