package app.model;

import app.vals.*;
import app.view.*;
import javafx.scene.control.*;

public class Cell {
    private CellButton cellObserver;
    private Piece piece;

    public Cell() {
        // A Cell contains a Color, Piece, and CellObserver
    }

    /* Getters */
    public CellButton getCellObserver() {
        return this.cellObserver;
    }
    public Piece getPiece() {
        return this.piece;
    }

    /* Setters */
    public void setCellObserver(CellButton cellObserver) {
        this.cellObserver = cellObserver;
    }
    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
