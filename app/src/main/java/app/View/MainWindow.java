package app.view;

import app.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

public class MainWindow implements Observer{
    Stage titleStage;
    Stage gameStage;

    // initialize the main window and close the title window
    public void initialize(Stage titleStage, Stage gameStage, boolean cheat, int difficulty) {
        this.titleStage = titleStage;
        titleStage.hide();
        this.gameStage = gameStage;
    }


}