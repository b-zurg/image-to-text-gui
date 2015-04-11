package com.zurg.imagetotext.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;




import java.io.InputStream;

import com.zurg.imagetotext.gui.state.StateContainer;
import com.zurg.imagetotext.gui.state.SubImageStateTracker;
import com.zurg.imagetotext.gui.view.FinishSceneController;
import com.zurg.imagetotext.gui.view.FontSceneController;
import com.zurg.imagetotext.gui.view.ModificationSceneController;
import com.zurg.imagetotext.gui.view.LineSceneController;
import com.zurg.imagetotext.gui.view.RootLayoutController;
import com.zurg.imagetotext.gui.view.SubImageSceneController;
import com.zurg.imagetotext.model.FinishSceneData;
import com.zurg.imagetotext.model.FontViewData;
import com.zurg.imagetotext.model.LineViewData;
import com.zurg.imagetotext.model.ModificationViewData;
import com.zurg.imagetotext.model.SubImagesSceneData;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	private Stage primaryStage;
	private BorderPane rootLayout;

	private RootLayoutController rootLayoutController;
	private ModificationSceneController modificationSceneController;
	private LineSceneController lineSceneController;
	private FontSceneController fontSceneController;
	private FinishSceneController finishSceneController;
	private SubImageSceneController subImageSceneController;


//	private ModificationViewData modificationData;

	public StateContainer currentStateContainer = new StateContainer();
	private SubImageStateTracker subImageTracker = new SubImageStateTracker();
	

	private AnchorPane modificationScene, lineScene, fontScene, finishScene, subImageScene;


	@Override
	public void start(Stage primaryStage) {
		//		this.stateContainer = new StateContainer();
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Image To Text Converter");

		initRootLayout();
		showModificationScene();
		showSubImagesScene();
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

	public void showSubImagesScene() {
		try {
			FXMLLoader loader = new FXMLLoader();
			InputStream stream = getClass().getResourceAsStream("/fxml/SubImageScene.fxml");
			this.subImageScene = (AnchorPane) loader.load(stream);

			subImageSceneController = loader.getController();
			subImageSceneController.setMainApp(this);
			subImageSceneController.setData(getSubImageData());

			rootLayoutController.setBottomPane(subImageScene);	


		} catch (IOException e) {
			System.err.println("Could not load resource because of: " + e);
		}
	}

	public void showLineScene() {

		try {
			FXMLLoader loader = new FXMLLoader();
			InputStream stream = getClass().getResourceAsStream("/fxml/LineScene.fxml");
			lineScene = (AnchorPane) loader.load(stream);

			lineSceneController = loader.getController();

			lineSceneController.setMainApp(this);
			lineSceneController.setData(getLineData());
			rootLayoutController.setCenterPane(lineScene);

		} catch (IOException e) {
			System.err.println("Could not load resource because of: " + e + e.getCause().toString() + e.getLocalizedMessage().toString() + e.getStackTrace().toString() + e.getSuppressed().toString());
		}
	}


	public void showModificationScene() {
		try {
			FXMLLoader loader = new FXMLLoader();
			InputStream stream = getClass().getResourceAsStream("/fxml/ModificationScene.fxml");
			modificationScene = (AnchorPane) loader.load(stream);

			modificationSceneController = loader.getController();
			modificationSceneController.setMainApp(this);
			modificationSceneController.setData(getModificationData());
			rootLayoutController.setCenterPane(modificationScene);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showFontScene() {
		try{
			FXMLLoader loader = new FXMLLoader();
			InputStream stream = getClass().getResourceAsStream("/fxml/FontScene.fxml");
			fontScene = (AnchorPane) loader.load(stream);

			fontSceneController = loader.getController();
			fontSceneController.setMainApp(this);
			fontSceneController.setData(getFontData());
			rootLayoutController.setCenterPane(fontScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showFinishScene() {
		try{
			FXMLLoader loader = new FXMLLoader();
			InputStream stream = getClass().getResourceAsStream("/fxml/FinishScene.fxml");
			finishScene = (AnchorPane) loader.load(stream);

			finishSceneController = loader.getController();
			finishSceneController.setMainApp(this);
			finishSceneController.setData(getFinishData());
			rootLayoutController.setCenterPane(finishScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public SubImagesSceneData getSubImageData() {
		return subImageTracker.getSubImagesSceneData();
	}
	public ModificationViewData getModificationData() {
		return subImageTracker.getModificationData();
	}
	public LineViewData getLineData() {
		return currentStateContainer.getLineData();
	}
	public FontViewData getFontData() {
		return currentStateContainer.getFontData();
	}
	public FinishSceneData getFinishData() {
		return currentStateContainer.getFinishData();
	}

	
	public void setSubImageTrackerTo(SubImageStateTracker tracker) {
		subImageTracker = tracker;
		setStateContainerTo(subImageTracker.getFirstStateContainer());
		showModificationScene();
	}
	
	public void setStateContainerToImage(BufferedImage image) {
		System.out.println("MAIN set state container to image : content present");
		System.out.println("MAIN set state container to image : is container null? " + this.subImageTracker.getStateContainerFor(image));

		setStateContainerTo(this.subImageTracker.getStateContainerFor(image));

		getLineData().setUntouchedImage(image);
		showLineScene();
	}	
	private void setStateContainerTo(StateContainer container) {
		this.currentStateContainer = container;
		updateToContainerData();
	}
	private void updateToContainerData() {
		showFinishScene();
		showFontScene();
		showSubImagesScene();
		showModificationScene();
		showLineScene();
	}

	
	
	public Stage getPrimaryStage() {
		return this.primaryStage;
	}

	
	public void setModificationSceneImage(BufferedImage image) {
		getModificationData().setImageProperty(image);
		showModificationScene();
		
//		this.modificationSceneController.setImage(image);
	}
	
	public void setLineSceneImage(BufferedImage image) {
		showLineScene();
		this.lineSceneController.setOriginalImage(image);
	}
	public void addSubImage(BufferedImage image) {
		this.subImageTracker.addStateContainerFor(image);
		showSubImagesScene();
		this.subImageSceneController.addSubImageButton(image);
	}
	
	public void clearSubImages() {
		this.subImageTracker.clearSubImagesData();
	}
	


	public static void main(String[] args) {
		launch(args);
	}











	//	public void setLineData(LineViewData data) {
	//	currentStateContainer.setLineData(data);
	//}
	//public void setFontData(FontViewData data) {
	//	currentStateContainer.setFontData(data);
	//}
	//public void setModificationData(ModificationViewData data) {
	//	currentStateContainer.setModificationData(data);
	//}
}
