package com.zurg.imagetotext.model;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import com.google.common.collect.Lists;
import com.zurg.imagetotext.gui.components.ImageButton;
import com.zurg.imagetotext.gui.state.SubImageStateTracker;


public class SubImagesSceneData {
	private List<BufferedImage> subImages = Lists.newArrayList();
	private List<SubImageStateTracker> subStates = Lists.newArrayList();
	
	private boolean isDefaultState = true;
	
	public List<BufferedImage> getSubImages() {
		return subImages;
	}
	public List<Image> getFXSubImages() {
		return getSubImages().stream().map(i -> SwingFXUtils.toFXImage(i, null)).collect(Collectors.toList());
	}
	
	public void setSubImages(List<BufferedImage> subImages) {
		this.subImages = subImages;
	}
	public void addSubImage(BufferedImage image) {
		if(isDefaultState) {
			isDefaultState = false;
			subImages.clear();
		}
		this.subImages.add(image);
	}
	
	public List<SubImageStateTracker> getSubStates() {
		return subStates;
	}
	public void setSubStates(List<SubImageStateTracker> subStates) {
		this.subStates = subStates;
	}
	
	public boolean isDefaultState() {
		return isDefaultState;
	}
	public void setDefaultState(boolean state) {
		this.isDefaultState = true;
	}
	
	public void clearSubImages() {
		this.subImages.clear();
		this.subStates.clear();
	}
}
