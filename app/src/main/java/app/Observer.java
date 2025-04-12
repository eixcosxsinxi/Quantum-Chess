package app;

import app.vals.Piece;
import app.vals.Player;

public interface Observer {
    void play();
    void win(Player winner);
}
