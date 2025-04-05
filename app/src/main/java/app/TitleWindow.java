package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class TitleWindow {

    private Stage titleStage;

    @FXML CheckBox cheatMode;

    @FXML Button startButton;

    @FXML Slider difficultySlider;

    public void initialize(Stage stage) {
        this.titleStage = stage;
    }

    @FXML
    void onStartClicked(ActionEvent event) throws Exception {
        var mainResource = getClass().getResource("MainWindow.fxml");
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
}