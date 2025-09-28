package org.opental.pdf.junit5;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

import org.junit.jupiter.api.Test;
import org.vebqa.vebtal.pdf.PDFDriver;

public class PDFDriverTest {

	@Test
	public void loadNonExistingFile() {
		try {
			new PDFDriver().setFilePath("./src/test/java/resource/FileNotExisting.pdf").load();
		} catch (NoSuchFileException nsfe) {
			assertTrue(true);
		} catch (Exception e) {
			assertFalse(true, "Expected NoSuchFileException!");
		}
	}

	@Test
	public void loadInvalidFile() {
		try {
			new PDFDriver().setFilePath("./src/test/resources/InvalidFile.pdf").load();
		} catch (IOException ioe) {
			assertTrue(ioe.getMessage().equals("Cannot load PDF!"));
		} catch (Exception e) {
			assertFalse(true, "Expected IOException!");
		}
	}

	@Test
	public void loadFileWithPassword() {
		try {
			new PDFDriver().setFilePath("./src/test/resources/ProtectedWithPassword.pdf").load();
		} catch (IOException ioe) {
			assertTrue(ioe.getMessage().equals("Cannot load PDF!"));
		} catch (Exception e) {
			assertFalse(true, "Expected IOException!");
		}
	}
}