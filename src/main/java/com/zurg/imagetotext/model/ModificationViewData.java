package com.zurg.imagetotext.model;

import java.awt.image.BufferedImage;

import utils.Point;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class ModificationViewData {
	private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();

	private ObservableList<Point> topLeftPoints = FXCollections.observableArrayList();
	private ObservableList<Point> bottomRightPoints = FXCollections.observableArrayList();
	
	
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
	
	
	public ObservableList<Point> getTopLeftPoints() {
		return topLeftPoints;
	}
	public void setTopLeftPoints(ObservableList<Point> topLeftPoints) {
		this.topLeftPoints = topLeftPoints;
	}
	
	public ObservableList<Point> getBottomRightPoints() {
		return bottomRightPoints;
	}
	public void setBottomRightPoints(ObservableList<Point> widthsAndHeights) {
		this.bottomRightPoints = widthsAndHeights;
	}
}
