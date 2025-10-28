package org.vebqa.vebtal.pdf.commands;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.FailedResponse;
import org.vebqa.vebtal.model.PassedResponse;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PdfDriver;
import org.vebqa.vebtal.pdfrestserver.PdfTestAdaptionPlugin;

import dev.brachtendorf.jimagehash.hash.Hash;
import dev.brachtendorf.jimagehash.hashAlgorithms.HashingAlgorithm;
import dev.brachtendorf.jimagehash.hashAlgorithms.PerceptiveHash;

@Keyword(module = PdfTestAdaptionPlugin.ID, 
         command = "verifyHashOfImage",
         hintTarget = "page=<number>;name=<text>",
         hintValue = "<number>")
public class Verifyhashofimage extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(Verifyhashofimage.class);

	public Verifyhashofimage(String aCommand, String aTarget, String aValue) {
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

		// resolve target and split equations
		int pageToExtract = 0;
		String nameOfImage = "";
		String[] equations = target.split(";");
		for (String equation : equations) {
			String[] token = equation.split("=");
		    // token[0] ist "page"
		    // token[1] ist int>page
		    switch(token[0]) {
		        case "page": 
		        	pageToExtract = Integer.parseInt(token[1]);
		        	break;
		        case "name":
		        	nameOfImage = token[1];
		        	break;
		        default:
		        	break;
		    }
		}

		BigInteger imageHash = null;
		try {
			InputStream inputStream = new ByteArrayInputStream(
					driver.getContentStream());
			PDDocument pdf = Loader.loadPDF(new RandomAccessReadBuffer(inputStream));
			PDPageTree list = pdf.getPages();
			int pageCount = 0;
			for (PDPage page : list) {
				pageCount++;
				PDResources pdResources = page.getResources();
				if (pageCount == pageToExtract) {
					for (COSName name : pdResources.getXObjectNames()) {
						PDXObject o = pdResources.getXObject(name);
						if (o instanceof PDImageXObject && name.getName().contentEquals(nameOfImage)) {
							PDImageXObject image = (PDImageXObject) o;
							HashingAlgorithm hasher = new PerceptiveHash(32);
							Hash hashImg = hasher.hash(image.getImage());
							imageHash = hashImg.getHashValue();
							logger.info("hash for: {} -> {}", name.getName(), hashImg.getHashValue());		
						}
					}
				}
			}
		} catch (IOException e) {
			logger.error("Error while stripping text from pdf document!", e);
			return new FailedResponse("Error while reading document: " + e.getMessage());
		}
		if (imageHash.compareTo(BigInteger.valueOf(Long.parseLong(this.value))) != 0) {
			return new FailedResponse("Image hash should be: "+ this.value + " but was: " + imageHash);
		}

		return new PassedResponse("Images hash matches the given hash: " + this.value);
	}
}