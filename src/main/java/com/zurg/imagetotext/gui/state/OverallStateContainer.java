package com.zurg.imagetotext.gui.state;

import java.awt.image.BufferedImage;
import java.util.Map;

import com.google.common.collect.Maps;

public class OverallStateContainer {
	private static OverallStateContainer instance;
	
	private static Map<BufferedImage, SubImageStateTracker> states = Maps.newHashMap();
	
	public static OverallStateContainer getInstance() {
		if(instance == null) {
			instance = new OverallStateContainer();
		}
		return instance;
	}
	
	public static SubImageStateTracker getSubImageTrackerForImage(BufferedImage image) {
		return states.get(image);
	}
	
	public static void addSubImageTrackerFor(BufferedImage image) {
		states.put(image, new SubImageStateTracker(image));	
	}
	

	
	
}
