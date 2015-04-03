package com.zurg.imagetotext.gui;

import java.awt.image.BufferedImage;
import java.util.Map;

import com.google.common.collect.Maps;

public class OverallStateTracker {
	private static Map<BufferedImage, StateContainer> states = Maps.newHashMap();
	private static OverallStateTracker instance;
	
	public static OverallStateTracker getInstance() {
		if(instance == null) {
			instance = new OverallStateTracker();
		}
		return instance;
	}
	
	public StateContainer getStateContainerFor(BufferedImage image) {
		return states.get(image);
	}
	
}
