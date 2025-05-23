package app.view;

import app.*;
import app.vals.*;
import app.model.*;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.*;

public class MainWindow implements Observer {
    Stage titleStage;
    Stage gameStage;

    Chess model;

    @FXML HBox MainHBox;
    @FXML HBox letters;
    @FXML VBox numbers;
    @FXML GridPane chessBoard;

    // initialize the main window and close the title window
    public void initialize(Stage titleStage, Stage gameStage, boolean cheat, int difficulty) {
        this.titleStage = titleStage;
        titleStage.hide();
        this.gameStage = gameStage;

        // media plays here
        // TODO: make media work
        //playMedia();

        // set numbers and letters
        setNumbersandLetters();

        // Start Game
        model = Chess.getModelInstance();
        model.setObserver(this);
        model.play();
    }

    /* Helper Methods */
    public void setNumbersandLetters() {
        letters.getChildren().add(new ImageView(new Image("assets/letterA.png")));
        letters.getChildren().add(new ImageView(new Image("assets/letterB.png")));
        letters.getChildren().add(new ImageView(new Image("assets/letterC.png")));
        letters.getChildren().add(new ImageView(new Image("assets/letterD.png")));
        letters.getChildren().add(new ImageView(new Image("assets/letterE.png")));
        letters.getChildren().add(new ImageView(new Image("assets/letterF.png")));
        letters.getChildren().add(new ImageView(new Image("assets/letterG.png")));
        letters.getChildren().add(new ImageView(new Image("assets/letterH.png")));

        numbers.getChildren().add(new ImageView(new Image("assets/number8.png")));
        numbers.getChildren().add(new ImageView(new Image("assets/number7.png")));
        numbers.getChildren().add(new ImageView(new Image("assets/number6.png")));
        numbers.getChildren().add(new ImageView(new Image("assets/number5.png")));
        numbers.getChildren().add(new ImageView(new Image("assets/number4.png")));
        numbers.getChildren().add(new ImageView(new Image("assets/number3.png")));
        numbers.getChildren().add(new ImageView(new Image("assets/number2.png")));
        numbers.getChildren().add(new ImageView(new Image("assets/number1.png")));
    }

    public Board setChessBoard() {
        var board = model.getBoard(); // first get the model's board

        int rows = board.getRows();
        int cols = board.getCols();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                var coord = new Coordinate(row, col); // give each Cell a Coordinate

                var button = new CellButton(); // give each cell a CellButton
                var cell = board.getCell(coord); // get the cell at created coordinate

                cell.setCellObserver(button); // set the CellObserver for each cell

                button.setUserData(coord);

                chessBoard.add(button, col, row); // add the button to the GridPane
            }
        }
        return board; // so the play method can use the already created board
    }

    public void playMedia() { // mostly code I asked AI to help with. I used ClaudeAI to help figure out why it doesn't loop right
        var mediaPlayer = new MediaPlayer(new Media(getClass().getResource("/assets/backgroundmusic.mp3").toString()));
        mediaPlayer.setVolume(0.5);

        // Don't use setStopTime for looping
        final double loopDuration = 93.0; // seconds

        // Create a listener to monitor the current playback position
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.toSeconds() >= loopDuration) {
                mediaPlayer.seek(Duration.ZERO);
            }
        });

        // Add error handling
        mediaPlayer.setOnError(() -> {
            System.out.println("Media error: " + mediaPlayer.getError().toString());
        });

        // Add status change listener to monitor playback state
        mediaPlayer.statusProperty().addListener((observable, oldStatus, newStatus) -> {
            System.out.println("MediaPlayer status changed from " + oldStatus + " to " + newStatus);
            if (newStatus == MediaPlayer.Status.STOPPED || newStatus == MediaPlayer.Status.PAUSED) {
                mediaPlayer.play();
            }
        });

        // Start playback
        mediaPlayer.play();
    }

    /* Observer implementations */
    @Override
    public void play() {
        var board = setChessBoard();
        board.setColors();
        board.setDefaultBoard();
    }

    @Override
    public void addSuperposition(Cell currentCell) {
        var superpositionButton = new Button();
        superpositionButton.setText("superposition");
        superpositionButton.setId("superposition");
        superpositionButton.setOnAction(e -> {
            model.trySuperposition(currentCell); // when superposition button is clicked, set boolean variable to true
        });
        MainHBox.getChildren().add(superpositionButton);
    }

    @Override
    public void removeSuperposition() {
        if (MainHBox.getChildren().getLast().getId() != null)
            MainHBox.getChildren().removeLast();
    }

    @Override
    public void win(Player winner) {
        model.deselectPieces();
        displayAlert(winner.toString() + " won!");
    }

    private void displayAlert(String text) {
        var alert = new Alert(Alert.AlertType.INFORMATION, text);
        alert.show();
    }
}