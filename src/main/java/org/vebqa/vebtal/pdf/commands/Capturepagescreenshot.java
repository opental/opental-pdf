package org.vebqa.vebtal.pdf.commands;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PdfDriver;
import org.vebqa.vebtal.pdfrestserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, command = "capturePageScreenshot", hintTarget = "page=", hintValue = "path/to/screenshot.png")
public class Capturepagescreenshot extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(Capturepagescreenshot.class);

	public Capturepagescreenshot(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACCESSOR;
	}

	@Override
	public Response executeImpl(Object aDocument) {

		PdfDriver driver = (PdfDriver)aDocument;

		String[] token = target.split("=");
		int page = Integer.parseInt(token[1]);
		// page tree starts at 0!
		page--;

		Response tResp = new Response();

		try (InputStream inputStream = new ByteArrayInputStream(driver.getContentStream());) {
			PDDocument pdf = Loader.loadPDF(new RandomAccessReadBuffer(inputStream));
			PDFRenderer pdfRenderer = new PDFRenderer(pdf);
			BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
			ImageIOUtil.writeImage(bim, value, 300);
			pdf.close();
		} catch (InvalidPasswordException e) {
			logger.error("Password needed to load file!", e);
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Password needed to load file: " + value);
			return tResp;
		} catch (IOException e) {
			logger.error("Error while saving pdf page as image!", e);
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Could not write Image to file: " + value);
			return tResp;
		}

		tResp.setCode(Response.PASSED);
		tResp.setMessage("Successfully written data to file: " + value);

		return tResp;
	}
}