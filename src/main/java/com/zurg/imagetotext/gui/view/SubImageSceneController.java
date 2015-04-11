package com.zurg.imagetotext.gui.view;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;

import com.zurg.imagetotext.gui.Main;
import com.zurg.imagetotext.gui.components.ImageButton;
import com.zurg.imagetotext.gui.state.OverallStateContainer;
import com.zurg.imagetotext.model.SubImagesSceneData;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;


public class SubImageSceneController {
	private Main mainApp;
	private SubImagesSceneData data;
	
	
	@FXML
	private HBox hbox;
	
	@FXML
	private void initialize() {
		
	}
	
	@FXML
	public void setData(SubImagesSceneData data) {
		//TODO move away from storing buttons;
		this.data = data;
		List<BufferedImage> images = data.getSubImages();
		images.stream().forEach(img -> addSubImageButton(SwingFXUtils.toFXImage(img, null), img));
		
	}
	
	public void setMainApp(Main main) {
		this.mainApp = main;
	}
	
	public void addSubImageButton(BufferedImage image) {
		checkForDefault();
		
		data.addSubImage(image);
		addSubImageButton(SwingFXUtils.toFXImage(image, null), image);
	}
	public void addSubImageButton(Image image, BufferedImage buffImage) {
		checkForDefault();
		
		ImageButton subImageButton = new ImageButton(image, 200);
		subImageButton.setOnAction((event) -> {
			mainApp.setStateContainerToImage(buffImage);		
		});
		

		hbox.getChildren().add(subImageButton);
		hbox.getChildren().add(new Separator());
	}
	
	private void checkForDefault() {
		if(data.isDefaultState()) {
			hbox.getChildren().clear();
		}
	}
}
