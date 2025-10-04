package org.vebqa.vebtal.pdf.asserts;

import org.assertj.core.api.AbstractAssert;
import org.vebqa.vebtal.pdf.PdfDriver;

/**
 * Special assertion class - inherits from AbstractAssert!
 * @author doerges
 *
 */
public class VerifyMetaDataAssert extends AbstractAssert<VerifyMetaDataAssert, PdfDriver> {
	
	/**
	 * Constructor assertion class, PDF filename ist the object we want to make assertions on.
	 * @param	aPdfToTest a documkent to test
	 */
	public VerifyMetaDataAssert(PdfDriver aPdfToTest) {
		super(aPdfToTest, VerifyMetaDataAssert.class);
	}	
	
    /**
     * A fluent entry point to our specific assertion class, use it with static import. 
     * @param	aPdfToTest	Our document to test
     * @return	self
     */
    public static VerifyMetaDataAssert assertThat(PdfDriver aPdfToTest) {
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

		if (this.actual.getNumberOfPages() != pages) {
			failWithMessage("Expected no. of pages <%s> but was <%s>.", pages, this.actual.getNumberOfPages());
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

		if (!this.actual.getAuthor().contentEquals(anAuthor)) {
			failWithMessage("Expected author is <%s> but was <%s>", anAuthor, this.actual.getAuthor());
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

		if (!this.actual.getCreator().contentEquals(aCreator)) {
			failWithMessage("Expected creator is <%s> but was <%s>", aCreator, this.actual.getCreator());
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

		if (this.actual.getTitle() == null) {
			failWithMessage("Expected title is <%s> but there is no title object.", aTitle);
		}
		if (!this.actual.getTitle().contentEquals(aTitle)) {
			failWithMessage("Expected title is <%s> but was <%s>", aTitle, this.actual.getTitle());
		}
		
		return this;
    }
    
    /**
     * 
     * @param	aTitle	the title we are expecting
     * @return	self
     */
    public VerifyMetaDataAssert isTrapped(String isTrapped) {
    	// check that we really have a pdf filename defined.
    	isNotNull();

		if (this.actual.isTrapped() == null) {
			failWithMessage("Expected trapped is <%s> but there is no trapped object.", isTrapped);
		}
		if (!this.actual.isTrapped()) {
			failWithMessage("Expected trapped is <%s> but was <%s>", isTrapped, this.actual.isTrapped());
		}
		
		return this;
    }   
}