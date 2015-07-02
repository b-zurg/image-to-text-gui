package com.zurg.imagetotext.gui.view;

import java.awt.image.BufferedImage;
import java.util.List;

import org.fxmisc.richtext.StyleClassedTextArea;

import com.zurg.imagetotext.gui.Main;
import com.zurg.imagetotext.model.FinishSceneData;
import com.zurg.imagetotext.model.LineViewData;

import recognition.LetterOCR;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

public class FinishSceneController {

	@FXML private Button convertButton;
	@FXML private ScrollPane textPane;
	//	@FXML private TextFlow textArea;

	private TextArea textArea = new TextArea();

	private Main mainApp;
	private FinishSceneData data;

	@FXML
	private void initialize() {
		textPane.setContent(textArea);

		textPane.setFitToHeight(true);
		textPane.setFitToWidth(true);
		textPane.setPadding(new Insets(10, 10, 10, 10));
	}

	@FXML
	private void handleConvert() {
		String paragraph = "";
		List<BufferedImage> lineImages = mainApp.getLineData().getParagraphAnalyzer().getLineSubImages();
		LetterOCR.getInstance();
		LetterOCR.setPageSegmentationMode(LetterOCR.LINE);
		for(BufferedImage image : lineImages) {
			paragraph += LetterOCR.recognize(image).trim()+"\n";
		}
		textArea.setText(paragraph);
		textArea.setStyle("-fx-font-size: 20");

		//		textArea.setScaleShape(true);		
		//		textArea.appendText(paragraph);
//		textArea.setFont(LetterOCR.getFont(30));

		//		TextArea ta = new TextArea();
		//		ta.setText(paragraph);
		//		ta.setFont(LetterOCR.getFont(20.0));


	}

	public void setData(FinishSceneData data) {
		this.data = data;
//		if(!data.isEmpty()){
			textArea.textProperty().bindBidirectional(data.getText());
//		}
	}


	public void setMainApp(Main main) {
		this.mainApp = main;
	}
}
