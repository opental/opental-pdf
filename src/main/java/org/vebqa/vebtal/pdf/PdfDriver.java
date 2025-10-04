package org.vebqa.vebtal.pdf;

import static java.nio.file.Files.readAllBytes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionJavaScript;
import org.apache.pdfbox.pdmodel.interactive.action.PDFormFieldAdditionalActions;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDNonTerminalField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PdfDriver {

	public static final String ACTION_VALIDATE = "validate";
	public static final String ACTION_CALCULATE = "calculate";
	public static final String ACTION_FORMAT = "format";
	public static final String ACTION_KEYSTROKE = "keystroke";
	
	private static final Logger logger = LoggerFactory.getLogger(PdfDriver.class);

	private boolean isSuccessfullyLoaded;

	private String pathToResource;

	private PDDocument document;

	private byte[] content;

	// Document Information
	private int numberOfPages;
	private String author;
	private Calendar creationDate;
	private String creator;
	public String keywords;
	public String producer;
	private String subject;
	private String title;
	private String isTrapped;
	public boolean encrypted;
	public boolean signed;
	public String signerName;
	public Calendar signatureTime;

	public PdfDriver() {
		this.isSuccessfullyLoaded = false;
		this.content = null;
	}

	public String getFilePath() {
		return this.pathToResource;
	}

	public PdfDriver setFilePath(String aPathToDoc) {
		this.pathToResource = aPathToDoc;
		return this;
	}

	public String getAuthor() {
		return this.author;
	}
	
	public Calendar getCreationDate() {
		return this.creationDate;
	}
	
	public int getNumberOfPages() {
		return this.numberOfPages;
	}
	
	public String getSubject() {
		return this.subject;
	}
	
	public String getCreator() {
		return this.creator;
	}
	
	public Boolean isTrapped() {
		if (this.isTrapped == null) {
			return false;
		}
		return true;
	}
	
	public PdfDriver load() throws IOException {
		load(this.pathToResource, readAllBytes(Paths.get(this.pathToResource)));
		return this;
	}

	public String getTitle() {
		return this.title;
	}
	
	public void load(File pdfFile) throws IOException {
		load(pdfFile.getAbsolutePath(), readAllBytes(Paths.get(pdfFile.getAbsolutePath())));
	}

	public void load(URL url) throws IOException {
		load(url.toString(), readBytes(url));
	}

	private void load(String name, byte[] content) throws IOException {
		this.content = content;

		// separate metadata if available in
		// - Dublin Core
		// - Adobe PDF Schema
		// - XMP Basic Schema
		// - Document Information

		try (InputStream inputStream = new ByteArrayInputStream(content)) {
			this.document = Loader.loadPDF(new RandomAccessReadBuffer(inputStream));
			this.pathToResource = name;
			this.numberOfPages = this.document.getNumberOfPages();
			this.author = this.document.getDocumentInformation().getAuthor();
			this.creationDate = this.document.getDocumentInformation().getCreationDate();
			this.creator = this.document.getDocumentInformation().getCreator();
			this.keywords = this.document.getDocumentInformation().getKeywords();
			this.producer = this.document.getDocumentInformation().getProducer();
			this.subject = this.document.getDocumentInformation().getSubject();
			this.title = this.document.getDocumentInformation().getTitle();
			this.isTrapped = this.document.getDocumentInformation().getTrapped();
			this.encrypted = this.document.isEncrypted();
			PDSignature signature = this.document.getLastSignatureDictionary();
			this.signed = signature != null;
			this.signerName = signature == null ? null : signature.getName();
			this.signatureTime = signature == null ? null : signature.getSignDate();
			this.isSuccessfullyLoaded = true;
		} catch (IllegalArgumentException e) {
			logger.error("Invalid PDF file: {}", name);
			throw new IllegalArgumentException("Invalid PDF file: " + name, e);
		} catch (IOException e) {
			logger.error("There was an error while processing the document!", e);
			throw new IOException("Cannot load PDF!");
		}
	}

	public PDDocument getDocument() {
		return this.document;
	}

	public boolean isLoaded() {
		return this.isSuccessfullyLoaded;
	}

	public void close() throws IOException {
		if (this.document != null) {
			this.document.close();
			this.isSuccessfullyLoaded = false;
		} else {
			logger.warn("Cannot close document because it is not existing.");
		}
	}

	private static byte[] readBytes(URL url) throws IOException {
		try (InputStream inputStream = url.openStream()) {
			return readBytes(inputStream);
		}
	}

	private static byte[] readBytes(InputStream inputStream) throws IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream(2048);
		byte[] buffer = new byte[2048];

		int nRead;
		while ((nRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
			result.write(buffer, 0, nRead);
		}

		return result.toByteArray();
	}

	public byte[] getContentStream() {
		return this.content;
	}

	/**
	 * Return some informations about existing fields or null if no field is available
	 * @param pdfDocument
	 * @return
	 * @throws IOException
	 */
	public String getFieldsInformation(PDDocument pdfDocument) throws IOException {

		PDDocumentCatalog docCatalog = pdfDocument.getDocumentCatalog();
		PDAcroForm acroForm = docCatalog.getAcroForm();
		if (acroForm == null) {
			return null;
		}
		List<PDField> fields = acroForm.getFields();

		StringBuilder output = new StringBuilder();
		for (PDField field : fields) {
			output.append(processField(field, "|--", field.getPartialName()));
			output.append("\n");
		}
		return output.toString();
	}

	private String processField(PDField field, String sLevel, String sParent) throws IOException {

		String output = "";

		String partialName = field.getPartialName();

		if (field instanceof PDNonTerminalField) {
			if (!sParent.equals(field.getPartialName())) {
				if (partialName != null) {
					sParent = sParent + "." + partialName;
				}
			}
			output = sLevel + sParent;

			for (PDField child : ((PDNonTerminalField) field).getChildren()) {
				// there are more subs, so go deeper
				processField(child, "|  " + sLevel, sParent);
			}
		} else {
			String fieldValue = field.getValueAsString();
			StringBuilder outputString = new StringBuilder(sLevel);
			output = sParent;
			if (partialName != null) {
				outputString.append(".").append(partialName);
			}
			outputString.append("|").append(fieldValue);
			outputString.append(",  type=").append(field.getClass().getSimpleName());

			// get available script information
			if (field.getActions() == null) {
				outputString.append(", No actions");
			} else {
				outputString.append(", C:");
				if (field.getActions().getC() != null) {
					PDActionJavaScript js = (PDActionJavaScript) field.getActions().getC();
					if (js != null) {
						outputString.append("+");
					} else {
						outputString.append("-");
					}
				} else {
					outputString.append("-");
				}
				outputString.append("|F:");
				if (field.getActions().getF() != null) {
					PDActionJavaScript js = (PDActionJavaScript) field.getActions().getF();
					if (js != null) {
						outputString.append("+");
					} else {
						outputString.append("-");
					}
				} else {
					outputString.append("-");
				}
				outputString.append("|K:");
				if (field.getActions().getK() != null) {
					PDActionJavaScript js = (PDActionJavaScript) field.getActions().getK();
					if (js != null) {
						outputString.append("+");
					} else {
						outputString.append("-");
					}
				} else {
					outputString.append("-");
				}
				outputString.append("|V:");
				if (field.getActions().getV() != null) {
					PDActionJavaScript js = (PDActionJavaScript) field.getActions().getV();
					if (js != null) {
						outputString.append("+");
					} else {
						outputString.append("-");
					}
				} else {
					outputString.append("-");
				}
			}
			output = output + outputString;
		}

		return output;
	}
	
	/**
	 * Return value or null if not available
	 * @param aName
	 * @return
	 */
	public String getValueByFieldName(String aName) {
		PDDocumentCatalog docCatalog = this.document.getDocumentCatalog();
		PDAcroForm acroForm = docCatalog.getAcroForm();
		if (acroForm == null) {
			return null;
		}
		List<PDField> fields = acroForm.getFields();
		for (PDField field : fields) {
			if (field.getPartialName().contentEquals(aName)) {
				return field.getValueAsString();
			}
		}
		return null;
	}

	public String getActionByFieldName(String aName) {
		PDDocumentCatalog docCatalog = this.document.getDocumentCatalog();
		PDAcroForm acroForm = docCatalog.getAcroForm();
		List<PDField> fields = acroForm.getFields();
		for (PDField field : fields) {
			if (field.getPartialName().contentEquals(aName)) {
				PDFormFieldAdditionalActions actions = field.getActions();
				if ((actions != null) && (actions.getV() != null)) {
					PDActionJavaScript js = (PDActionJavaScript) actions.getV();
					return js.getAction();
				}
			}
		}
		return null;
	}

	public String getActionByFieldName(String aName, String anAction) {
		PDDocumentCatalog docCatalog = this.document.getDocumentCatalog();
		PDAcroForm acroForm = docCatalog.getAcroForm();
		List<PDField> fields = acroForm.getFields();
		for (PDField field : fields) {
			if (field.getPartialName().contentEquals(aName)) {
				PDFormFieldAdditionalActions actions = field.getActions();
				// there is no action attached
				if (actions == null) {
					return null;
				}
				
				if ((actions.getV() != null) && anAction.contentEquals(PdfDriver.ACTION_VALIDATE)) {
					return ((PDActionJavaScript)actions.getV()).getAction();
				}
				if ((actions.getC() != null) && anAction.contentEquals(PdfDriver.ACTION_CALCULATE)) {
					return ((PDActionJavaScript)actions.getC()).getAction();
				}
				if ((actions.getF() != null) && anAction.contentEquals(PdfDriver.ACTION_FORMAT)) {
					return ((PDActionJavaScript)actions.getF()).getAction();
				}
				if ((actions.getK() != null) && anAction.contentEquals(PdfDriver.ACTION_KEYSTROKE)) {
					return ((PDActionJavaScript)actions.getK()).getAction();
				}
			}
		}
		return null;
	}
	
	
	/**
	 * Returns true if a field has the expected additional action.
	 * 
	 * @param aName 	field name
	 * @param anAction	specified additional action, e.g. validate, calculate, keystroke, format
	 * @return			true, if expected additional action was found
	 */
	public boolean hasAdditionalActionByFieldName(String aName, String anAction) {
		PDDocumentCatalog docCatalog = this.document.getDocumentCatalog();
		PDAcroForm acroForm = docCatalog.getAcroForm();
		List<PDField> fields = acroForm.getFields();
		for (PDField field : fields) {
			if (field.getPartialName().contentEquals(aName)) {
				PDFormFieldAdditionalActions actions = field.getActions();
				// there is no action attached
				if (actions == null) {
					return false;
				}
				
				if ((actions.getV() != null) && anAction.contentEquals(PdfDriver.ACTION_VALIDATE)) {
					return true;
				}
				if ((actions.getC() != null) && anAction.contentEquals(PdfDriver.ACTION_CALCULATE)) {
					return true;
				}
				if ((actions.getF() != null) && anAction.contentEquals(PdfDriver.ACTION_FORMAT)) {
					return true;
				}
				if ((actions.getK() != null) && anAction.contentEquals(PdfDriver.ACTION_KEYSTROKE)) {
					return true;
				}
			}
		}
		return false;
	}	
	
	/**
	 * 
	 * @param aName		field name
	 * @param aValue	expected value
	 * @return			true, if field contains the expected value 
	 */
	public boolean setValueByFieldName(String aName, String aValue) {
		PDDocumentCatalog docCatalog = this.document.getDocumentCatalog();
		PDAcroForm acroForm = docCatalog.getAcroForm();
		List<PDField> fields = acroForm.getFields();
		for (PDField field : fields) {
			if (field.getPartialName().contentEquals(aName)) {
				logger.info("Field found: {}", aName);
				try {
					field.setValue(aValue);
				} catch (IOException e) {
					logger.error("cannot write to field: {}", aName, e);
					return false;
				}
				return true;
			} else {
				logger.info("Field not matching: {}", field.getPartialName());
			}
		}
		return false;
	}
}