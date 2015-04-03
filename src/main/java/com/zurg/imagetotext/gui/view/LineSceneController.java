package com.zurg.imagetotext.gui.view;

import java.awt.image.BufferedImage;
import java.util.List;

import com.zurg.imagetotext.gui.Main;
import com.zurg.imagetotext.model.LineViewData;

import document.analysis.LineComponentAnalyzer;
import document.analysis.ParagraphComponentAnalyzer;
import utils.ImageUtils;


import utils.MyImageIO;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.layout.HBox;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.NumberStringConverter;

public class LineSceneController {
	@FXML private ScrollPane scrollPane;

	@FXML private Button applyButton;
	
	//~~||standard blur controls||~~//
	@FXML private Slider standardBlurNeighborhoodSlider;
	@FXML private Slider standardBlurIterationsSlider;
	
	@FXML private TextField standardBlurNeighborhoodText;
	@FXML private TextField standardBlurIterationsText;
	//~~--------------------------~~//
	
	//~~||horizontal blur controls||~~//
	@FXML private Slider horizontalBlurNeighborhoodSlider;
	@FXML private Slider horizontalBlurIterationsSlider;
	
	@FXML private TextField horizontalBlurNeighborhoodText;
	@FXML private TextField horizontalBlurIterationsText;
	//~~--------------------------~~//

	@FXML private Slider thresholdLevelSlider;
	@FXML private TextField thresholdLevelText;

	@FXML private RadioButton overlapTextButton;
	@FXML private RadioButton showLineSplitsButton;
	@FXML private RadioButton showThresholdButton;
	@FXML private RadioButton hotfixLinesButton;
	
	private Main mainApp;
		
	private static int NONE = 0, BLUROVERLAY = 1, THRESHOLDOVERLAY = 2;
	
	private ImageView underImageView, overImageView;
	

	@FXML
	private void initialize() {		
		LineViewData.getInstance();
		
		syncSlidersWithTextFields();
		setSliderProperties();
		bindSlidersToSingleton();	
		setToggleButtonValsToSingleton();
	}
	
	private void setSliderProperties() {
		setNeighborhoodSliderProperties(standardBlurNeighborhoodSlider);
		setGeneralSliderProperties(standardBlurNeighborhoodSlider);
		
		setNeighborhoodSliderProperties(horizontalBlurNeighborhoodSlider);
		setGeneralSliderProperties(horizontalBlurNeighborhoodSlider);
		
		setIterationSliderProperties(standardBlurIterationsSlider);
		setGeneralSliderProperties(standardBlurIterationsSlider);

		setIterationSliderProperties(horizontalBlurIterationsSlider);
		setGeneralSliderProperties(horizontalBlurIterationsSlider);
		
		thresholdLevelSlider.setMin(0);
		thresholdLevelSlider.setMax(1);
		thresholdLevelSlider.setMajorTickUnit(0.05);
		setGeneralSliderProperties(thresholdLevelSlider);
	}
	private void setNeighborhoodSliderProperties(Slider slider) {
		slider.setMin(1);
		slider.setMax(20);
		slider.setMajorTickUnit(1);
	}
	private void setIterationSliderProperties(Slider slider) {
		slider.setMin(1);
		slider.setMax(6);
		slider.setMajorTickUnit(1);
	}
	private void setGeneralSliderProperties(Slider slider) {
		slider.setMinorTickCount(0);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setSnapToTicks(true);
	}
	private void syncSlidersWithTextFields() {
		standardBlurNeighborhoodText.textProperty().bindBidirectional(standardBlurNeighborhoodSlider.valueProperty(), new NumberStringConverter());
		standardBlurIterationsText.textProperty().bindBidirectional(standardBlurIterationsSlider.valueProperty(), new NumberStringConverter());
		horizontalBlurNeighborhoodText.textProperty().bindBidirectional(horizontalBlurNeighborhoodSlider.valueProperty(), new NumberStringConverter());
		horizontalBlurIterationsText.textProperty().bindBidirectional(horizontalBlurIterationsSlider.valueProperty(), new NumberStringConverter());
		thresholdLevelText.textProperty().bindBidirectional(thresholdLevelSlider.valueProperty(), new NumberStringConverter());
	}	
	private void bindSlidersToSingleton() {
		standardBlurNeighborhoodSlider.valueProperty().bindBidirectional(LineViewData.getStandardBlurNeighborhood());
		standardBlurIterationsSlider.valueProperty().bindBidirectional(LineViewData.getStandardBlurIterations());
		horizontalBlurNeighborhoodSlider.valueProperty().bindBidirectional(LineViewData.getHorizontalBlurNeighborhood());
		horizontalBlurIterationsSlider.valueProperty().bindBidirectional(LineViewData.getHorizontalBlurIterations());
		thresholdLevelSlider.valueProperty().bindBidirectional(LineViewData.getThresholdLevel());
	}
	private void setToggleButtonValsToSingleton() {
		overlapTextButton.selectedProperty().bindBidirectional(LineViewData.getShowOriginalText());
		showLineSplitsButton.selectedProperty().bindBidirectional(LineViewData.getShowLineSplits());
		showThresholdButton.selectedProperty().bindBidirectional(LineViewData.getShowThresholdImage());
		hotfixLinesButton.selectedProperty().bindBidirectional(LineViewData.getHotfixLines());
	}
	
	
	@FXML
	private void handleApplyButtonClick(){
		int standardBlurIterations = LineViewData.getStandardBlurIterations().intValue();
		int standardBlurNeighborhood = LineViewData.getStandardBlurNeighborhood().intValue();
		int horizontalBlurIterations = LineViewData.getHorizontalBlurIterations().intValue();
		int horizontalBlurNeighborhood = LineViewData.getHorizontalBlurNeighborhood().intValue();
		
		
		BufferedImage imageToBlur = ImageUtils.copyImage(LineViewData.getUntouchedImage());
		
		ImageUtils.blurImageFast(imageToBlur, standardBlurNeighborhood, standardBlurIterations);
		ImageUtils.blurImageHorizontal(imageToBlur, horizontalBlurNeighborhood, horizontalBlurIterations);
		LineViewData.setBlurredImage(imageToBlur);
		
		this.handleRadioButtonConfigs();
	}
	
	@FXML
	private void handleRadioButtonConfigs() {
		
		boolean showLineSplits = LineViewData.getShowLineSplits().getValue();
		boolean showOriginalText = LineViewData.getShowOriginalText().getValue();
		boolean showThresholdImage = LineViewData.getShowThresholdImage().getValue();
		BufferedImage underImage = ImageUtils.copyImage(LineViewData.getBlurredImage());
		BufferedImage overImage = ImageUtils.copyImage(LineViewData.getUntouchedImage());
		int overlapMode = 0;


		if (showOriginalText) {
			if(showThresholdImage) {
				overlapMode = LineSceneController.THRESHOLDOVERLAY;
				ImageUtils.threshold(underImage, LineViewData.getThresholdLevel().get());
				if(showLineSplits) {
					this.addLineSplitsToImage(overImage);

				}
			}
			else if(!showThresholdImage) {
				overlapMode = LineSceneController.BLUROVERLAY;
				if(showLineSplits) {
					this.addLineSplitsToImage(overImage);

				}
			}
		}
		else if (!showOriginalText) {
			overlapMode = LineSceneController.NONE;
			if(showThresholdImage) {
				ImageUtils.threshold(underImage, LineViewData.getThresholdLevel().get());
				if(showLineSplits) {
					this.addLineSplitsToImage(underImage);
				}
			}
			else if(!showThresholdImage) {
				if(showLineSplits) {
					this.addLineSplitsToImage(underImage);
				}
			}
		}
		
		setOverlapMode(overlapMode, underImage, overImage);
		
	}
	
	private void setOverlapMode(int mode, BufferedImage underImage, BufferedImage overImage) {
		Image underImageFX = SwingFXUtils.toFXImage(underImage, null);
		Image overImageFX = SwingFXUtils.toFXImage(overImage, null);
		underImageView.setImage(underImageFX);
		overImageView.setImage(overImageFX);
		
		Group images = null;
		if(mode == LineSceneController.BLUROVERLAY) {
			underImageView.setBlendMode(BlendMode.MULTIPLY);
			images = new Group(overImageView, underImageView);
		}
		if(mode == LineSceneController.THRESHOLDOVERLAY) {
			underImageView.setBlendMode(BlendMode.EXCLUSION);
			images = new Group(overImageView, underImageView);
		}
		if(mode == LineSceneController.NONE){
			images = new Group(underImageView);
		}
		this.scrollPane.setContent(images);
	}
	
	private void addLineSplitsToImage(BufferedImage image) {
		BufferedImage untouchedImage = LineViewData.getUntouchedImage();
		BufferedImage blurredImage = LineViewData.getBlurredImage();

		ParagraphComponentAnalyzer paragraphAnalyzer = LineViewData.getParagraphAnalyzer();

		paragraphAnalyzer.setImages(untouchedImage, blurredImage);
		paragraphAnalyzer.setThreshold(thresholdLevelSlider.getValue());
		
		List<Integer> lineSplits = paragraphAnalyzer.getLineSplits();
					
		ImageUtils.createHorizontalRedLinesAt(image, lineSplits);
	}

	
	public void setOriginalImage(BufferedImage image) {
		LineViewData.setBlurredImage(ImageUtils.copyImage(image));
		LineViewData.setUntouchedImage(ImageUtils.copyImage(image));
		
		initializeImageViews();
	}
	
	private void initializeImageViews() {	
		Image originalImageFX = SwingFXUtils.toFXImage(LineViewData.getUntouchedImage(), null);
		Image blurredImageFX = SwingFXUtils.toFXImage(LineViewData.getBlurredImage(), null);
	
		underImageView = new ImageView(blurredImageFX);
		underImageView.fitWidthProperty().bind(scrollPane.widthProperty());
		underImageView.preserveRatioProperty().set(true);
		
		overImageView = new ImageView(originalImageFX);		
		overImageView.fitWidthProperty().bind(scrollPane.widthProperty());
		overImageView.preserveRatioProperty().set(true);	
		
		addImageViewsToScrollPane();
	}
	
	private void addImageViewsToScrollPane() {
		underImageView.setBlendMode(BlendMode.MULTIPLY);
		Group blended = new Group(underImageView);
		this.scrollPane.setContent(blended);
	}
	
	public void setMainApp(Main main) {
		this.mainApp = main;
	}
}


//maybe I'll use this later

/*
	private void setBlurNeighborhoodSliderActionListener(){
		blurNeighborhoodSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
//			Integer neighborhood = (int) (newValue.intValue()/10)+1;
//			this.blurNeighborhoodText.setText(neighborhood.toString());

		});
	}
	
	private void setBlurIterationsSliderActionListener(){
		blurIterationsSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
//			Integer iterations = (Integer) (newValue.intValue()/10)+1;
//			this.blurIterationsText.setText(iterations.toString());
		});
	}


@FXML 
	private void overlapOriginalText() {
		if(overlapTextButton.isSelected()) {
			underImageView.setBlendMode(BlendMode.MULTIPLY);
			blended = new Group(overImageView, underImageView);
			this.scrollPane.setContent(blended);
		}
		if(!overlapTextButton.isSelected()) {
			this.scrollPane.setContent(new Group(underImageView));
		}
	}

//	@FXML
//	private void showLineSplits() {
//		if(showLineSplitsButton.isSelected()) {
//			BufferedImage untouchedImage = SwingFXUtils.fromFXImage(originalImageFX, null);
//			BufferedImage blurredImage = SwingFXUtils.fromFXImage(blurredImageFX, null);
//			paragraphAnalyzer.setImages(untouchedImage, blurredImage);
//			paragraphAnalyzer.setThreshold(thresholdLevelSlider.getValue());
//			
//			List<Integer> lineSplits = paragraphAnalyzer.getLineSplits();
//						
//			ImageUtils.createHorizontalRedLinesAt(untouchedImage, lineSplits);
//			overImageView.setImage(SwingFXUtils.toFXImage(untouchedImage, null));
//		}
//		if(!showLineSplitsButton.isSelected()) {
//			overImageView.setImage(SwingFXUtils.toFXImage(originalImage, null));
//		}
//	}
*
*
*/