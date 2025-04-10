package app.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class TitleWindow {
    // Here is the controller for TitleWindow.

    private Stage titleStage; // This is so that MainWindow can hide this screen

    @FXML CheckBox cheatMode;

    @FXML Button startButton;

    @FXML Slider difficultySlider;

    public void initialize(Stage stage) {
        this.titleStage = stage;
    }

    @FXML
    void onHelpClicked(ActionEvent event) {
        displayAlert("The game Quantum Chess starts with the classical game of chess.");
    }

    @FXML
    void onStartClicked(ActionEvent event) throws Exception {
        // on start clicked, load the main window
        var mainResource = getClass().getResource("/app/MainWindow.fxml");
        var mainLoader = new FXMLLoader(mainResource);
        var mainScene = new Scene(mainLoader.load());

        var mainStage = new Stage();

        mainStage.setScene(mainScene);
        mainStage.setTitle("Quantum Chess");
        mainStage.show();

        MainWindow mainWindow = mainLoader.getController();
        boolean cheat = cheatMode.isSelected();
        int difficulty = (int)difficultySlider.getValue();
        mainWindow.initialize(titleStage, mainStage, cheat, difficulty);
    }

    private void displayAlert(String text) {
        var alert = new Alert(Alert.AlertType.INFORMATION, text);
        alert.getDialogPane().getStyleClass().add("helpScreen");
    }
}