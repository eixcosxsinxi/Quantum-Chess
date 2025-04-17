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
        setPiece(Piece.NONE, Color.NONE);
    }

    public void removePiece() {
        this.piece = Piece.NONE;
        this.color = Color.NONE;
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
    public void setPiece(Piece piece, Color color) {
        this.piece = piece;
        piece.setColor(color);
        if (getCellObserver() != null)
            getCellObserver().setPiece(piece, color);
    }
    public void setColor(Color color) {
        this.color = color;
        getCellObserver().setColor(color);
    }
    public void setCoord(Coordinate coord) {
        this.coord = coord;
    }
}
