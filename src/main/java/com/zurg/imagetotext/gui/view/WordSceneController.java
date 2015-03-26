package com.zurg.imagetotext.gui.view;

import java.awt.image.BufferedImage;
import java.util.List;

import utils.ImageUtils;

import com.google.common.collect.Lists;
import com.zurg.imagetotext.gui.Main;
import com.zurg.imagetotext.model.LineViewData;
import com.zurg.imagetotext.model.WordViewData;

import document.analysis.LineComponentAnalyzer;
import document.analysis.ParagraphComponentAnalyzer;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;

public class WordSceneController {
	@FXML private ScrollPane scrollPane;
	
	@FXML private VBox vbox;

	@FXML private Button applyButton;
	
	//~~||standard blur controls||~~//
	@FXML private Slider standardBlurNeighborhoodSlider;
	@FXML private Slider standardBlurIterationsSlider;
	
	@FXML private TextField standardBlurNeighborhoodText;
	@FXML private TextField standardBlurIterationsText;
	//~~--------------------------~~//
	
	//~~||vertical blur controls||~~//
	@FXML private Slider verticalBlurNeighborhoodSlider;
	@FXML private Slider verticalBlurIterationsSlider;
	
	@FXML private TextField verticalBlurNeighborhoodText;
	@FXML private TextField verticalBlurIterationsText;
	//~~--------------------------~~//
		
	@FXML private Slider thresholdLevelSlider;
	@FXML private TextField thresholdLevelText;

	@FXML private RadioButton overlapTextButton;
	@FXML private RadioButton showLineSplitsButton;
	@FXML private RadioButton showThresholdButton;

	private static int NONE = 0, BLUROVERLAY = 1, THRESHOLDOVERLAY = 2;

	private Main mainApp;

	private LineComponentAnalyzer lineAnalyzer;
	
	@FXML
	private void initialize() {		
		lineAnalyzer = new LineComponentAnalyzer();
		WordViewData.getInstance();
		
		syncSlidersWithTextFields();
		setSliderProperties();
		bindSlidersToSingleton();
		setToggleButtonValsToSingleton();
		
		initImagesToScene();
	}
	
	private void setSliderProperties() {
		setNeighborhoodSliderProperties(standardBlurNeighborhoodSlider);
		setGeneralSliderProperties(standardBlurNeighborhoodSlider);
		
		setNeighborhoodSliderProperties(verticalBlurNeighborhoodSlider);
		setGeneralSliderProperties(verticalBlurNeighborhoodSlider);
		
		setIterationSliderProperties(standardBlurIterationsSlider);
		setGeneralSliderProperties(standardBlurIterationsSlider);

		setIterationSliderProperties(verticalBlurIterationsSlider);
		setGeneralSliderProperties(verticalBlurIterationsSlider);
		
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
		verticalBlurNeighborhoodText.textProperty().bindBidirectional(verticalBlurNeighborhoodSlider.valueProperty(), new NumberStringConverter());
		verticalBlurIterationsText.textProperty().bindBidirectional(verticalBlurIterationsSlider.valueProperty(), new NumberStringConverter());
		thresholdLevelText.textProperty().bindBidirectional(thresholdLevelSlider.valueProperty(), new NumberStringConverter());
	}	
	private void bindSlidersToSingleton() {
		standardBlurNeighborhoodSlider.valueProperty().bindBidirectional(WordViewData.getStandardBlurNeighborhood());
		standardBlurIterationsSlider.valueProperty().bindBidirectional(WordViewData.getStandardBlurIterations());
		verticalBlurNeighborhoodSlider.valueProperty().bindBidirectional(WordViewData.getVerticalBlurNeighborhood());
		verticalBlurIterationsSlider.valueProperty().bindBidirectional(WordViewData.getVerticalBlurIterations());
		thresholdLevelSlider.valueProperty().bindBidirectional(WordViewData.getThresholdLevel());
	}
	
	private void setToggleButtonValsToSingleton() {
		overlapTextButton.selectedProperty().bindBidirectional(LineViewData.getShowOriginalText());
		showLineSplitsButton.selectedProperty().bindBidirectional(LineViewData.getShowLineSplits());
		showThresholdButton.selectedProperty().bindBidirectional(LineViewData.getShowThresholdImage());
	}
	
	private void initImagesToScene() {
		List<BufferedImage> lineImages = LineViewData.getParagraphAnalyzer().getLineSubImages();
		WordViewData.getUntouchedImages().addAll(lineImages);
		
		List<ImageView> overImageViews = WordViewData.getOverImageViews();
		List<ImageView> underImageViews = WordViewData.getUnderImageViews();
		
		for(BufferedImage line : lineImages) {
			Image overImage = SwingFXUtils.toFXImage(line, null);
			
			
			ImageView underImageView = new ImageView(overImage);
			underImageView.fitWidthProperty().bind(scrollPane.widthProperty());
			underImageView.preserveRatioProperty().set(true);
			
			ImageView overImageView = new ImageView(overImage);
			overImageView.fitWidthProperty().bind(scrollPane.widthProperty());
			overImageView.preserveRatioProperty().set(true);
			
			underImageViews.add(underImageView);
			overImageViews.add(overImageView);
			
			underImageView.setBlendMode(BlendMode.MULTIPLY);
			Group images = new Group(overImageView, underImageView);
			
			vbox.getChildren().add(images);
			vbox.getChildren().add(new Separator());
		}
	}
	
	@FXML
	private void handleDisplaySettings() {
		List<ImageView> overImageViews = WordViewData.getOverImageViews();
		List<ImageView> underImageViews = WordViewData.getUnderImageViews();
		List<BufferedImage> untouchedImages = WordViewData.getUntouchedImages();
		
		for(int i = 0; i < overImageViews.size(); i++) {
			ImageView underImageView = underImageViews.get(i);
			ImageView overImageView = overImageViews.get(i);
			BufferedImage untouchedImage = untouchedImages.get(i);
			handleDisplayOneLineImage(underImageView, overImageView, untouchedImage);
		}
	}
	

	private void handleDisplayOneLineImage(ImageView underImageView, ImageView overImageView, BufferedImage untouchedImage) {
		int overlapMode = 0;
		BufferedImage underImage = getBlurredCopyOf(untouchedImage);
		BufferedImage overImage = ImageUtils.copyImage(untouchedImage);
		
		boolean overlapText = WordViewData.getShowOriginalText().get();
		boolean showLineSplits = WordViewData.getShowLineSplits().get();
		boolean showThreshold = WordViewData.getShowThresholdImage().get();

		if(overlapText) {
			if(showThreshold) {
				if(showLineSplits) {
					List<Integer> wordSplits = getWordSplits(underImage, overImage);
					ImageUtils.createVerticalRedLinesAt(overImage, wordSplits);
				}
				thresholdImage(underImage);
				overlapMode = WordSceneController.THRESHOLDOVERLAY;
			}
			if(!showThreshold) {
				if(showLineSplits) {
					List<Integer> wordSplits = getWordSplits(underImage, overImage);
					ImageUtils.createVerticalRedLinesAt(overImage, wordSplits);

				}
				overlapMode = WordSceneController.BLUROVERLAY;
			}
		}
		else if(!overlapText) {
			if(showThreshold) {
				if(showLineSplits) {
					List<Integer> wordSplits = getWordSplits(underImage, overImage);
					ImageUtils.createVerticalRedLinesAt(underImage, wordSplits);
				}
				thresholdImage(underImage);
				overlapMode = WordSceneController.NONE;
			}
			else if(!showThreshold) {
				if(showLineSplits) {
					List<Integer> wordSplits = getWordSplits(underImage, overImage);
					ImageUtils.createVerticalRedLinesAt(underImage, wordSplits);
				}
				overlapMode = WordSceneController.NONE;
			}
		}
		
		Image underfx = SwingFXUtils.toFXImage(underImage, null);
		Image overfx = SwingFXUtils.toFXImage(overImage, null);
		underImageView.setImage(underfx);
		overImageView.setImage(overfx);
		
		setOverlapMode(overlapMode, underImageView, overImageView);
	}
	
	private BufferedImage getBlurredCopyOf(BufferedImage untouchedImage) {
		BufferedImage imageToModify = ImageUtils.copyImage(untouchedImage);
		
		int standardNeighborhood = WordViewData.getStandardBlurNeighborhood().intValue();
		int standardIterations = WordViewData.getStandardBlurIterations().intValue();
		
		int verticalNeighborhood = WordViewData.getVerticalBlurNeighborhood().intValue();
		int verticalIterations = WordViewData.getVerticalBlurNeighborhood().intValue();
		
		ImageUtils.blurImageFast(imageToModify, standardNeighborhood, standardIterations);
		ImageUtils.blurImageVertical(imageToModify, verticalNeighborhood, verticalIterations);
		
		return imageToModify;
	}
	
	private void thresholdImage(BufferedImage blurredImage) {
		double thresholdLevel = WordViewData.getThresholdLevel().get();
		ImageUtils.threshold(blurredImage, thresholdLevel);
	}
	
	private List<Integer> getWordSplits(BufferedImage blurredImage, BufferedImage originalImage) {
		lineAnalyzer.setUntouchedImage(originalImage);
		lineAnalyzer.setBlurredImage(blurredImage);
		
		List<Integer> wordSplits = lineAnalyzer.getWordSplits();
		return wordSplits;
	}
	
	
	public void setOverlapMode(int mode, ImageView underView, ImageView overView) {
		if(mode == WordSceneController.BLUROVERLAY) {
			underView.setBlendMode(BlendMode.MULTIPLY);
		} else if (mode == WordSceneController.THRESHOLDOVERLAY) {
			underView.setBlendMode(BlendMode.EXCLUSION);
		} else if (mode == WordSceneController.NONE) {
			underView.setBlendMode(BlendMode.ADD);
//			overView.setImage(null);
		}
	}
	
	
	public void setMainApp(Main main) {
		this.mainApp = main;
	}
}
