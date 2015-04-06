package com.zurg.imagetotext.gui;

import java.awt.image.BufferedImage;

import com.zurg.imagetotext.model.FontViewData;
import com.zurg.imagetotext.model.LineViewData;
import com.zurg.imagetotext.model.ModificationViewData;

public class StateContainer {
	private LineViewData lineData = new LineViewData();
	private FontViewData fontData = new FontViewData();
	private ModificationViewData modificationData = new ModificationViewData();
	
	public StateContainer(BufferedImage image) {
		this.lineData.setUntouchedImage(image);
	}
	public StateContainer() {};
	
	
	public LineViewData getLineData() {
		return lineData;
	}
	public void setLineData(LineViewData lineData) {
		this.lineData = lineData;
	}

	public FontViewData getFontData() {
		return fontData;
	}
	public void setFontData(FontViewData fontData) {
		this.fontData = fontData;
	}
	public ModificationViewData getModificationData() {
		return modificationData;
	}
	public void setModificationData(ModificationViewData modificationData) {
		this.modificationData = modificationData;
	}
}
