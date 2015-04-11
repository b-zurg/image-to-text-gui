package com.zurg.imagetotext.model;

import java.awt.image.BufferedImage;

import document.analysis.ParagraphComponentAnalyzer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class LineViewData {


	private  DoubleProperty standardBlurNeighborhood = new SimpleDoubleProperty(9.0);
	private  DoubleProperty standardBlurIterations = new SimpleDoubleProperty(2.0);

	private  DoubleProperty horizontalBlurNeighborhood = new SimpleDoubleProperty(9.0);
	private  DoubleProperty horizontalBlurIterations = new SimpleDoubleProperty(2.0);

	private  DoubleProperty thresholdLevel = new SimpleDoubleProperty(0.9);

	private  BooleanProperty showOriginalText = new SimpleBooleanProperty(true);
	private  BooleanProperty showLineSplits = new SimpleBooleanProperty(true);
	private  BooleanProperty showThresholdImage= new SimpleBooleanProperty(false);

	private ObjectProperty<Image> underImage = new SimpleObjectProperty<Image>();
	private ObjectProperty<Image> overImage = new SimpleObjectProperty<Image>();

	private BufferedImage untouchedImage;
	private BufferedImage blurredImage;

	private  ParagraphComponentAnalyzer paragraphAnalyzer = new ParagraphComponentAnalyzer();

	private boolean isEmpty = true;

	public LineViewData() {}


	public Image getOverFXImage() {
		return overImage.get();
	}
	public ObjectProperty<Image> getOverImageProperty() {
		return overImage;
	}
	public BufferedImage getOverBufferedImage() {
		return SwingFXUtils.fromFXImage(overImage.get(), null);
	}
	public void setOverImageProperty(BufferedImage image) {
		isEmpty = false;
		Image fxImage = SwingFXUtils.toFXImage(image, null);
		setOverImageProperty(fxImage);
	}
	public void setOverImageProperty(Image image) {
		isEmpty = false;
		overImage.set(image);
	}
	public void setOverImageProperty(ObjectProperty<Image> imageProperty) {
		isEmpty = false;
		overImage = imageProperty;
	}



	public Image getUnderFXImage() {
		return underImage.get();
	}
	public ObjectProperty<Image> getUnderImageProperty() {
		return underImage;
	}
	public BufferedImage getUnderBufferedImage() {
		return SwingFXUtils.fromFXImage(underImage.get(), null);
	}
	public void setUnderImageProperty(BufferedImage image) {
		isEmpty = false;
		Image fxImage = SwingFXUtils.toFXImage(image, null);
		setUnderImageProperty(fxImage);
	}
	public void setUnderImageProperty(Image image) {
		isEmpty = false;
		underImage.set(image);
	}
	public void setUnderImageProperty(ObjectProperty<Image> imageProperty) {
		isEmpty = false;
		underImage = imageProperty;
	}


	public  ParagraphComponentAnalyzer getParagraphAnalyzer() {
		return paragraphAnalyzer;
	}

	public  BufferedImage getUntouchedImage() { return this.untouchedImage; }
	public void setUntouchedImage(BufferedImage image) { 
		isEmpty = false;
		this.untouchedImage = image; 
	}

	public  BufferedImage getBlurredImage() { return this.blurredImage; }
	public void setBlurredImage(BufferedImage image) { 
		isEmpty = false;
		this.blurredImage = image; 
	}


	public  BooleanProperty getShowLineSplits() { return showLineSplits; }
	public  BooleanProperty getShowOriginalText() { return showOriginalText; }
	public  BooleanProperty getShowThresholdImage() { return showThresholdImage; }


	public  DoubleProperty getStandardBlurNeighborhood() { return standardBlurNeighborhood; }
	public  DoubleProperty getStandardBlurIterations() { return standardBlurIterations; }
	public  DoubleProperty getHorizontalBlurNeighborhood() { return horizontalBlurNeighborhood; }
	public  DoubleProperty getHorizontalBlurIterations() { return horizontalBlurIterations; } 
	public  DoubleProperty getThresholdLevel() { return thresholdLevel; }
	
	public boolean isEmpty() {
		if(isEmpty || paragraphAnalyzer.imagesAreEmpty()) {
			return true;
		}
		return false;
	}
}
