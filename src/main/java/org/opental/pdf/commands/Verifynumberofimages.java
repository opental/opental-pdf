package org.opental.pdf.commands;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.opental.core.annotations.Keyword;
import org.opental.core.command.AbstractCommand;
import org.opental.core.model.CommandType;
import org.opental.core.model.FailedResponse;
import org.opental.core.model.PassedResponse;
import org.opental.core.model.Response;
import org.opental.pdf.PdfDriver;
import org.opental.pdf.restserver.PdfTestAdaptionPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.brachtendorf.jimagehash.hash.Hash;
import dev.brachtendorf.jimagehash.hashAlgorithms.HashingAlgorithm;
import dev.brachtendorf.jimagehash.hashAlgorithms.PerceptiveHash;

@Keyword(module = PdfTestAdaptionPlugin.ID, 
         command = "verifyNumberOfImages",
         hintTarget = "page=<number>",
         hintValue = "<number>")
public class Verifynumberofimages extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(Verifynumberofimages.class);

	public Verifynumberofimages(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ASSERTION;
	}

	@Override
	public Response executeImpl(Object aDocument) {

		if (!this.validate()) {
			return new FailedResponse("Input doesnt match validation pattern.");
		}
		
		PdfDriver driver = (PdfDriver)aDocument;
		
		if (!driver.isLoaded()) {
			return new FailedResponse("No document loaded.");
		}

		// resolve target
		String[] token = target.split("=");
		// token[0] ist "page"
		// token[1] ist int>page
		int pageToExtact = Integer.parseInt(token[1]);

		// count found images
		int i = 0;
		try {
			InputStream inputStream = new ByteArrayInputStream(
					driver.getContentStream());
			PDDocument pdf = Loader.loadPDF(new RandomAccessReadBuffer(inputStream));
			PDPageTree list = pdf.getPages();
			int pageCount = 0;
			for (PDPage page : list) {
				pageCount++;
				PDResources pdResources = page.getResources();
				if (pageCount == pageToExtact) {
					for (COSName name : pdResources.getXObjectNames()) {
						PDXObject o = pdResources.getXObject(name);
						if (o instanceof PDImageXObject) {
							i++;
							PDImageXObject image = (PDImageXObject) o;
							HashingAlgorithm hasher = new PerceptiveHash(32);
							Hash hashImg = hasher.hash(image.getImage());
							logger.info("hash for: {} -> {}", name.getName(), hashImg.getHashValue());
						}
					}
				}
			}
		} catch (IOException e) {
			logger.error("Error while stripping text from pdf document!", e);
			return new FailedResponse("Error while reading document: " + e.getMessage());
		}
		if (i != Integer.valueOf(this.value)) {
			return new FailedResponse("Image count should be: "+ this.value + " but was: " + i);
		}

		return new PassedResponse("Number of images matches the expected number: " + this.value);
	}
}