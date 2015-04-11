package com.zurg.imagetotext.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FinishSceneData {
	private StringProperty text = new SimpleStringProperty();
	
	public StringProperty getText() {
		return text;
	}

	public void setText(StringProperty text) {
		this.text = text;
	}
}
