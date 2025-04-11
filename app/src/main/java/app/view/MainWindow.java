package app.view;

import app.Observer;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class MainWindow implements Observer{
    Stage titleStage;
    Stage gameStage;

    // initialize the main window and close the title window
    public void initialize(Stage titleStage, Stage gameStage, boolean cheat, int difficulty) {
        this.titleStage = titleStage;
        titleStage.hide();
        this.gameStage = gameStage;

        // media plays here
        var mediaPlayer = new MediaPlayer(new Media(getClass().getResource("/assets/backgroundmusic.mp3").toString()));
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.5);
        mediaPlayer.play(); 
    }


}