package com.zurg.imagetotext.gui.state;

import java.awt.image.BufferedImage;

import com.zurg.imagetotext.model.FinishSceneData;
import com.zurg.imagetotext.model.FontViewData;
import com.zurg.imagetotext.model.LineViewData;
import com.zurg.imagetotext.model.ModificationViewData;

public class StateContainer {
	private LineViewData lineData = new LineViewData();
	private FontViewData fontData = new FontViewData();
	private FinishSceneData finishData = new FinishSceneData();
	

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

	public FinishSceneData getFinishData() {
		return finishData;
	}
	public void setFinishData(FinishSceneData finishData) {
		this.finishData = finishData;
	}
}
