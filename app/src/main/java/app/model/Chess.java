package app.model;

import app.*;
import app.vals.*;
import org.checkerframework.checker.units.qual.C;

public class Chess {

    private static Chess model;

    private Board board;
    private Player turn = Player.WHITE;
    private Player winner = Player.NONE;
    private Observer observer;

    public Chess() {
        // A chess is made up of a Board which contains the Pieces
        setBoard(new Board(8, 8));
    }

    public static Chess getModelInstance() {
        if (model == null)
            model = new Chess();
        return model;
    }

    /* Main Play method and various helper methods */
    public void play() {
        observer.play();
    }

    public void selectSquares(Cell currentCell) { // TODO: write collision logic in all of these
        var piece = currentCell.getPiece();
        var boardState = getBoard();
        var coord = currentCell.getCoord();

        var coordRow = coord.getRow();
        var coordCol = coord.getCol();
        var boardRows = boardState.getRows();
        var boardCols = boardState.getCols();
        switch (piece) { // highlight squares according to each type of piece
            case KING -> {
                // TODO: write logic for where the king can move to
            }
            case QUEEN -> {
                // TODO: write logic for where the queen can move to
            }
            case ROOK -> {
                // TODO: write logic for where the rook can move to
            }
            case BISHOP -> {
                int top = coordRow + 1;
                int bottom = boardRows - coordRow;
                int left = coordCol + 1;
                int right = boardCols - coordCol;

                if (right <= bottom) { // highlights the NW diagonal
                    for (int i = 1; i < right; i++)
                        boardState.getCell(coordRow + i, coordCol + i).setColor(Color.BLUE);
                } else {
                    for (int i = 1; i < bottom; i++)
                        boardState.getCell(coordRow + i, coordCol + i).setColor(Color.BLUE);
                }
                if (left <= top) { // highlights the SE diagonal
                    for (int i = 1; i < left; i++)
                        boardState.getCell(coordRow - i, coordCol - i).setColor(Color.BLUE);
                } else {
                    for (int i = 1; i < top; i++)
                        boardState.getCell(coordRow - i, coordCol - i).setColor(Color.BLUE);
                }
                if (right <= top) { // highlights the NE diagonal
                    for (int i = 1; i < right; i++)
                        boardState.getCell(coordRow - i, coordCol + i).setColor(Color.BLUE);
                } else {
                    for (int i = 1; i < top; i++)
                        boardState.getCell(coordRow - i, coordCol + i).setColor(Color.BLUE);
                }
                if (left <= bottom) { // highlights the SW diagonal
                    for (int i = 1; i < left; i++)
                        boardState.getCell(coordRow + i, coordCol - i).setColor(Color.BLUE);
                } else {
                    for (int i = 1; i < bottom; i++)
                        boardState.getCell(coordRow + i, coordCol - i).setColor(Color.BLUE);
                }
            }
            case KNIGHT -> {
                if (coordRow > 0 && coordCol > 1) // up 1 left 2
                    boardState.getCell(coordRow - 1, coordCol - 2).setColor(Color.BLUE);
                if (coordRow > 0 && coordCol < boardCols - 1) // up 1 right 2
                    boardState.getCell(coordRow - 1, coordCol + 2).setColor(Color.BLUE);
                if (coordRow < boardRows - 2 && coordCol > 1) // down 1 left 2
                    boardState.getCell(coordRow + 1, coordCol - 2).setColor(Color.BLUE);
                if (coordRow < boardRows - 2 && coordCol < boardCols - 3) // down 1 right 2
                    boardState.getCell(coordRow + 1, coordCol + 2).setColor(Color.BLUE);
                if (coordRow < boardRows - 2 && coordCol > 0) // down 2 left 1
                    boardState.getCell(coordRow + 2, coordCol - 1).setColor(Color.BLUE);
                if (coordRow < boardRows - 2 && coordCol < boardCols - 1) // down 2 right 1
                    boardState.getCell(coordRow + 2, coordCol + 1).setColor(Color.BLUE);
                if (coordRow > 1 && coordCol > 0) // up 2 left 1
                    boardState.getCell(coordRow - 2, coordCol - 1).setColor(Color.BLUE);
                if (coordRow > 1 && coordCol < boardCols - 1) // up 2 right 1
                    boardState.getCell(coordRow - 2, coordCol + 1).setColor(Color.BLUE);
            }
            case PAWN -> {
                if (coordRow < boardRows && coordRow > 0)
                    boardState.getCell(coordRow - 1, coordCol).setColor(Color.BLUE);
                if (piece.getFirstMove() && coordRow > 1)
                    boardState.getCell(coordRow - 2, coordCol).setColor(Color.BLUE);
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

        // TODO: write code to change the next button action to select a move and check if they can
        doMoveAction();

        // Look for a win, if no win, observe to update view.
        setWinner(tryWin()); // TODO: maybe move win check to end of tryMove

        if (getWinner() == Player.BLACK)
            observer.win(Player.BLACK);
        else if (getWinner() == Player.WHITE)
            observer.win(Player.WHITE);
        else {
            changeTurn();
            // currentCellObserver.deselectPiece(); // TODO: put this after a move is finished and before the win is checked
        }
    }

    public void doMoveAction() { // set all onActions to look for another click to parse as a move
        for (Cell[] cellArray : board.getGrid()) {
            for (Cell cell : cellArray) {
                cell.getCellObserver().setOnAction(e -> {
                    var model = Chess.getModelInstance();
                    model.parseMove(cell.getCoord());
                });
            }
        }
    }

    public void parseMove(Coordinate coord) { // parse the move at the coordinate
        board.getCell(coord).getCellObserver().deselectPiece(); // Just a test line to see if onAction worked
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
