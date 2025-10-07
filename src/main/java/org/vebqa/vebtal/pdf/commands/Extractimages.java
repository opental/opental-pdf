package org.vebqa.vebtal.pdf.commands;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

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

@Keyword(module = PdfTestAdaptionPlugin.ID, 
         command = "extractImages", 
         hintTarget = "page=<number>",
         hintValue = "<path>")
public class Extractimages extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(Extractimages.class);

	public Extractimages(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACCESSOR;
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
							String filename = value + "\\extracted-image-" + i + ".png";
							ImageIO.write(image.getImage(), "png", new File(filename));
						}
					}
				}
				if (i == 0) {
					return new FailedResponse("No images found in page: "+ pageToExtact);
				}
			}
		} catch (IOException e) {
			logger.error("Error while stripping text from pdf document!", e);
			return new FailedResponse("Error while reading document: " + e.getMessage());
		}
		return new PassedResponse("Successfully extracted " + i + " image(s) from page: " + pageToExtact);
	}
}