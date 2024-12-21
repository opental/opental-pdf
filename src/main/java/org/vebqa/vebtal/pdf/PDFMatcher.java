package org.vebqa.vebtal.pdf;

import org.hamcrest.SelfDescribing;
import org.hamcrest.TypeSafeMatcher;

abstract class PDFMatcher extends TypeSafeMatcher<PDFDriver> implements SelfDescribing {
  protected String reduceSpaces(String text) {
    return text.replaceAll("[\\s\\n\\r\u00a0]+", " ").trim();
  }
}
