package app.view;

import app.*;
import app.vals.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class CellButton extends Button implements CellObserver {

    Cell model;

    private StackPane stackPane; // To give multiple images to the button

    public CellButton() {
        model = new Cell();
        setStackPane(new StackPane());
        setMinSize(30.0, 30.0);
        setMaxSize(30.0, 30.0);
        setGraphic(getStackPane());
    }

    /* Getters */
    public StackPane getStackPane() {
        return this.stackPane;
    }

    /* Setters */
    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
        getStackPane().setMinSize(30.0, 30.0);
        getStackPane().setMaxSize(30.0, 30.0);
    }

    /* CellObserver implementations */
    @Override
    public void selectPiece() {
        // highlight the piece
        var node = getStackPane().getChildren().get(1);
        if (!node.getStyleClass().contains("current"))
            node.getStyleClass().add("current");
    }
    @Override
    public void deselectPiece() {
        // unhighlight the piece
        var node = getStackPane().getChildren().get(1);
        if (node.getStyleClass().contains("current"))
            node.getStyleClass().remove("current");
    }
    @Override
    public void setPiece(Piece piece, Color color) {
        // set the piece

        // These five lines grab the name of the png file to grab
        var pieceString = piece.toString();
        pieceString.toLowerCase();
        var firstLetter = pieceString.substring(0,1);
        var rest = pieceString.substring(1, pieceString.length());
        var correctedPieceString = firstLetter.toUpperCase() + rest;

        ImageView imageView;

        if (color == Color.BLACK) {
            imageView = new ImageView(new Image("/assets/" + correctedPieceString + "B.png"));
            getStackPane().getChildren().add(1, imageView);
        } else if (color == Color.WHITE) {
            imageView = new ImageView(new Image("/assets/" + correctedPieceString + "W.png"));
            getStackPane().getChildren().add(1, imageView);
        }
    }
    @Override
    public void setColor(Color color) {
        // set the initial checkered colors
        var images = getStackPane().getChildren();
        switch (color) {
            case WHITE -> {
                var imageView = new ImageView(new Image("/assets/WhiteSquare.png"));
                if (images.isEmpty())
                    images.addFirst(imageView);
                else
                    images.set(0, imageView);
            }
            case BLUE -> {
                var imageView = new ImageView(new Image("/assets/BlueSquare.png"));
                if (images.isEmpty())
                    images.addFirst(imageView);
                else
                    images.set(0, imageView);
            }
            case BLACK -> {
                var imageView = new ImageView(new Image("/assets/BlackSquare.png"));
                if (images.isEmpty())
                    images.addFirst(imageView);
                else
                    images.set(0, imageView);
            }
        }
    }
    @Override
    public void removePiece() {
        var images = getStackPane().getChildren();
        images.remove(1);
    }
}
