package com.zurg.imagetotext.gui.components;

import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageButton extends Button {
	BufferedImage awtImage;
	Image fxImage;
	
	public ImageButton(BufferedImage image, int width) {
		fxImage = SwingFXUtils.toFXImage(image, null);
		awtImage = image;
		ImageView imageView = new ImageView(fxImage);
		imageView.fitWidthProperty().set(width);
		imageView.preserveRatioProperty().set(true);
		this.setGraphic(imageView);
		this.setWidth(width);
	}
	
//	public BufferedImage getImage() {
//		return awtImage;
//	}
}
