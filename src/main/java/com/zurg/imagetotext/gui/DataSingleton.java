package com.zurg.imagetotext.gui;

import java.awt.image.BufferedImage;

public class DataSingleton {
	private static DataSingleton instance = null;
	
	//ensures class can't be instantiated publicly
	protected DataSingleton() {}
	private static BufferedImage untouchedParagraph;
	private static BufferedImage blurredParagraph;
	
	private static double standardBlurNeighborhood;
	private static double standardBlurIterations;
	
	private static double verticalBlurNeighborhood;
	private static double verticalBlurIterations;
	
	public static DataSingleton getInstance() {
		if(instance == null) {
			instance = new DataSingleton();
		}
		return instance;
	}
	
	public static void setUntouchedParagraph(BufferedImage image) { 
		untouchedParagraph = image;
	}
	public static void setBlurredParagraph(BufferedImage image) { 
		blurredParagraph = image;
	}
	
	public static void setStandardBlurNeighborhood(double val) {
		standardBlurNeighborhood = val;
	}
	public static void setStandardBlurIterations(double val) {
		standardBlurIterations = val;
	}
	
	public static void setVerticalBlurNeighborhood(double val) {
		verticalBlurNeighborhood = val;
	}
	public static void setVerticalBlurIterations(double val) {
		verticalBlurIterations = val;
	}
	
	public static BufferedImage getUntouchedParagraph() { return untouchedParagraph; }
	public static BufferedImage getBlurredParagraph() { return blurredParagraph; }
	public static double getStandardBlurNeighborhood() { return standardBlurNeighborhood; }
	public static double getStandardBlurIterations() { return standardBlurIterations; }
	public static double getVerticalBlurNeighborhood() { return verticalBlurNeighborhood; }
	public static double getVerticalBlurIterations() { return verticalBlurIterations; } 
}
