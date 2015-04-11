package com.zurg.imagetotext.gui.components;

import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageButton extends Button {	
	public ImageButton(BufferedImage image, int width) {
		Image fxImage = SwingFXUtils.toFXImage(image, null);
		applyImageAndSettings(fxImage, width);
	}
	
	public ImageButton(Image image, int width) {
		this.applyImageAndSettings(image, width);
	}
	
	private void applyImageAndSettings(Image img, int width) {
		ImageView imageView = new ImageView(img);
		imageView.fitWidthProperty().set(width);
		imageView.preserveRatioProperty().set(true);
		this.setGraphic(imageView);
		this.setWidth(width);		
	}
}

