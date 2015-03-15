package com.zurg.imagetotext.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;




import java.io.InputStream;

import com.zurg.imagetotext.gui.view.DefaultSceneController;
import com.zurg.imagetotext.gui.view.RootLayoutController;

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
	private DefaultSceneController defaultSceneController;
	private RootLayoutController rootLayoutController;
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Image To Text Converter");
		
		initRootLayout();
		showDefaultOverview();
		
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
	
	public void showDefaultOverview() {
		try {
			FXMLLoader loader = new FXMLLoader();
			InputStream stream = getClass().getResourceAsStream("/fxml/DefaultScene.fxml");
			AnchorPane defaultPage = (AnchorPane) loader.load(stream);
			
			defaultSceneController = loader.getController();
			defaultSceneController.setMainApp(this);
			
			rootLayout.setCenter(defaultPage);
			
			
			
		} catch (IOException e) {
    		System.err.println("Could not load resource because of: " + e + e.getCause().toString() + e.getLocalizedMessage().toString() + e.getStackTrace().toString() + e.getSuppressed().toString());
		}
	}
	
	@Deprecated
	public void showAlternateOverview() {
		try {
			FXMLLoader loader = new FXMLLoader();
			InputStream stream = getClass().getResourceAsStream("/fxml/AlternateTezt.fxml");
			AnchorPane defaultPage = (AnchorPane) loader.load(stream);
			
//			A = loader.getController();
//			defaultSceneController.setMainApp(this);
//			
			rootLayout.setCenter(defaultPage);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Stage getPrimaryStage() {
		return this.primaryStage;
	}
	
	public void setDefaultSceneImage(BufferedImage image) {
		this.defaultSceneController.setOriginalImage(image);
		this.defaultSceneController.showImage();
	}

	
	public static void main(String[] args) {
		launch(args);
	}
}
