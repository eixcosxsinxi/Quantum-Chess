package app.model;

import app.vals.*;
import app.view.*;
import javafx.scene.control.*;

public class Cell {
    private CellButton cellObserver;
    private Piece piece;
    private Color color;
    private Coordinate coord;

    public Cell(Coordinate coord) {
        // A Cell contains a Color, Piece, Coordinate, and CellObserver
        setCoord(coord);
        setPiece("NONE", Color.NONE, true);
    }

    public void removePiece() {
        this.piece.setType("NONE");
        this.piece.setColor(Color.NONE);
        this.piece.setFirstMove(true);
        this.piece.setProbability(1.0);
        getCellObserver().removePiece();
    }

    /* Getters */
    public CellButton getCellObserver() {
        return this.cellObserver;
    }
    public Piece getPiece() {
        return this.piece;
    }
    public Color getColor() {
        return this.color;
    }
    public Coordinate getCoord() {
        return this.coord;
    }

    /* Setters */
    public void setCellObserver(CellButton cellObserver) {
        this.cellObserver = cellObserver;
    }
    public void setPiece(String type, Color color, boolean firstMove) {
        piece = new Piece(type, color, firstMove);
        if (getCellObserver() != null)
            getCellObserver().setPiece(type, color);
    }
    public void setPiece(String type, Color color, boolean firstmove, double probability) {
        piece = new Piece(type, color, firstmove, probability);
        if (getCellObserver() != null)
            getCellObserver().setPiece(type, color, probability);
    }
    public void setColor(Color color) {
        this.color = color;
        getCellObserver().setColor(color);
    }
    public void setCoord(Coordinate coord) {
        this.coord = coord;
    }
}
