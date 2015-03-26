package com.zurg.imagetotext.model;

import java.awt.image.BufferedImage;

import document.analysis.ParagraphComponentAnalyzer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class LineViewData {
	private static LineViewData instance = null;
	
	//ensures class can't be instantiated publicly
	protected LineViewData() {}
	private static BufferedImage untouchedParagraph;
	private static BufferedImage blurredParagraph;
	
	private static DoubleProperty standardBlurNeighborhood = new SimpleDoubleProperty(9.0);
	private static DoubleProperty standardBlurIterations = new SimpleDoubleProperty(2.0);
	
	private static DoubleProperty horizontalBlurNeighborhood = new SimpleDoubleProperty(9.0);
	private static DoubleProperty horizontalBlurIterations = new SimpleDoubleProperty(2.0);
	
	private static DoubleProperty thresholdLevel = new SimpleDoubleProperty(0.9);
	
	private static BooleanProperty showOriginalText = new SimpleBooleanProperty(false);
	private static BooleanProperty showLineSplits = new SimpleBooleanProperty(false);
	private static BooleanProperty showThresholdImage= new SimpleBooleanProperty(false);
	private static BooleanProperty hotfixLines = new SimpleBooleanProperty(true);

	private static ParagraphComponentAnalyzer paragraphAnalyzer = new ParagraphComponentAnalyzer();
	
	
	public static LineViewData getInstance() {
		if(instance == null) {
			instance = new LineViewData();
		}
		return instance;
	}
	
	public static boolean isInitialized() {
		return (instance != null);
	}
	
	public static void setUntouchedImage(BufferedImage untouched) {
		untouchedParagraph = untouched;
	}
	public static void setBlurredImage(BufferedImage blurred) {
		blurredParagraph = blurred;
	}

	public static ParagraphComponentAnalyzer getParagraphAnalyzer() {
		return paragraphAnalyzer;
	}
	
	public static BufferedImage getUntouchedImage() { return untouchedParagraph; }
	public static BufferedImage getBlurredImage() { return blurredParagraph; } 
	
	public static BooleanProperty getShowLineSplits() { return showLineSplits; }
	public static BooleanProperty getShowOriginalText() { return showOriginalText; }
	public static BooleanProperty getShowThresholdImage() { return showThresholdImage; }
	public static BooleanProperty getHotfixLines() { return hotfixLines; }

	
	public static DoubleProperty getStandardBlurNeighborhood() { return standardBlurNeighborhood; }
	public static DoubleProperty getStandardBlurIterations() { return standardBlurIterations; }
	public static DoubleProperty getHorizontalBlurNeighborhood() { return horizontalBlurNeighborhood; }
	public static DoubleProperty getHorizontalBlurIterations() { return horizontalBlurIterations; } 
	public static DoubleProperty getThresholdLevel() { return thresholdLevel; }
}
