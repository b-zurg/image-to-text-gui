package com.zurg.imagetotext.model;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FontViewData {
	private static FontViewData instance = null;
	
	static private ObservableList<String> topFonts = FXCollections.observableArrayList();
	static private StringProperty enteredText = new SimpleStringProperty();
	static private StringProperty selectedFont = new SimpleStringProperty();
	
	
	public static FontViewData getInstance() {
		if(instance == null) {
			instance = new FontViewData(); 
		}
		return instance;
	}
	
	public static ObservableList<String> getToFontsList() { return topFonts; }
	public static StringProperty getEnteredText() { return enteredText; }
	public static StringProperty getSelectedFont() { return selectedFont; }
	
}
