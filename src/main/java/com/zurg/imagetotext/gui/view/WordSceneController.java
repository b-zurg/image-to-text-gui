package com.zurg.imagetotext.gui.view;

import java.awt.image.BufferedImage;
import java.util.List;

import utils.ImageUtils;

import com.zurg.imagetotext.gui.Main;
import com.zurg.imagetotext.model.LineViewData;
import com.zurg.imagetotext.model.WordViewData;

import document.analysis.LineComponentAnalyzer;
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

	
	@FXML
	private void initialize() {		
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
		overlapTextButton.selectedProperty().bindBidirectional(WordViewData.getShowOriginalText());
		showLineSplitsButton.selectedProperty().bindBidirectional(WordViewData.getShowLineSplits());
		showThresholdButton.selectedProperty().bindBidirectional(WordViewData.getShowThresholdImage());
	}
	
	private void initImagesToScene() {
		List<BufferedImage> lineImages = LineViewData.getParagraphAnalyzer().getLineSubImages();
		WordViewData.setUntouchedImages(lineImages);
		
		List<Group> imageGroups = WordViewData.getImageGroups();
		
		for(BufferedImage line : lineImages) {
			Image lineImage = SwingFXUtils.toFXImage(line, null);
			
			ImageView imageView = new ImageView(lineImage);
//			imageView.fitWidthProperty().bind(scrollPane.widthProperty());
			imageView.preserveRatioProperty().set(true);
			imageView.setBlendMode(BlendMode.MULTIPLY);
			
			Group imageGroup = new Group(imageView);
			imageGroups.add(imageGroup);
			
			vbox.getChildren().add(imageGroup);
			vbox.getChildren().add(new Separator());
		}
	}
	
	@FXML private void handleApplyButton() {
		List<BufferedImage> untouchedImages = WordViewData.getUntouchedImages();
		List<BufferedImage> blurredImages = WordViewData.getBlurredImages();
		blurredImages.clear();
		
		for(BufferedImage image : untouchedImages) {
			BufferedImage imageToModify = ImageUtils.copyImage(image);
			blurImage(imageToModify);
			blurredImages.add(imageToModify);
		}
		handleDisplaySettings();
	}
	
	
	private void blurImage(BufferedImage image) {
		
		int standardNeighborhood = WordViewData.getStandardBlurNeighborhood().intValue();
		int standardIterations = WordViewData.getStandardBlurIterations().intValue();
		
		int verticalNeighborhood = WordViewData.getVerticalBlurNeighborhood().intValue();
		int verticalIterations = WordViewData.getVerticalBlurIterations().intValue();
		
		ImageUtils.blurImageFast(image, standardNeighborhood, standardIterations);
		ImageUtils.blurImageVertical(image, verticalNeighborhood, verticalIterations);
		
	}
	
	@FXML
	private void handleDisplaySettings() {
		List<Group> imageGroups = WordViewData.getImageGroups();
		List<BufferedImage> untouchedImages = WordViewData.getUntouchedImages();
		List<BufferedImage> blurredImages = WordViewData.getBlurredImages();
		
		for(int i = 0; i < imageGroups.size(); i++) {
			BufferedImage untouchedImage = untouchedImages.get(i);
			BufferedImage blurredImage = blurredImages.get(i);
			Group imageGroup = imageGroups.get(i);
			handleDisplayOneLineImage(imageGroup, untouchedImage, blurredImage);
		}
	}
	

	private void handleDisplayOneLineImage(Group imageGroup, BufferedImage untouchedImage, BufferedImage blurredImage) {
		int overlapMode = 0;
		BufferedImage underImage = ImageUtils.copyImage(blurredImage);
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
		
		setOverlapMode(overlapMode, underImage, overImage, imageGroup);
	}

	private void thresholdImage(BufferedImage blurredImage) {
		double thresholdLevel = WordViewData.getThresholdLevel().get();
		ImageUtils.threshold(blurredImage, thresholdLevel);
	}
	
	private List<Integer> getWordSplits(BufferedImage blurredImage, BufferedImage originalImage) {
		double thresholdLevel = WordViewData.getThresholdLevel().get();
		LineComponentAnalyzer lineAnalyzer = WordViewData.getLineAnalyzer();
		lineAnalyzer.setUntouchedImage(originalImage);
		lineAnalyzer.setBlurredImage(blurredImage);
		lineAnalyzer.setThreshold(thresholdLevel);
		
		List<Integer> wordSplits = lineAnalyzer.getWordSplits();
		return wordSplits;
	}
	
	public void setOverlapMode(int mode, BufferedImage underImage, BufferedImage overImage, Group imageGroup) {
		Image underImageFX = SwingFXUtils.toFXImage(underImage, null);
		Image overImageFX = SwingFXUtils.toFXImage(overImage, null);
		ImageView underImageView = new ImageView(underImageFX);
		ImageView overImageView = new ImageView(overImageFX);
		imageGroup.getChildren().clear();
		
		if(mode == WordSceneController.BLUROVERLAY) {
			underImageView.setBlendMode(BlendMode.MULTIPLY);
			imageGroup.getChildren().add(overImageView);
			imageGroup.getChildren().add(underImageView);
		}
		else if (mode == WordSceneController.THRESHOLDOVERLAY) {
			underImageView.setBlendMode(BlendMode.EXCLUSION);
			imageGroup.getChildren().add(overImageView);
			imageGroup.getChildren().add(underImageView);
		}
		else if (mode == WordSceneController.NONE) {
			imageGroup.getChildren().add(underImageView);
		}
	}
	
	public void setMainApp(Main main) {
		this.mainApp = main;
	}
}
