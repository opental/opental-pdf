package org.opental.pdf;

import java.awt.geom.Rectangle2D;

import org.opental.core.exception.InvalidSpecificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Area {

	private static final Logger logger = LoggerFactory.getLogger(Area.class);

	private int page = 1;
	private int x = 0;
	private int y = 0;
	private int height = 0;
	private int width = 0;

	public Area(String aSpecLine) throws InvalidSpecificationException {
		parseSpecLine(aSpecLine);
	}

	public Area(int apage, int ax, int ay, int aheight, int awidth) {
		page = apage - 1;
		x = ax;
		y = ay;
		height = aheight;
		width = awidth;
	}

	private void parseSpecLine(String aSpecLine) throws InvalidSpecificationException {
		boolean invalidArea = false;
		String[] someFragments = aSpecLine.split(";");
		for (String aFragment : someFragments) {
			aFragment = aFragment.trim().toLowerCase();
			String[] someToken = aFragment.split("=");
			if (someToken[0] == null || someToken[1] == null) {
				throw new InvalidSpecificationException("Invalid Arguments!");
			}
			switch (someToken[0]) {
			case "x":
				x = Integer.parseInt(someToken[1]);
				break;
			case "y":
				y = Integer.parseInt(someToken[1]);
				break;
			case "height":
				height = Integer.parseInt(someToken[1]);
				break;
			case "width":
				width = Integer.parseInt(someToken[1]);
				break;
			case "page":
				page = Integer.parseInt(someToken[1]) - 1;
				break;
			default:
				invalidArea = true;
				break;
			}
		}
		if (invalidArea) {
			throw new InvalidSpecificationException("Invalid area data!");
		}
		logger.info("Area created: {},{} - {},{} on page {}", x, y, height, width, page);
	}

	public Rectangle2D getRectangle() {
		return new Rectangle2D.Float(x, y, width, height);
	}

	public int getPage() {
		return page;
	}
	
}