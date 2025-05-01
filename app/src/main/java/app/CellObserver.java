package app;

import app.vals.*;

public interface CellObserver {
    void selectPiece();
    void deselectPiece();
    void setPiece(String type, Color color);
    void setPiece(String type, Color color, double probability);
    void setColor(Color color);
    void removePiece();
}
