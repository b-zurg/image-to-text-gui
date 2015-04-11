package com.zurg.imagetotext.gui.state;

import java.awt.image.BufferedImage;
import java.util.Map;

import com.google.common.collect.Maps;
import com.zurg.imagetotext.model.ModificationViewData;
import com.zurg.imagetotext.model.SubImagesSceneData;

public class SubImageStateTracker {
	private Map<BufferedImage, StateContainer> states = Maps.newHashMap();
	private SubImagesSceneData subImageData = new SubImagesSceneData();
	private ModificationViewData modificationData = new ModificationViewData();

	
	
	public SubImageStateTracker(BufferedImage image) {
		addStateContainerFor(image);
		this.subImageData.addSubImage(image);
		this.subImageData.setDefaultState(true);
		this.modificationData.setImageProperty(image);
	}
	
	public SubImageStateTracker() {}
	
	public StateContainer getStateContainerFor(BufferedImage image) {
		System.out.println("subStateTracker: states size: " + states.size());
		return states.get(image);
	}
	
	public void addStateContainerFor(BufferedImage image) {
		StateContainer newContainer = new StateContainer();
		states.put(image, newContainer);
	}
	
	public StateContainer getFirstStateContainer() {
		System.out.println("subStateTracker: states size: " + states.size());
		return states.get(states.keySet().toArray()[0]);
	}
	
	
	public SubImagesSceneData getSubImagesSceneData() {
		return this.subImageData;
	}
	
	
	public ModificationViewData getModificationData() {
		return modificationData;
	}
	public void setModificationData(ModificationViewData modificationData) {
		this.modificationData = modificationData;
	}
	public void setModificationDataImage(BufferedImage image) {
		this.modificationData.setImageProperty(image);
	}
	
	public void clearSubImagesData() {
		this.states.clear();
		this.subImageData.clearSubImages();
	}
}
