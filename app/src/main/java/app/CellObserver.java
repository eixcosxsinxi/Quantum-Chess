package app;

import app.vals.*;

public interface CellObserver {
    void selectPiece();
    void deselectPiece();
    void setPiece(Piece piece, Color color);
    void setColor(Color color);
}
