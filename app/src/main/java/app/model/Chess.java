package app.model;

import app.*;
import app.vals.*;

public class Chess {

    private Board board;
    private Player turn = Player.WHITE;
    private Player winner = Player.NONE;
    private Observer observer;

    public Chess() {
        // A chess is made up of a Board which contains the Pieces
        setBoard(new Board(8, 8));
    }

    /* Main Play method and various helper methods */
    public void play() {
        observer.play();
    }

    public void selectSquares(Cell currentCell) {
        var piece = currentCell.getPiece();
        var boardState = getBoard();
        var coord = currentCell.getCoord();

        var coordRow = coord.getRow();
        var coordCol = coord.getCol();
        var boardRows = boardState.getRows();
        var coordCols = boardState.getCols();
        switch (piece) {
            case KING -> {}
            case QUEEN -> {}
            case ROOK -> {}
            case BISHOP -> {}
            case KNIGHT -> {}
            case PAWN -> {
                if (coordRow < boardRows && coordRow > 0)
                    boardState.getCell(coordRow - 1, coordCol).setColor(Color.BLUE);
            }
            case NONE -> {}
            default -> {}
        }
    }

    public void tryTurn(Coordinate coord) {

        var currentCell = getBoard().getCell(coord);
        var currentCellObserver = currentCell.getCellObserver();
        currentCellObserver.selectPiece();

        selectSquares(currentCell);

        // Look for a win, if no win, observe to update view.
        setWinner(tryWin());

        if (getWinner() == Player.BLACK)
            observer.win(Player.BLACK);
        else if (getWinner() == Player.WHITE)
            observer.win(Player.WHITE);
        else {
            currentCellObserver.deselectPiece();
        }
    }

    public void changeTurn() {
        if (turn == Player.WHITE)
            setTurn(Player.BLACK);
        else
            setTurn(Player.WHITE);
    }

    public Player tryWin() {
        return Player.NONE;
    }

    /* Getters */
    public Board getBoard() {
        return this.board;
    }
    public Player getTurn() {
        return this.turn;
    }
    public Observer getObserver() {
        return this.observer;
    }
    public Player getWinner() {
        return this.winner;
    }

    /* Setters */
    public void setBoard(Board board) {
        this.board = board;
    }
    public void setTurn(Player turn) {
        this.turn = turn;
    }
    public void setObserver(Observer observer) {
        this.observer = observer;
    }
    public void setWinner(Player winner) {
        this.winner = winner;
    }
}
