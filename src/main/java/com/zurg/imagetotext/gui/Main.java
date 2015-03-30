package com.zurg.imagetotext.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;




import java.io.InputStream;

import com.zurg.imagetotext.gui.view.FontSceneController;
import com.zurg.imagetotext.gui.view.ImageSceneController;
import com.zurg.imagetotext.gui.view.LineSceneController;
import com.zurg.imagetotext.gui.view.RootLayoutController;
import com.zurg.imagetotext.gui.view.WordSceneController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	private Stage primaryStage;
	private BorderPane rootLayout;
	private RootLayoutController rootLayoutController;
	private ImageSceneController imageSceneController;
	private LineSceneController lineSceneController;
	private WordSceneController wordSceneController;
	private FontSceneController fontSceneController;
	
	private AnchorPane imageScene, lineScene, wordScene, fontScene;
	
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Image To Text Converter");
		
		initRootLayout();
		showLineScene();
	}

	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader(); 
			InputStream stream = getClass().getResourceAsStream("/fxml/RootLayout.fxml");
			this.rootLayout = loader.load(stream);

			rootLayoutController = loader.getController();
			rootLayoutController.setMainApp(this);

			
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e){
			System.err.println("Could not load resource because of: " + e);
		}
	}
	
	public void showLineScene() {
		try {
			if(lineScene == null) {
				FXMLLoader loader = new FXMLLoader();
				InputStream stream = getClass().getResourceAsStream("/fxml/LineScene.fxml");
				lineScene = (AnchorPane) loader.load(stream);
				
				lineSceneController = loader.getController();
				lineSceneController.setMainApp(this);
			}
			rootLayout.setCenter(lineScene);

		} catch (IOException e) {
    		System.err.println("Could not load resource because of: " + e + e.getCause().toString() + e.getLocalizedMessage().toString() + e.getStackTrace().toString() + e.getSuppressed().toString());
		}
	}
	
	public void showWordScene() {
		try {
			if(wordScene == null) {
				FXMLLoader loader = new FXMLLoader();
				InputStream stream = getClass().getResourceAsStream("/fxml/WordScene.fxml");
				wordScene = (AnchorPane) loader.load(stream);
				
				wordSceneController = loader.getController();
				wordSceneController.setMainApp(this);
			}
			rootLayout.setCenter(wordScene);
		} catch (IOException e) {
			System.err.println("Could not load resource because of: " + e + e.getCause().toString() + e.getLocalizedMessage().toString() + e.getStackTrace().toString() + e.getSuppressed().toString());
		}
	}
	
	
	public void showImageScene() {
		try {
			if(imageScene == null) {
				FXMLLoader loader = new FXMLLoader();
				InputStream stream = getClass().getResourceAsStream("/fxml/ImageScene.fxml");
				imageScene = (AnchorPane) loader.load(stream);
				
				imageSceneController = loader.getController();
				imageSceneController.setMainApp(this);
			}
			rootLayout.setCenter(imageScene);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void showFontScene() {
		try{
			if(fontScene == null) {
				FXMLLoader loader = new FXMLLoader();
				InputStream stream = getClass().getResourceAsStream("/fxml/FontScene.fxml");
				fontScene = (AnchorPane) loader.load(stream);
				
				fontSceneController = loader.getController();
				fontSceneController.setMainApp(this);
			}
			rootLayout.setCenter(fontScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Stage getPrimaryStage() {
		return this.primaryStage;
	}
	
	public void setLineSceneImage(BufferedImage image) {
		this.lineSceneController.setOriginalImage(image);
	}

	
	public static void main(String[] args) {
		launch(args);
	}
}
