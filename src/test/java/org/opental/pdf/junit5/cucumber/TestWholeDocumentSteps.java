package org.opental.pdf.junit5.cucumber;

import java.io.IOException;

import org.opental.pdf.PdfDriver;
import org.opental.pdf.asserts.VerifyMetaDataAssert;
import org.opental.pdf.asserts.VerifyTextAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class TestWholeDocumentSteps {
	
	private static final Logger logger = LoggerFactory.getLogger(TestWholeDocumentSteps.class);
	
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
			logger.error("Could not load document!", e);
		}
	}

	@Then("i expect {int} pages")
	public void i_expect_pages(int countPages) {
		VerifyMetaDataAssert
		    .assertThat(dut)
		    .hasNumberOfPages(countPages);
	}
	
	@Then("i expect {string} as title")
	public void i_expect_as_title(String expectedTitle) {
		VerifyMetaDataAssert
		    .assertThat(this.dut)
		    .hasTitle(expectedTitle);
	}
	
	@Then("i expect the author to be {string}")
	public void i_expect_the_author_to_be(String expectedAuthor) {
		VerifyMetaDataAssert
		    .assertThat(this.dut)
		    .hasAuthor(expectedAuthor);
	}
	
	@Then("i expect text {string} at page {int}")
	public void i_expect_text_at_page(String expectedText, int expectedPage) {
		VerifyTextAssert
		    .assertThat(this.dut)
		    .hasText(expectedText)
		    .atPage(expectedPage)
		    .check();
	}
	
}
