package com.zurg.imagetotext.model;

import java.awt.image.BufferedImage;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class ModificationViewData {
	private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();

	public ObjectProperty<Image> getImageProperty() {
		return imageProperty;
	}
	public Image getFXImage() {
		return imageProperty.get();
	}
	public BufferedImage getBufferedImage() {
		return SwingFXUtils.fromFXImage(imageProperty.get(), null);
	}
	
	
	public void setImageProperty(BufferedImage image) {
		Image fxImage = SwingFXUtils.toFXImage(image, null);
		setImageProperty(fxImage);
	}
	public void setImageProperty(Image image) {
		imageProperty.set(image);
	}
	public void setImageProperty(ObjectProperty<Image> imageProperty) {
		this.imageProperty = imageProperty;
	}
}
