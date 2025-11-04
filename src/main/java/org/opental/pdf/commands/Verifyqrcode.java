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

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

@Keyword(module = PdfTestAdaptionPlugin.ID, 
         command = "verifyQRCode", 
         hintTarget = "page=<number>",
         hintValue = "<text>")
public class Verifyqrcode extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(Verifyqrcode.class);

	public Verifyqrcode(String aCommand, String aTarget, String aValue) {
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
		PDDocument pdf = null;
		try {
			InputStream inputStream = new ByteArrayInputStream(
					driver.getContentStream());
			pdf = Loader.loadPDF(new RandomAccessReadBuffer(inputStream));
		} catch (IOException e) {
			logger.error("Error while stripping text from pdf document!", e);
			return new FailedResponse("Error while reading document: " + e.getMessage());
		}
		
		if (pageToExtact > pdf.getNumberOfPages()) {
			return new FailedResponse("Can not read from page " + pageToExtact + " because the document has only " + pdf.getNumberOfPages() + " pages");
		}
		
		PDPageTree list = pdf.getPages();
		int pageCount = 0;
		String text = "";
		Boolean codeFound = false;
		for (PDPage page : list) {
			pageCount++;
			PDResources pdResources = page.getResources();
			if (pageCount == pageToExtact) {
				for (COSName name : pdResources.getXObjectNames()) {
					PDXObject o;
					try {
					    o = pdResources.getXObject(name);
					} catch (IOException e) {
						continue;
					}
					if (o instanceof PDImageXObject) {
						i++;
						PDImageXObject image = (PDImageXObject) o;
						LuminanceSource luminanceSource;
						try {
						    luminanceSource = new BufferedImageLuminanceSource(image.getImage());
						} catch (IOException e) {
							logger.warn("Could not extract image; {}", name.getName());
							continue;
						}
						BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));
						Result result = null;
						try {
						    result = new QRCodeReader().decode(binaryBitmap);
						} catch (NotFoundException e) {
							logger.info("No code found in image: {}", name.getName());
							continue;
						} catch (ChecksumException e) {
                            logger.warn("Internal checksum error.");
                            continue;
						} catch (FormatException e) {
                            logger.warn("Internal format exception");
                            continue;
						}
						text = result.getText();
						codeFound = true;
					}
				}
			}
			if (i == 0) {
				return new FailedResponse("No images found in page: "+ pageToExtact);
			}

			if (!codeFound) {
				return new FailedResponse(i + " image(s) analyzed, but no code was found.");
			}
			
			if (!text.contentEquals(value) && codeFound) {
				return new FailedResponse ("Expected text is "+ value + " but found " + text);
			}
		}
		
		return new PassedResponse("Successfully found code: " + value);
	}
}