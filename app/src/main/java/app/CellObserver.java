package app;

import app.vals.*;

public interface CellObserver {
    void selectPiece();
    void deselectPiece();
    void setPiece(String type, Color color);
    void setColor(Color color);
    void removePiece();
}
