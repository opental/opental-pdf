package org.vebqa.vebtal.pdf.commands;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionJavaScript;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDNonTerminalField;
import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PDFDriver;
import org.vebqa.vebtal.pdfrestserver.PdfTestAdaptionPlugin;

@Keyword(module = PdfTestAdaptionPlugin.ID, command = "showFormFields")
public class Showformfields extends AbstractCommand {

	public Showformfields(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACTION;
	}

	@Override
	public Response executeImpl(Object driver) {
		PDFDriver pdfDriver = (PDFDriver) driver;

		Response tResp = new Response();

		String output = "";
		try {
			output = getFieldsInformation(pdfDriver.getDocument());
		} catch (IOException e) {
			tResp.setCode(Response.FAILED);
			tResp.setMessage(e.getMessage());
		}

		if (output == null) {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Document contains no form field(s)");
		} else {
			tResp.setCode(Response.PASSED);
			tResp.setMessage(output);
		}
		
		return tResp;
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
}
