package com.zurg.imagetotext.gui;

import com.zurg.imagetotext.model.FontViewData;
import com.zurg.imagetotext.model.LineViewData;
import com.zurg.imagetotext.model.WordViewData;

public class StateContainer {
	private LineViewData lineData;
	private WordViewData wordData;
	private FontViewData fontData;
	public LineViewData getLineData() {
		return lineData;
	}
	public void setLineData(LineViewData lineData) {
		this.lineData = lineData;
	}
	public WordViewData getWordData() {
		return wordData;
	}
	public void setWordData(WordViewData wordData) {
		this.wordData = wordData;
	}
	public FontViewData getFontData() {
		return fontData;
	}
	public void setFontData(FontViewData fontData) {
		this.fontData = fontData;
	}
	
	
}
