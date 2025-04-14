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
    public void setPiece(Piece piece) {
        this.piece = piece;
        getCellObserver().setPiece(piece);
    }
    public void setColor(Color color) {
        this.color = color;
        getCellObserver().setColor(color);
    }
    public void setCoord(Coordinate coord) {
        this.coord = coord;
    }
}
