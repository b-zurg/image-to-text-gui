package com.zurg.imagetotext.gui.view;


import java.awt.image.BufferedImage;
import java.util.List;

import recognition.FontInfo;
import recognition.LetterOCR;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;


import com.zurg.imagetotext.gui.Main;
import com.zurg.imagetotext.model.LineViewData;

public class FontSceneController {
	private Main mainApp;
	
	@FXML ImageView lineView;
	@FXML TextField enterTextField;
	@FXML Button goButton;
	@FXML TableView<FontInfo> fontTable;
	@FXML TableColumn<FontInfo, String> fontNameCol; 
	@FXML TableColumn<FontInfo, String> distanceCol;
	@FXML TableColumn<FontInfo, String> resultCol;
	
	
	BufferedImage lineImage;
	LetterOCR locr;
	
	@FXML
	private void initialize() {		
		locr = new LetterOCR();
		setLineImage();
		setColSettings();
	}
	
	private void setLineImage() {
		List<BufferedImage> lineImages = LineViewData.getParagraphAnalyzer().getLineSubImages();
		lineImage = lineImages.get(1);
		lineView.setImage(SwingFXUtils.toFXImage(lineImage, null));
	}
	
	private void setColSettings() {
		fontNameCol.setCellValueFactory(cell -> cell.getValue().getProcessedFontNameProperty());
		distanceCol.setCellValueFactory(cell -> cell.getValue().getScoreProperty().asString());
		resultCol.setCellValueFactory(cell -> cell.getValue().getOcrResultsProperty());
	}
	
	@FXML
	private void handleGoButton() {
		String enteredText = enterTextField.getText();
		System.out.println("entered text: " + enteredText);
		List<FontInfo> fontInfo = locr.guessFonts(lineImage, enteredText);
		
		ObservableList<FontInfo> fonts = FXCollections.observableArrayList();
		fonts.addAll(fontInfo);
		fontTable.setItems(fonts);

	}
	
	
	public void setMainApp(Main main) {
		this.mainApp = main;
	}
}
