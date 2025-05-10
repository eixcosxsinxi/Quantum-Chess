package app.model;

import app.vals.*;

public class Board {
    private int rows;
    private int cols;
    private Cell[][] grid;

    public Board(int rows, int cols) {
        // A Board has row rows and col cols
        setRows(rows);
        setCols(cols);

        grid = new Cell[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                grid[row][col] = new Cell(new Coordinate(row, col));
            }
        }
    }

    /* Getters */
    public int getRows() {
        return this.rows;
    }
    public int getCols() {
        return this.cols;
    }
    public Cell[][] getGrid() {
        return this.grid;
    }
    public Cell getCell(int row, int col) {
        return getGrid()[row][col];
    }
    public Cell getCell(Coordinate coord) {
        return getGrid()[coord.getRow()][coord.getCol()];
    }

    /* Setters */
    public void setRows(int rows) {
        this.rows = rows;
    }
    public void setCols(int cols) {
        this.cols = cols;
    }
    public void setGrid(Cell[][] grid) {
        this.grid = grid;
    }
    public void setCell(int row, int col, Cell cell) {
        getGrid()[row][col] = cell;
    }
    public void setCell(Coordinate coord, Cell cell) {
        getGrid()[coord.getRow()][coord.getCol()] = cell;
    }
    public void setPiece(int row, int col, String type, Color color, boolean firstMove) {
        getCell(row, col).setPiece(type, color, firstMove);
    }
    public void setPiece(Coordinate coord, String type, Color color, boolean firstMove) {
        getCell(coord).setPiece(type, color, firstMove);
    }
    public void setPiece(Coordinate coord, Piece piece) {
        getCell(coord).setPiece(piece);
    }
    public void setAllPawns() {
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                var cell = grid[row][col];
                cell.setPiece("PAWN", Color.BLACK, true);
            }
        }
    }
    public void setDefaultBoard() {
        /* Black Pawns */
        setPiece(1, 0, "PAWN", Color.BLACK, true);
        setPiece(1, 1, "PAWN", Color.BLACK, true);
        setPiece(1, 2, "PAWN", Color.BLACK, true);
        setPiece(1, 3, "PAWN", Color.BLACK, true);
        setPiece(1, 4, "PAWN", Color.BLACK, true);
        setPiece(1, 5, "PAWN", Color.BLACK, true);
        setPiece(1, 6, "PAWN", Color.BLACK, true);
        setPiece(1, 7, "PAWN", Color.BLACK, true);
        /* Black Back-Rank Pieces */
        setPiece(0, 0, "ROOK", Color.BLACK, true);
        setPiece(0, 1, "KNIGHT", Color.BLACK, true);
        setPiece(0, 2, "BISHOP", Color.BLACK, true);
        setPiece(0, 3, "QUEEN", Color.BLACK, true);
        setPiece(0, 4, "KING", Color.BLACK, true);
        setPiece(0, 5, "BISHOP", Color.BLACK, true);
        setPiece(0, 6, "KNIGHT", Color.BLACK, true);
        setPiece(0, 7, "ROOK", Color.BLACK, true);

        /* White Pawns */
        setPiece(6, 0, "PAWN", Color.WHITE, true);
        setPiece(6, 1, "PAWN", Color.WHITE, true);
        setPiece(6, 2, "PAWN", Color.WHITE, true);
        setPiece(6, 3, "PAWN", Color.WHITE, true);
        setPiece(6, 4, "PAWN", Color.WHITE, true);
        setPiece(6, 5, "PAWN", Color.WHITE, true);
        setPiece(6, 6, "PAWN", Color.WHITE, true);
        setPiece(6, 7, "PAWN", Color.WHITE, true);
        /* White Back-Rank Pieces */
        setPiece(7, 0, "ROOK", Color.WHITE, true);
        setPiece(7, 1, "KNIGHT", Color.WHITE, true);
        setPiece(7, 2, "BISHOP", Color.WHITE, true);
        setPiece(7, 3, "QUEEN", Color.WHITE, true);
        setPiece(7, 4, "KING", Color.WHITE, true);
        setPiece(7, 5, "BISHOP", Color.WHITE, true);
        setPiece(7, 6, "KNIGHT", Color.WHITE, true);
        setPiece(7, 7, "ROOK", Color.WHITE, true);
    }
    public void setColors() {
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                var cell = grid[row][col]; // this variable is used to update each cell

                if (row % 2 == 0) {
                    if (col % 2 == 0)
                        cell.setColor(Color.WHITE);
                    else
                        cell.setColor(Color.BLACK);
                } else {
                    if (col % 2 == 1)
                        cell.setColor(Color.WHITE);
                    else
                        cell.setColor(Color.BLACK);
                }
            }
        }
    }
}
