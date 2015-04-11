package com.zurg.imagetotext.gui.view;

import java.awt.image.BufferedImage;

import utils.CoordinateUtils;
import utils.ImageUtils;
import utils.Point;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import com.zurg.imagetotext.gui.Main;
import com.zurg.imagetotext.model.ModificationViewData;

public class ModificationSceneController {
	private Main mainApp;
	private ModificationViewData modData;

	@FXML private ScrollPane scrollPane;
	@FXML private AnchorPane anchorPane;
	@FXML private Button addBoxButton, clearButton;

	private Point startingPos, curTopLeft, curBottomRight;


	private Canvas canvas = new Canvas();
	private ImageView imageView = new ImageView();


	@FXML 
	private void initialize() {
		bindImageView();
		bindCanvasToImage();
		addCanvasHandlers();
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
	
	private Point getImageScaleAdjustedPoint(Point p) {
		double xmultiple = imageView.imageProperty().get().getWidth() /  imageView.layoutBoundsProperty().get().getWidth();
		double ymultiple = imageView.imageProperty().get().getHeight() / imageView.layoutBoundsProperty().get().getHeight();
		int scaledx = (int) ((double)p.X() * xmultiple);
		int scaledy = (int) ((double)p.Y() * ymultiple);

		Point adjustedPoint = new Point(scaledx, scaledy);
		return adjustedPoint;
	}
	private Point getBoxScaleAdjustedPoint(Point p ) {
		double xmultiple = 1 / (imageView.imageProperty().get().getWidth() /  imageView.layoutBoundsProperty().get().getWidth());
		double ymultiple = 1 / (imageView.imageProperty().get().getHeight() / imageView.layoutBoundsProperty().get().getHeight());
		int scaledx = (int) ((double)p.X() * xmultiple);
		int scaledy = (int) ((double)p.Y() * ymultiple);

		Point adjustedPoint = new Point(scaledx, scaledy);
		return adjustedPoint;		
	}


	private void bindImageView() {		
		imageView.fitWidthProperty().bind(scrollPane.widthProperty());
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
		GraphicsContext gg = canvas.getGraphicsContext2D();
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
	
	

	@Deprecated
	public void setImage(BufferedImage image) {
		imageView.imageProperty().bindBidirectional(modData.getImageProperty());
		this.modData.setImageProperty(image);
		createCanvasAndImageView();
	}

	public void setData(ModificationViewData data) {
		this.modData = data;
		imageView.imageProperty().bindBidirectional(data.getImageProperty());
		createCanvasAndImageView();
		drawPreviousBoxes();
	}
	private void createCanvasAndImageView() {
		Group gp = new Group(imageView, canvas);
		scrollPane.setContent(gp);
	}
	
	public void setMainApp(Main mainapp) {
		this.mainApp = mainapp;
	}
}
