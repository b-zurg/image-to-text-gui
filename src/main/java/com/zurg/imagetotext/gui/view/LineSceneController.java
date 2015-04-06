package com.zurg.imagetotext.gui.view;

import java.awt.image.BufferedImage;
import java.util.List;

import com.zurg.imagetotext.gui.Main;
import com.zurg.imagetotext.model.LineViewData;

import document.analysis.ParagraphComponentAnalyzer;
import utils.ImageUtils;


import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
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
	
	private Main mainApp;
	private LineViewData lineData;
	
	private static int NONE = 0, BLUROVERLAY = 1, THRESHOLDOVERLAY = 2;
	
	private ImageView underImageView = new ImageView();
	private ImageView overImageView = new ImageView();
	

	@FXML
	private void initialize() {		
		
		syncSlidersWithTextFields();
		setSliderProperties();
	}
	
	public void setData(LineViewData lineData) {
		this.lineData = lineData;
		
		bindSlidersToData();	
		setToggleButtonValsToData();
		bindImageViewsToData();
		if(lineData.getUntouchedImage() != null && lineData.getBlurredImage()!= null) { 
			handleRadioButtonConfigs(); 
		}
		if(lineData.getUntouchedImage() != null && lineData.getBlurredImage() == null) {
			initializeImageViews();
		}
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
	
	private void bindSlidersToData() {
//		standardBlurNeighborhoodSlider.setValue(lineData.getStandardBlurNeighborhood().get());
//		standardBlurIterationsSlider.setValue(lineData.getStandardBlurIterations().get());
//		horizontalBlurNeighborhoodSlider.setValue(lineData.getHorizontalBlurNeighborhood().get());
//		horizontalBlurIterationsSlider.setValue(lineData.getHorizontalBlurIterations().get());
//		thresholdLevelSlider.setValue(lineData.getThresholdLevel().get());
//		
		standardBlurNeighborhoodSlider.valueProperty().bindBidirectional(lineData.getStandardBlurNeighborhood());
		standardBlurIterationsSlider.valueProperty().bindBidirectional(lineData.getStandardBlurIterations());
		horizontalBlurNeighborhoodSlider.valueProperty().bindBidirectional(lineData.getHorizontalBlurNeighborhood());
		horizontalBlurIterationsSlider.valueProperty().bindBidirectional(lineData.getHorizontalBlurIterations());
		thresholdLevelSlider.valueProperty().bindBidirectional(lineData.getThresholdLevel());
	}
	private void setToggleButtonValsToData() {
		overlapTextButton.selectedProperty().bindBidirectional(lineData.getShowOriginalText());
		showLineSplitsButton.selectedProperty().bindBidirectional(lineData.getShowLineSplits());
		showThresholdButton.selectedProperty().bindBidirectional(lineData.getShowThresholdImage());
	}
	private void bindImageViewsToData() {
		underImageView.imageProperty().bindBidirectional(lineData.getUnderImageProperty());
		overImageView.imageProperty().bindBidirectional(lineData.getOverImageProperty());
	}
	
	
	
	@FXML
	private void handleApplyButtonClick(){
		int standardBlurIterations = lineData.getStandardBlurIterations().intValue();
		int standardBlurNeighborhood = lineData.getStandardBlurNeighborhood().intValue();
		int horizontalBlurIterations = lineData.getHorizontalBlurIterations().intValue();
		int horizontalBlurNeighborhood = lineData.getHorizontalBlurNeighborhood().intValue();
		
		
		BufferedImage imageToBlur = ImageUtils.copyImage(lineData.getUntouchedImage());
		
		ImageUtils.blurImageFast(imageToBlur, standardBlurNeighborhood, standardBlurIterations);
		ImageUtils.blurImageHorizontal(imageToBlur, horizontalBlurNeighborhood, horizontalBlurIterations);
		lineData.setBlurredImage(imageToBlur);
		lineData.setUnderImageProperty(imageToBlur);
		
		this.handleRadioButtonConfigs();
	}
	
	@FXML
	private void handleRadioButtonConfigs() {
		
		boolean showLineSplits = lineData.getShowLineSplits().getValue();
		boolean showOriginalText = lineData.getShowOriginalText().getValue();
		boolean showThresholdImage = lineData.getShowThresholdImage().getValue();
		BufferedImage overImage = ImageUtils.copyImage(lineData.getUntouchedImage());
		BufferedImage underImage = ImageUtils.copyImage(lineData.getBlurredImage());
		int overlapMode = 0;


		if (showOriginalText) {
			if(showThresholdImage) {
				overlapMode = LineSceneController.THRESHOLDOVERLAY;
				ImageUtils.threshold(underImage, lineData.getThresholdLevel().get());
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
				ImageUtils.threshold(underImage, lineData.getThresholdLevel().get());
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
		lineData.setUnderImageProperty(underImage);
		lineData.setOverImageProperty(overImage);
		
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
		BufferedImage untouchedImage = lineData.getUntouchedImage();
		BufferedImage blurredImage = lineData.getUnderBufferedImage();

		ParagraphComponentAnalyzer paragraphAnalyzer = lineData.getParagraphAnalyzer();
		
		paragraphAnalyzer.setImages(untouchedImage, blurredImage);
		paragraphAnalyzer.setThreshold(thresholdLevelSlider.getValue());
		
		List<Integer> lineSplits = paragraphAnalyzer.getLineSplits();
					
		ImageUtils.createHorizontalRedLinesAt(image, lineSplits);
	}

	
	public void setOriginalImage(BufferedImage image) {
		lineData.setUntouchedImage(ImageUtils.copyImage(image));
		
		initializeImageViews();
	}
	
	private void initializeImageViews() {	
		BufferedImage original = lineData.getUntouchedImage();
	
		lineData.setUnderImageProperty(original);
		underImageView.fitWidthProperty().bind(scrollPane.widthProperty());
		underImageView.preserveRatioProperty().set(true);

		lineData.setOverImageProperty(original);
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
*/