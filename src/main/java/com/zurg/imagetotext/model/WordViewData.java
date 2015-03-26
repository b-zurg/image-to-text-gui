package com.zurg.imagetotext.model;

import java.awt.image.BufferedImage;
import java.util.List;

import com.google.common.collect.Lists;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.image.ImageView;

public class WordViewData {
	private static WordViewData instance = null;
	
	private static List<BufferedImage> untouchedImages = Lists.newArrayList();
	private static List<ImageView> overImageViews = Lists.newArrayList();
	private static List<ImageView> underImageViews = Lists.newArrayList();
	private static List<Group> imageGroups = Lists.newArrayList();
	
	private static DoubleProperty standardBlurNeighborhood = new SimpleDoubleProperty(9.0);
	private static DoubleProperty standardBlurIterations = new SimpleDoubleProperty(2.0);
	
	private static DoubleProperty verticalBlurNeighborhood = new SimpleDoubleProperty(9.0);
	private static DoubleProperty verticalBlurIterations = new SimpleDoubleProperty(2.0);

	private static DoubleProperty thresholdLevel = new SimpleDoubleProperty(0.9);
	
	private static BooleanProperty showOriginalText = new SimpleBooleanProperty(false);
	private static BooleanProperty showLineSplits = new SimpleBooleanProperty(false);
	private static BooleanProperty showThresholdImage= new SimpleBooleanProperty(false);

	
	public static WordViewData getInstance() {
		if(instance == null) {
			instance = new WordViewData();
		}
		return instance;
	}

	public static List<ImageView> getOverImageViews() {
		return overImageViews;
	}
	public static List<ImageView> getUnderImageViews() {
		return underImageViews;
	}
	public static List<BufferedImage> getUntouchedImages() {
		return untouchedImages;
	}
	
	public static DoubleProperty getStandardBlurNeighborhood() { return standardBlurNeighborhood; }
	public static DoubleProperty getStandardBlurIterations() { return standardBlurIterations; }
	public static DoubleProperty getVerticalBlurNeighborhood() { return verticalBlurNeighborhood; }
	public static DoubleProperty getVerticalBlurIterations() { return verticalBlurIterations; } 
	public static DoubleProperty getThresholdLevel() { return thresholdLevel; }

	public static BooleanProperty getShowLineSplits() { return showLineSplits; }
	public static BooleanProperty getShowOriginalText() { return showOriginalText; }
	public static BooleanProperty getShowThresholdImage() { return showThresholdImage; }
	
}
