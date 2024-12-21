package org.vebqa.vebtal.pdf.asserts;

import org.assertj.core.api.AbstractAssert;
import org.vebqa.vebtal.pdf.PDFDriver;

/**
 * Special assertion class - inherits from AbstractAssert!
 * @author doerges
 *
 */
public class VerifyMetaDataAssert extends AbstractAssert<VerifyMetaDataAssert, PDFDriver> {
	
	/**
	 * Constructor assertion class, PDF filename ist the object we want to make assertions on.
	 * @param	aPdfToTest a documkent to test
	 */
	public VerifyMetaDataAssert(PDFDriver aPdfToTest) {
		super(aPdfToTest, VerifyMetaDataAssert.class);
	}	
	
    /**
     * A fluent entry point to our specific assertion class, use it with static import. 
     * @param	aPdfToTest	Our document to test
     * @return	self
     */
    public static VerifyMetaDataAssert assertThat(PDFDriver aPdfToTest) {
        return new VerifyMetaDataAssert(aPdfToTest);
    }
    
    /**
     * A specific assertion
     * @param	pages	pages to expect
     * @return	self
     */
    public VerifyMetaDataAssert hasNumberOfPages(int pages) {
    	// check that we really have a pdf filename defined.
    	isNotNull();

		if (this.actual.getDocument().getNumberOfPages() != pages) {
			failWithMessage("Expected no. of pages <%s> but was <%s>.", pages, this.actual.getDocument().getNumberOfPages());
		}
		
		return this;
    }
    
    /**
     * 
     * @param	anAuthor	the author we are expecting
     * @return	self
     */
    public VerifyMetaDataAssert hasAuthor(String anAuthor) {
    	// check that we really have a pdf filename defined.
    	isNotNull();

		if (!this.actual.getDocument().getDocumentInformation().getAuthor().contentEquals(anAuthor)) {
			failWithMessage("Expected author is <%s> but was <%s>", anAuthor, this.actual.getDocument().getDocumentInformation().getAuthor());
		}
		
		return this;
    }
    
    /**
     * 
     * @param 	aCreator	the creator we are expecting
     * @return	self
     */
    public VerifyMetaDataAssert hasCreator(String aCreator) {
    	// check that we really have a pdf filename defined.
    	isNotNull();

		if (!this.actual.getDocument().getDocumentInformation().getCreator().contentEquals(aCreator)) {
			failWithMessage("Expected creator is <%s> but was <%s>", aCreator, this.actual.getDocument().getDocumentInformation().getCreator());
		}
		
		return this;
    }   
   
    /**
     * 
     * @param	aTitle	the title we are expecting
     * @return	self
     */
    public VerifyMetaDataAssert hasTitle(String aTitle) {
    	// check that we really have a pdf filename defined.
    	isNotNull();

		if (this.actual.getDocument().getDocumentInformation().getTitle() == null) {
			failWithMessage("Expected title is <%s> but there is no title object.", aTitle);
		}
		if (!this.actual.getDocument().getDocumentInformation().getTitle().contentEquals(aTitle)) {
			failWithMessage("Expected title is <%s> but was <%s>", aTitle, this.actual.getDocument().getDocumentInformation().getTitle());
		}
		
		return this;
    }   
}