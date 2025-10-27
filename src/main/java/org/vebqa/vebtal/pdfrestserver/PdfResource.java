package org.vebqa.vebtal.pdfrestserver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.AbstractTestAdaptionResource;
import org.vebqa.vebtal.GuiManager;
import org.vebqa.vebtal.TestAdaptionResource;
import org.vebqa.vebtal.model.Command;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.pdf.PDFStore;
import org.vebqa.vebtal.sut.SutStatus;

public class PdfResource extends AbstractTestAdaptionResource implements TestAdaptionResource {

	private static final Logger logger = LoggerFactory.getLogger(PdfResource.class);

	public PdfResource() {
	}

	@Override
	public Response execute(Command cmd) {

		// disable user actions
		PdfTestAdaptionPlugin.setDisableUserActions(true);

		Response tResponse;

		Response result = null;
		try {
			Class<?> cmdClass = Class.forName("org.vebqa.vebtal.pdf.commands." + getCommandClassName(cmd));
			Constructor<?> cons = cmdClass.getConstructor(String.class, String.class, String.class);
			Object cmdObj = cons.newInstance(cmd.getCommand(), cmd.getTarget(), cmd.getValue());

			// get type
			Method mType = cmdClass.getMethod("getType");
			CommandType cmdType = (CommandType) mType.invoke(cmdObj);
			PdfTestAdaptionPlugin.addCommandToList(cmd, cmdType);

			// execute
			Method m = cmdClass.getDeclaredMethod("executeImpl", Object.class);

			setStart();
			result = (Response) m.invoke(cmdObj, PDFStore.getStore().getDriver());
			setFinished();

		} catch (ClassNotFoundException e) {
			logger.error("Keyword class {} not found.", getCommandClassName(cmd), e);
		} catch (NoSuchMethodException e) {
			logger.error("Execute method in keyword class not found.", e);
		} catch (SecurityException e) {
			logger.error("Cannot invoke keyword class.", e);
		} catch (InstantiationException e) {
			logger.error("Cannot instantiate keyword class.", e);
		} catch (IllegalAccessException e) {
			logger.error("Illegal access for keyword class.", e);
		} catch (IllegalArgumentException e) {
			logger.error("Illegal argument while invoking keyword.", e);
		} catch (InvocationTargetException e) {
			logger.error("Cannot invoke keyword class.", e);
		}

		if (result == null) {
			tResponse = new Response.Builder()
			    .setCode(Response.FAILED)
			    .setMessage("Cannot resolve command.")
			    .build();
			return tResponse;
		}
		if (!result.getCode().equals(Response.PASSED)) {
			PdfTestAdaptionPlugin.setLatestResult(false, result.getMessage());
		} else {
			PdfTestAdaptionPlugin.setLatestResult(true, result.getMessage());
		}
		if (PDFStore.getStore().getDriver().isLoaded()) {
			GuiManager.getinstance().setTabStatus(PdfTestAdaptionPlugin.ID, SutStatus.CONNECTED);
		} else {
			GuiManager.getinstance().setTabStatus(PdfTestAdaptionPlugin.ID, SutStatus.DISCONNECTED);
		}

		// enable user actions
		PdfTestAdaptionPlugin.setDisableUserActions(false);

		return result;
	}
}
