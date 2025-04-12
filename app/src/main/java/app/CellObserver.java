package app;

import app.vals.*;

public interface CellObserver {
    void selectPiece();
    void deselectPiece();
    void setPiece(Piece piece);
    void setColor(Color color);
}
