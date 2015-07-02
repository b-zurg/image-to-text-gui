package com.zurg.imagetotext.gui.view;

import java.awt.image.BufferedImage;
import java.util.List;

import utils.CoordinateUtils;
import utils.ImageUtils;
import utils.Point;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import com.google.common.collect.Lists;
import com.zurg.imagetotext.gui.Main;
import com.zurg.imagetotext.gui.components.*;
import com.zurg.imagetotext.model.ModificationViewData;

public class ModificationSceneController {
	private Main mainApp;
	private ModificationViewData modData;

	@FXML private ScrollPane scrollPane;
	@FXML private AnchorPane anchorPane;
	@FXML private Button addBoxButton, clearButton, resetButton;
	@FXML private Slider rotateSlider;

	private Point startingPos, curTopLeft, curBottomRight;

	private BufferedImage imageToRotate;
	
	private DoubleProperty xTopLeft = new SimpleDoubleProperty(), yTopLeft = new SimpleDoubleProperty();
	private DoubleProperty xTopRight = new SimpleDoubleProperty(), yTopRight = new SimpleDoubleProperty();
	private DoubleProperty xBottomLeft = new SimpleDoubleProperty(), yBottomLeft = new SimpleDoubleProperty();
	private DoubleProperty xBottomRight = new SimpleDoubleProperty(), yBottomRight = new SimpleDoubleProperty();
	
	private Canvas canvas = new Canvas();
	private ImageView imageView = new ImageView();
	private Group scrollContent = new Group();


	@FXML 
	private void initialize() {
		bindImageView();
		setRotateSliderActionListener();
		bindCanvasToImage();
		addCanvasHandlers();
	}
	
	private void setRotateSliderActionListener(){
		rotateSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			modData.setImageProperty(ImageUtils.rotateImage(imageToRotate, newValue.intValue()));
		});
	}

	@FXML private void handleAddSubImage() {
		Point adjustedTopLeft = getImageScaleAdjustedPoint(curTopLeft);
		Point adjustedBottomRight = getImageScaleAdjustedPoint(curBottomRight);
		modData.getTopLeftPoints().add(new Point(adjustedTopLeft));
		modData.getBottomRightPoints().add(new Point(adjustedBottomRight));

		BufferedImage subImage = ImageUtils.getSubImageFrom(modData.getBufferedImage(), adjustedTopLeft, adjustedBottomRight);
		mainApp.addSubImage(subImage);
	}

	@FXML private void handleClearBoxes() {
		mainApp.clearSubImages();
		mainApp.addSubImage(modData.getBufferedImage());
		modData.getBottomRightPoints().clear();
		modData.getTopLeftPoints().clear();
		
		this.clearCanvas();
	}
	
	@FXML private void handleResetStretchButton() {
		modData.setImageProperty(this.imageToRotate);
		this.setAnchorPositionsToImageCorners();
	}
	
	private Point getImageScaleAdjustedPoint(Point p) {
		double xmultiple = imageView.imageProperty().get().getWidth() /  imageView.layoutBoundsProperty().get().getWidth();
		double ymultiple = imageView.imageProperty().get().getHeight() / imageView.layoutBoundsProperty().get().getHeight();
		int scaledx = (int) ((double)p.X() * xmultiple);
		int scaledy = (int) ((double)p.Y() * ymultiple);

		Point adjustedPoint = new Point(scaledx, scaledy);
		return adjustedPoint;
	}
	
	private void bindImageView() {		
		imageView.fitWidthProperty().bind(scrollPane.widthProperty());
		imageView.fitHeightProperty().bind(scrollPane.heightProperty());
		imageView.setPreserveRatio(true);
	}
	private void bindCanvasToImage() {
		resizeCanvasToBounds(imageView.getBoundsInLocal());
		imageView.boundsInLocalProperty().addListener( new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> observableValue, Bounds bounds, Bounds bounds2) {
				resizeCanvasToBounds(bounds2);
				
				clearCanvas();
				drawPreviousBoxes();
			}
		});
	}	
	private void resizeCanvasToBounds(Bounds bounds) {
		canvas.setHeight(bounds.getHeight());
		canvas.setWidth(bounds.getWidth());
	}
	
	
	private void addCanvasHandlers() {
		canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, 
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						startingPos = new Point((int)event.getX() - 2, (int)event.getY() - 2);
					}
		});
		canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						clearCanvas();
						drawPreviousBoxes();
						updateCurrentBox(event);
					}
				});
	}
	
	private void clearCanvas() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
	private void drawPreviousBoxes() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		for(int i = 0; i < modData.getTopLeftPoints().size(); i++) {
			Point topLeft = modData.getTopLeftPoints().get(i);
			Point bottomRight = modData.getBottomRightPoints().get(i);
			Point adjustedTopLeft = getImageScaleAdjustedPoint(topLeft);
			Point adjustedBottomRight = getImageScaleAdjustedPoint(bottomRight);

			int width = CoordinateUtils.getWidthBetweenPoints(adjustedTopLeft, adjustedBottomRight);
			int height = CoordinateUtils.getHeightBetweenPoints(adjustedTopLeft, adjustedBottomRight);

			gc.strokeRect(topLeft.X(), topLeft.Y(), width, height);
		}
	}
	private void updateCurrentBox(MouseEvent e) {
		GraphicsContext gg = canvas.getGraphicsContext2D();
		Point currentPoint = new Point((int) e.getX(), (int) e.getY());
		curTopLeft = CoordinateUtils.getTopLeftPoint(startingPos, currentPoint);

		int width = CoordinateUtils.getWidthBetweenPoints(startingPos, currentPoint);
		int height = CoordinateUtils.getHeightBetweenPoints(startingPos, currentPoint);
		curBottomRight = new Point(curTopLeft.X() + width, curTopLeft.Y() + height);

		gg.strokeRect(curTopLeft.X(), curTopLeft.Y(), width, height);
	}
	
	private void createImageControlAnchors() {
//		setAnchorPositionsToImageCorners();
		
		Anchor topLeft = createControlAnchor(xTopLeft, yTopLeft);
		Anchor topRight = createControlAnchor(xTopRight, yTopRight);
		Anchor bottomLeft = createControlAnchor(xBottomLeft, yBottomLeft);
		Anchor bottomRight = createControlAnchor(xBottomRight, yBottomRight);
		
		addAnchorsToScene(Lists.newArrayList(topLeft, topRight, bottomLeft, bottomRight));
	}
	
	private Anchor createControlAnchor(DoubleProperty xProperty, DoubleProperty yProperty) {
		xProperty.addListener((observableVal, oldVal, newVal) -> {
			stretchImage();
		});
		yProperty.addListener((observableVal, oldVal, newVal) -> {
			stretchImage();
		});
		Anchor a = new Anchor(Color.BLUE, xProperty, yProperty);
		return a;
	}
	
	private void setAnchorPositionsToImageCorners() {
		Bounds bounds = imageView.boundsInLocalProperty().getValue();
		xTopLeft.set(bounds.getMinX()); 		yTopLeft.set(bounds.getMinY());
		xTopRight.set(bounds.getMaxX()); 		yTopRight.set(bounds.getMinY());
		xBottomLeft.set(bounds.getMinX()); 		yBottomLeft.set(bounds.getMaxY());
		xBottomRight.set(bounds.getMaxX()); 	yBottomRight.set(bounds.getMaxY());
	}
	
	private void addAnchorsToScene(List<Anchor> anchors) {
		this.scrollContent.getChildren().addAll(anchors);
	}
	
	private void stretchImage() {
		Point ul = new Point(xTopLeft.intValue(), yTopLeft.intValue());
		Point ur = new Point(xTopRight.intValue(), yTopRight.intValue());
		Point ll = new Point(xBottomLeft.intValue(), yBottomLeft.intValue());
		Point lr = new Point(xBottomRight.intValue(), yBottomRight.intValue());
		BufferedImage skewedImage = ImageUtils.skewImage(ImageUtils.copyImage(imageToRotate), ul, ur, lr, ll);
		modData.setImageProperty(skewedImage);
	}
	
	


	@Deprecated
	public void setImage(BufferedImage image) {
		imageView.imageProperty().bindBidirectional(modData.getImageProperty());
		this.modData.setImageProperty(image);
		createCanvasAndImageView();
	}

	public void setData(ModificationViewData data) {
		this.modData = data;
		imageToRotate = ImageUtils.copyImage(data.getBufferedImage());
		imageView.imageProperty().bindBidirectional(data.getImageProperty());
		rotateSlider.valueProperty().bindBidirectional(data.getRotateValueProperty());
		createCanvasAndImageView();
		drawPreviousBoxes();
		createImageControlAnchors();
		setAnchorPositionsToImageCorners();
	}
	
	private void createCanvasAndImageView() {
		this.scrollContent.getChildren().addAll(imageView, canvas);
		scrollPane.setContent(scrollContent);
	}
	
	public void setMainApp(Main mainapp) {
		this.mainApp = mainapp;
	}
}
