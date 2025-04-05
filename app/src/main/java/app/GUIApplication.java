package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIApplication extends Application{

    @Override
    public void start(Stage stage) throws Exception {

        var titleResource = getClass().getResource("TitleWindow.fxml");
        var titleLoader = new FXMLLoader(titleResource);
        var titleScene = new Scene(titleLoader.load());

        stage.setScene(titleScene);
        stage.setTitle("Title Screen"); // Title of main window
        stage.show();

        TitleWindow titleWindow = titleLoader.getController();
        titleWindow.initialize(stage);
    }

}