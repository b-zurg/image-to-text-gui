package com.zurg.imagetotext.gui.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.zurg.imagetotext.gui.Main;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;


public class RootLayoutController {
	@FXML 
	private MenuItem openImageMenuItem;
	@FXML
	private MenuItem closeAppMenuItem;
	
	private Main mainApp;
	private DefaultSceneController defaultSceneController;
	
	@FXML
	private void openImage(){
        FileChooser fileChooser = new FileChooser();
        
        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
          
        //Show open file dialog*
        File file = fileChooser.showOpenDialog(null);
        
        try {
        	if(file != null) {
	        	BufferedImage buffImage = ImageIO.read(file);
	        	mainApp.setDefaultSceneImage(buffImage);
        	}
        	
        } catch (IOException  e) {
        	System.out.println("jfdklas;jfkdls;afjdklsa\n\n");
        	System.out.println("Could not open image because of: " + e);
        }
        
	}
	
	@FXML 
	private void closeMainApp() {
		mainApp.getPrimaryStage().close();
	}
	
	public void setMainApp(Main mainapp) {
		this.mainApp = mainapp;
	}
	


	
}
