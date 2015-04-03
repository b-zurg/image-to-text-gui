package com.zurg.imagetotext.gui.view;

import java.awt.image.BufferedImage;
import java.util.List;

import com.zurg.imagetotext.gui.Main;
import com.zurg.imagetotext.model.FontViewData;
import com.zurg.imagetotext.model.LineViewData;
import com.zurg.imagetotext.model.WordViewData;

import document.analysis.LineComponentAnalyzer;
import document.analysis.WordComponentAnalyzer;
import document.structure.Line;
import document.structure.Word;
import recognition.LetterOCR;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class FinishSceneController {

	@FXML private Button convertButton;
	@FXML private TextFlow textArea;
	
	Main mainApp;
	
	
	@FXML
	private void initialize() {
		System.out.println("hello");
	}
	
	@FXML
	private void handleConvert() {
		String paragraph = "";
		List<Line> lines = WordViewData.getAdjustedLineObjects();
		List<BufferedImage> lineImages = LineViewData.getParagraphAnalyzer().getLineSubImages();
		LetterOCR.getInstance().setPageSegmentationMode(LetterOCR.LINE);
		for(BufferedImage image : lineImages) {
			paragraph += LetterOCR.recognize(image);
		}

		Text t1 = new Text(paragraph);
		textArea.getChildren().add(t1);
	}
	
	
	public void setMainApp(Main main) {
		this.mainApp = main;
	}
}
