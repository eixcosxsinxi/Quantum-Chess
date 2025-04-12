package app.model;

import app.vals.*;

public class Chess {

    private Board board;
    private Turn turn = Turn.WHITE;

    public Chess() {
        // A chess is made up of a Board which contains the Pieces
        setBoard(new Board(8, 8));
    }

    /* Getters */
    public Board getBoard() {
        return this.board;
    }
    public Turn getTurn() {
        return this.turn;
    }

    /* Setters */
    public void setBoard(Board board) {
        this.board = board;
    }
    public void setTurn(Turn turn) {
        this.turn = turn;
    }
}
