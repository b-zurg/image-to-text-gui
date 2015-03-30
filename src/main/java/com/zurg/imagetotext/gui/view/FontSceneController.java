package com.zurg.imagetotext.gui.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import com.zurg.imagetotext.gui.Main;

public class FontSceneController {
	private Main mainApp;
	
	@FXML ImageView lineView;
	@FXML TextField enterTextField;
	@FXML Button goButton;
	@FXML TableView fontTable;
	@FXML TextArea finishedTextArea;
	
	public FontSceneController() {
		
	}
	
	
	public void setMainApp(Main main) {
		this.mainApp = main;
	}
}
