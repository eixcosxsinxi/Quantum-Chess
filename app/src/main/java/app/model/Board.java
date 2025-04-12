package app.model;

import app.vals.*;

public class Board {
    private int rows;
    private int cols;

    public Board(int rows, int cols) {
        // A Board has row rows and col cols
        setRows(rows);
        setCols(cols);
    }

    /* Getters */
    public int getRows() {
        return this.rows;
    }
    public int getCols() {
        return this.cols;
    }

    /* Setters */
    public void setRows(int rows) {
        this.rows = rows;
    }
    public void setCols(int cols) {
        this.cols = cols;
    }
}
