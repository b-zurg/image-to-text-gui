package com.zurg.imagetotext.gui.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;





import com.zurg.imagetotext.gui.Main;
import com.zurg.imagetotext.gui.components.ImageButton;
import com.zurg.imagetotext.gui.state.OverallStateContainer;
import com.zurg.imagetotext.gui.state.SubImageStateTracker;

import utils.ImageUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;


public class RootLayoutController {
	@FXML private MenuItem openImageMenuItem;
	@FXML private MenuItem closeAppMenuItem;
	
	@FXML private VBox vbox;
	@FXML private BorderPane centerPane;
	
	
	@FXML private Button showModificationScene;
	@FXML private Button showLineScene;
	@FXML private Button showFontScene;
	@FXML private Button showFinishScene;
	
	private Main mainApp;
	
	@FXML
	private void openImage(){
		FileChooser fileChooser = createGeneralFileChooser();
        List<File> fileList = fileChooser.showOpenMultipleDialog(null);
        
        if(fileList != null) {
			List<BufferedImage> images = fileList.stream()
					.map(f -> ImageUtils.openImageFromFile(f))
					.collect(Collectors.toList());
			images.stream().forEach(i -> addImageToSelectionPane(i));
        }
	}
	
	private FileChooser createGeneralFileChooser() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterAll = new FileChooser.ExtensionFilter("All Files (*.)", "*.*");
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterAll, extFilterJPG, extFilterPNG);
        return fileChooser;
	}
	
	private void addImageToSelectionPane(BufferedImage image) {
		OverallStateContainer.getInstance();
		OverallStateContainer.addSubImageTrackerFor(image);		
		
		ImageButton imageButton = new ImageButton(image, 200);
		imageButton.setOnAction((event) -> {
			mainApp.setSubImageTrackerTo(OverallStateContainer.getSubImageTrackerForImage(image));		
		});

		vbox.getChildren().add(imageButton);
		vbox.getChildren().add(new Separator());
	}
	
	@FXML 
	private void closeMainApp() {
		mainApp.getPrimaryStage().close();
	}
	
	@FXML
	private void showModificationScene() { mainApp.showModificationScene(); }
	@FXML
	private void showLineScene() { mainApp.showLineScene(); }
	@FXML
	private void showFontScene() { mainApp.showFontScene(); }
	@FXML
	private void showFinishScene() { mainApp.showFinishScene(); }
	
	public void setMainApp(Main mainapp) {
		this.mainApp = mainapp;
	}
	
	public void setCenterPane(Node node) {
		centerPane.setCenter(node);
	}
	public void setBottomPane(Node node) {
		centerPane.setBottom(node);
	}
}
