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
import com.zurg.imagetotext.model.FontViewData;
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
	
	FontViewData fontData;
	
	BufferedImage lineImage;
	
	@FXML
	private void initialize() {		
		setColSettings();
//		initLineImageToScene();
	}
	
	private void setColSettings() {
		fontNameCol.setCellValueFactory(cell -> cell.getValue().getProcessedFontNameProperty());
		distanceCol.setCellValueFactory(cell -> cell.getValue().getScoreProperty().asString());
		resultCol.setCellValueFactory(cell -> cell.getValue().getOcrResultsProperty());
	}
	
	public void initLineImageToScene() {
		List<BufferedImage> lineImages = mainApp.getLineData().getParagraphAnalyzer().getLineSubImages();
		lineImage = lineImages.get(0);
		fontData.setLineImageProperty(lineImage);
	}
	
	public void setData(FontViewData fontData) {
		this.fontData = fontData;
		bindComponentsToData();
		initLineImageToScene();
	}
	
	private void bindComponentsToData() {
		enterTextField.textProperty().bindBidirectional(fontData.getEnteredText());
		lineView.imageProperty().bind(fontData.getLineImageProperty());
		fontTable.itemsProperty().bindBidirectional(fontData.getTopFontsProperty());
		
		
		fontTable.getSelectionModel()
			.selectedItemProperty()
			.addListener((observableValue, fontInfo, fontInfo2) -> {
				fontData.getSelectedFont().bind(fontInfo2.getUnprocessedFontNameProperty());
				LetterOCR.getInstance().setFont(fontInfo2.getUnprocessedFontName());
			});
	}
	
	@FXML
	private void handleGoButton() {
		String enteredText = enterTextField.getText();
		fontData.setTopFonts(LetterOCR.getInstance().guessFonts(lineImage, enteredText));
	}
	
	public void setMainApp(Main main) {
		this.mainApp = main;
	}
}
