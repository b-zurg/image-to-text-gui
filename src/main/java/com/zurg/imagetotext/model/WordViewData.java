package com.zurg.imagetotext.model;

import java.awt.image.BufferedImage;
import java.util.List;

import com.google.common.collect.Lists;

import document.analysis.LineComponentAnalyzer;
import document.structure.Line;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;

public class WordViewData {
	private static WordViewData instance = null;
	
	private static List<BufferedImage> untouchedImages = Lists.newArrayList();
	private static List<BufferedImage> blurredImages = Lists.newArrayList();
	private static List<Group> imageGroups = Lists.newArrayList();
	
	private static DoubleProperty standardBlurNeighborhood = new SimpleDoubleProperty(9.0);
	private static DoubleProperty standardBlurIterations = new SimpleDoubleProperty(2.0);
	
	private static DoubleProperty verticalBlurNeighborhood = new SimpleDoubleProperty(9.0);
	private static DoubleProperty verticalBlurIterations = new SimpleDoubleProperty(2.0);

	private static DoubleProperty thresholdLevel = new SimpleDoubleProperty(0.9);
	
	private static BooleanProperty showOriginalText = new SimpleBooleanProperty(false);
	private static BooleanProperty showLineSplits = new SimpleBooleanProperty(false);
	private static BooleanProperty showThresholdImage= new SimpleBooleanProperty(false);

	private static LineComponentAnalyzer lineAnalyzer = new LineComponentAnalyzer();
	
	public static WordViewData getInstance() {
		if(instance == null) {
			instance = new WordViewData();
		}
		return instance;
	}

	public static List<Group> getImageGroups() {
		return imageGroups;
	}

	public static List<BufferedImage> getUntouchedImages() {
		return untouchedImages;
	}
	public static void setUntouchedImages(List<BufferedImage> untouchedImages) {
		WordViewData.untouchedImages = untouchedImages;
	}
	
	public static List<BufferedImage> getBlurredImages() {
		return blurredImages;
	}
	public static void setBlurredImages(List<BufferedImage> blurredImages) {
		WordViewData.blurredImages = blurredImages;
	}

	public static LineComponentAnalyzer getLineAnalyzer() {
		return lineAnalyzer;
	}

	public static void setLineAnalyzer(LineComponentAnalyzer lineAnalyzer) {
		WordViewData.lineAnalyzer = lineAnalyzer;
	}
	
	public static DoubleProperty getStandardBlurNeighborhood() { return standardBlurNeighborhood; }
	public static DoubleProperty getStandardBlurIterations() { return standardBlurIterations; }
	public static DoubleProperty getVerticalBlurNeighborhood() { return verticalBlurNeighborhood; }
	public static DoubleProperty getVerticalBlurIterations() { return verticalBlurIterations; } 
	public static DoubleProperty getThresholdLevel() { return thresholdLevel; }

	public static BooleanProperty getShowLineSplits() { return showLineSplits; }
	public static BooleanProperty getShowOriginalText() { return showOriginalText; }
	public static BooleanProperty getShowThresholdImage() { return showThresholdImage; }
	
	public static List<Line> getAdjustedLineObjects() {
		List<Line> lines = LineViewData.getParagraphAnalyzer().getLineObjects();
		for(Line line : lines) {
			line.setStandardBlur(standardBlurNeighborhood.intValue(), standardBlurIterations.intValue());
			line.setVerticalBlur(verticalBlurNeighborhood.intValue(), verticalBlurIterations.intValue());
			line.setThreshold(thresholdLevel.get());
		}
		return lines;
	}
}
