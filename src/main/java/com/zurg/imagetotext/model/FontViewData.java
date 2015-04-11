package com.zurg.imagetotext.model;


import java.awt.image.BufferedImage;
import java.util.List;

import recognition.FontInfo;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class FontViewData {
	
	private ObjectProperty<Image> lineImage = new SimpleObjectProperty<Image>();
	private ObjectProperty<ObservableList<FontInfo>> topFontsProperty = new SimpleObjectProperty<ObservableList<FontInfo>>();
	
	
	
	private StringProperty enteredText = new SimpleStringProperty();
	private StringProperty selectedFont = new SimpleStringProperty();
	
	
	public StringProperty getEnteredText() { return enteredText; }
	public StringProperty getSelectedFont() { return selectedFont; }
	
	public ObjectProperty<ObservableList<FontInfo>> getTopFontsProperty() {
		return topFontsProperty;
	}
	public void setTopFonts(ObservableList<FontInfo> topFonts) {
		this.topFontsProperty.set(topFonts);
	}
	public void setTopFonts(List<FontInfo> topFonts) {
		this.topFontsProperty.set(FXCollections.observableArrayList(topFonts));
	}
	public ObjectProperty<Image> getLineImageProperty() {
		return lineImage;
	}
	public void setLineImageProperty(ObjectProperty<Image> lineImage) {
		this.lineImage = lineImage;
	}
	public void setLineImageProperty(Image lineImage) {
		this.lineImage.set(lineImage);
	}
	public void setLineImageProperty(BufferedImage image) {
		Image fxImage = SwingFXUtils.toFXImage(image, null);
		setLineImageProperty(fxImage);
	}

}
