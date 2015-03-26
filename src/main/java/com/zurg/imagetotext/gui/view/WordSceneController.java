package com.zurg.imagetotext.gui.view;

import java.awt.image.BufferedImage;
import java.util.List;

import utils.ImageUtils;

import com.zurg.imagetotext.gui.Main;

import document.analysis.LineComponentAnalyzer;
import document.analysis.ParagraphComponentAnalyzer;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.converter.NumberStringConverter;

public class WordSceneController {
	@FXML
	private ScrollPane scrollPane;
	

	@FXML
	private Button applyButton;
	
	@FXML
	private Slider blurNeighborhoodSlider;
	@FXML
	private Slider blurIterationsSlider;
	@FXML
	private Slider thresholdLevelSlider;
	
	@FXML
	private TextField blurNeighborhoodText;
	@FXML
	private TextField blurIterationsText;
	@FXML
	private TextField thresholdLevelText;
	
	@FXML 
	private RadioButton overlapTextButton;
	@FXML 
	private RadioButton showLineSplitsButton;
	
	
	private Main mainApp;

	private BufferedImage originalImage, blurredImage;
	private Image originalImageFX, blurredImageFX;
	
	private ImageView blurred, original;
	private Group blended;
	
	private LineComponentAnalyzer lineAnalyzer;
	
	@FXML
	private void initialize() {		
		syncSlidersWithTextFields();
		setSliderProperties();
		
		lineAnalyzer = new LineComponentAnalyzer();
	}
	
	private void setSliderProperties() {
		blurNeighborhoodSlider.setMin(1);
		blurNeighborhoodSlider.setMax(20);
		blurNeighborhoodSlider.setMajorTickUnit(1);
		setGeneralSliderProperties(blurNeighborhoodSlider);
		
		blurIterationsSlider.setMin(1);
		blurIterationsSlider.setMax(6);
		blurIterationsSlider.setMajorTickUnit(1);
		setGeneralSliderProperties(blurIterationsSlider);

		thresholdLevelSlider.setMin(0);
		thresholdLevelSlider.setMax(1);
		thresholdLevelSlider.setMajorTickUnit(0.1);
		setGeneralSliderProperties(thresholdLevelSlider);
	}
	
	private void setGeneralSliderProperties(Slider slider) {
		slider.setMinorTickCount(0);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setSnapToTicks(true);
	}

	private void syncSlidersWithTextFields() {
		blurNeighborhoodText.textProperty().bindBidirectional(blurNeighborhoodSlider.valueProperty(), new NumberStringConverter());
		blurIterationsText.textProperty().bindBidirectional(blurIterationsSlider.valueProperty(), new NumberStringConverter());
		thresholdLevelText.textProperty().bindBidirectional(thresholdLevelSlider.valueProperty(), new NumberStringConverter());
	}
	
	@FXML
	private void handleApplyButtonClick(){
		int blurIterations = Integer.parseInt(blurIterationsText.getText());
		int blurNeighborhood = Integer.parseInt(blurNeighborhoodText.getText());
		
		this.blurImage(blurNeighborhood, blurIterations);
	}
	
	@FXML 
	private void overlapOriginalText() {
		if(overlapTextButton.isSelected()) {
			blurred.setBlendMode(BlendMode.MULTIPLY);
			blended = new Group(original, blurred);
			this.scrollPane.setContent(blended);
		}
		if(!overlapTextButton.isSelected()) {
			this.scrollPane.setContent(new Group(blurred));
		}
	}
	
	@FXML
	private void showLineSplits() {
		if(showLineSplitsButton.isSelected()) {
			BufferedImage untouchedImage = SwingFXUtils.fromFXImage(originalImageFX, null);
			BufferedImage blurredImage = SwingFXUtils.fromFXImage(blurredImageFX, null);
			lineAnalyzer.setImages(untouchedImage, blurredImage);
			lineAnalyzer.setThreshold(thresholdLevelSlider.getValue());
			
//			List<Integer> wordSplits = lineAnalyzer.getWordSubImages();
						
//			ImageUtils.createHorizontalRedLinesAt(untouchedImage, lineSplits);
//			original.setImage(SwingFXUtils.toFXImage(untouchedImage, null));
		}
		if(!showLineSplitsButton.isSelected()) {
			original.setImage(SwingFXUtils.toFXImage(originalImage, null));
		}
	}
	
	private void blurImage(int neighborhoodSize, int iterations){
		BufferedImage imageToBlur = ImageUtils.copyImage(originalImage);
		ImageUtils.blurImageFast(imageToBlur, neighborhoodSize, iterations);
		blurredImageFX = SwingFXUtils.toFXImage(imageToBlur, null);
		blurred.setImage(blurredImageFX);
		
		this.blended = new Group(blurred);
		this.scrollPane.setContent(blended);
	}
	
	public void showImage() {
		this.scrollPane.setContent(original);

	}
	
	public void setOriginalImage(BufferedImage image) {
		this.originalImage = image;
		this.blurredImage = originalImage;
		this.originalImageFX = SwingFXUtils.toFXImage(originalImage, null);
		this.blurredImageFX = SwingFXUtils.toFXImage(blurredImage, null);
		
		initializeImageViews();
	}
	
	private void initializeImageViews() {
		blurred = new ImageView(blurredImageFX);
		blurred.fitWidthProperty().bind(scrollPane.widthProperty());
		blurred.preserveRatioProperty().set(true);
		
		original = new ImageView(originalImageFX);		
		original.fitWidthProperty().bind(scrollPane.widthProperty());
		original.preserveRatioProperty().set(true);		
	}
	
	public void setMainApp(Main main) {
		this.mainApp = main;
	}
}
