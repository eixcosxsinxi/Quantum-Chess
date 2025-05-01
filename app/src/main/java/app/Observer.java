package app;

import app.vals. *;
import app.model.*;

public interface Observer {
    void play();
    void win(Player winner);
    void addSuperposition(Cell currentCell);
    void removeSuperposition();
}
