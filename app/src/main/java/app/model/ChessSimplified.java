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
        if (model == null) {
            model = new Chess();
        }
        return model;
    }

    /* Main Play method and various helper methods */
    public void play() {
        observer.play();
    }

    public boolean inCheck() {
        // TODO: write inCheck.
        /*
        * probably will highlight all squares of all pieces on board and check if king is on blue square.
        * */
        return false; // for now...
    }

    private boolean inRange(int row, int col) {
        int max = getBoard().getRows() - 1;
        return row >= 0 && row <= max && col >= 0 && col < max;
    }

    private void colorCell(int row, int col, Color color) {
        if (inRange(row, col)) {
            Cell cell = getBoard().getCell(row, col);

            Color cellColor = cell.getPiece().getColor();
            if (cellColor.equals(Color.NONE)) {
                cell.setColor(Color.BLUE);
            } else if (!cellColor.equals(color)) {
                cell.setColor(Color.RED);
            }
        }
    }

    public void selectSquares(Cell currentCell) { // TODO: write collision logic in all of these

        /* TODO: write a boardState.matchesColor(row, col, color) does the logic of checking cell color within the method
        * can also write a matchesColor(Cell, color)
         */
        var piece = currentCell.getPiece();
        var color = piece.getColor();
        var boardState = getBoard();
        var coord = currentCell.getCoord();

        var coordRow = coord.getRow();
        var coordCol = coord.getCol();
        var boardRows = boardState.getRows();
        var boardCols = boardState.getCols();

        switch (piece) { // highlight squares according to each type of piece
            case KING -> {
                // TODO: write logic for king in check
                if (!inCheck()) {
                    for (int i = -1; i <= 1; ++i) {
                        for (int j = -1; j <= 1; ++j) {
                            if (i != 0 && j != 0) {
                                colorCell(coordRow + i, coordCol + j, color);
                            }
                        }
                    }
                }
            }
            case QUEEN -> {
                int top = coordRow + 1;
                int bottom = boardRows - coordRow;
                int left = coordCol + 1;
                int right = boardCols - coordCol;

                int end = Math.min(right, bottom);
                for (int i = 1; i < end; i++) { // highlights the NW diagonal
                    if (boardState.getCell(coordRow + i, coordCol + i).getPiece().getColor().equals(Color.NONE)) {
                        boardState.getCell(coordRow + i, coordCol + i).setColor(Color.BLUE);
                    } else {
                        if (!boardState.getCell(coordRow + i, coordCol + i).getPiece().getColor().equals(color)) {
                            boardState.getCell(coordRow + i, coordCol + i).setColor(Color.RED);
                        }
                        break;
                    }
                }
                end = Math.min(left, top);
                for (int i = 1; i < end; i++) { // highlights the NW diagonal
                    boardState.getCell(coordRow - i, coordCol - i).setColor(Color.BLUE);
                }
                end = Math.min(right, top);
                for (int i = 1; i < end; i++) { // highlights the NE diagonal
                    boardState.getCell(coordRow - i, coordCol + i).setColor(Color.BLUE);
                }
                if (left <= bottom) { // highlights the SW diagonal
                    for (int i = 1; i < left; i++) {
                        boardState.getCell(coordRow + i, coordCol - i).setColor(Color.BLUE);
                    }
                } else {
                    for (int i = 1; i < bottom; i++) {
                        boardState.getCell(coordRow + i, coordCol - i).setColor(Color.BLUE);
                    }
                }
                for (int i = 1; i < top; i++) { // highlights the N column
                    boardState.getCell(coordRow - i, coordCol).setColor(Color.BLUE);
                }
                for (int i = 1; i < bottom; i++) { // highlights the S column
                    boardState.getCell(coordRow + i, coordCol).setColor(Color.BLUE);
                }
                for (int i = 1; i < left; i++) { // highlights the W row
                    boardState.getCell(coordRow, coordCol - i).setColor(Color.BLUE);
                }
                for (int i = 1; i < right; i++) { // highlights the E row
                    boardState.getCell(coordRow, coordCol + i).setColor(Color.BLUE);
                }
            }
            case ROOK -> {
                int top = coordRow + 1;
                int bottom = boardRows - coordRow;
                int left = coordCol + 1;
                int right = boardCols - coordCol;

                for (int i = 1; i < top; i++) // highlights the N column
                {
                    boardState.getCell(coordRow - i, coordCol).setColor(Color.BLUE);
                }
                for (int i = 1; i < bottom; i++) // highlights the S column
                {
                    boardState.getCell(coordRow + i, coordCol).setColor(Color.BLUE);
                }
                for (int i = 1; i < left; i++) // highlights the W row
                {
                    boardState.getCell(coordRow, coordCol - i).setColor(Color.BLUE);
                }
                for (int i = 1; i < right; i++) // highlights the E row
                {
                    boardState.getCell(coordRow, coordCol + i).setColor(Color.BLUE);
                }
            }
            case BISHOP -> {
                int top = coordRow + 1;
                int bottom = boardRows - coordRow;
                int left = coordCol + 1;
                int right = boardCols - coordCol;

                if (right <= bottom) { // highlights the NW diagonal
                    for (int i = 1; i < right; i++) {
                        boardState.getCell(coordRow + i, coordCol + i).setColor(Color.BLUE);
                    }
                } else {
                    for (int i = 1; i < bottom; i++) {
                        boardState.getCell(coordRow + i, coordCol + i).setColor(Color.BLUE);
                    }
                }
                if (left <= top) { // highlights the SE diagonal
                    for (int i = 1; i < left; i++) {
                        boardState.getCell(coordRow - i, coordCol - i).setColor(Color.BLUE);
                    }
                } else {
                    for (int i = 1; i < top; i++) {
                        boardState.getCell(coordRow - i, coordCol - i).setColor(Color.BLUE);
                    }
                }
                if (right <= top) { // highlights the NE diagonal
                    for (int i = 1; i < right; i++) {
                        boardState.getCell(coordRow - i, coordCol + i).setColor(Color.BLUE);
                    }
                } else {
                    for (int i = 1; i < top; i++) {
                        boardState.getCell(coordRow - i, coordCol + i).setColor(Color.BLUE);
                    }
                }
                if (left <= bottom) { // highlights the SW diagonal
                    for (int i = 1; i < left; i++) {
                        boardState.getCell(coordRow + i, coordCol - i).setColor(Color.BLUE);
                    }
                } else {
                    for (int i = 1; i < bottom; i++) {
                        boardState.getCell(coordRow + i, coordCol - i).setColor(Color.BLUE);
                    }
                }
            }
            case KNIGHT -> {
                if (coordRow > 0 && coordCol > 1) // up 1 left 2
                {
                    boardState.getCell(coordRow - 1, coordCol - 2).setColor(Color.BLUE);
                }
                if (coordRow > 0 && coordCol < boardCols - 1) // up 1 right 2
                {
                    boardState.getCell(coordRow - 1, coordCol + 2).setColor(Color.BLUE);
                }
                if (coordRow < boardRows - 2 && coordCol > 1) // down 1 left 2
                {
                    boardState.getCell(coordRow + 1, coordCol - 2).setColor(Color.BLUE);
                }
                if (coordRow < boardRows - 2 && coordCol < boardCols - 3) // down 1 right 2
                {
                    boardState.getCell(coordRow + 1, coordCol + 2).setColor(Color.BLUE);
                }
                if (coordRow < boardRows - 2 && coordCol > 0) // down 2 left 1
                {
                    boardState.getCell(coordRow + 2, coordCol - 1).setColor(Color.BLUE);
                }
                if (coordRow < boardRows - 2 && coordCol < boardCols - 1) // down 2 right 1
                {
                    boardState.getCell(coordRow + 2, coordCol + 1).setColor(Color.BLUE);
                }
                if (coordRow > 1 && coordCol > 0) // up 2 left 1
                {
                    boardState.getCell(coordRow - 2, coordCol - 1).setColor(Color.BLUE);
                }
                if (coordRow > 1 && coordCol < boardCols - 1) // up 2 right 1
                {
                    boardState.getCell(coordRow - 2, coordCol + 1).setColor(Color.BLUE);
                }
            }
            case PAWN -> {
                if (coordRow < boardRows && coordRow > 0) {
                    boardState.getCell(coordRow - 1, coordCol).setColor(Color.BLUE);
                }
                if (piece.getFirstMove() && coordRow > 1) {
                    boardState.getCell(coordRow - 2, coordCol).setColor(Color.BLUE);
                }
            }
            case NONE -> {
            }
            default -> {
            }
        }
    }

    public void deselectPieces() {
        board.setColors();
    }

    public void doTurnAction() {
        for (Cell[] cells : board.getGrid()) {
            for (Cell cell : cells) {
                var coord = cell.getCoord();
                var cellObserver = cell.getCellObserver();
                var model = Chess.getModelInstance();

                cellObserver.setOnAction(e -> {
                    model.tryTurn(coord);
                });
            }
        }
    }

    public void tryTurn(Coordinate coord) {

        var currentCell = getBoard().getCell(coord);
        var currentCellObserver = currentCell.getCellObserver();
        currentCellObserver.selectPiece();

        selectSquares(currentCell);

        doMoveAction(currentCell);
    }

    public void doMoveAction(Cell currentCell) { // set all onActions to look for another click to parse as a move
        for (Cell[] cellArray : board.getGrid()) {
            for (Cell cell : cellArray) {
                cell.getCellObserver().setOnAction(e -> {
                    var model = Chess.getModelInstance();
                    model.parseMove(currentCell, cell);
                });
            }
        }
    }

    // TODO: Write collision logic and 'eat' method
    public void parseMove(Cell currentCell, Cell movetoCell) { // parse the move at the coordinate
        var movetoCoord = movetoCell.getCoord();
        var currentCoord = currentCell.getCoord();
        var currentPiece = currentCell.getPiece();
        if (currentPiece.getFirstMove()) {
            currentPiece.setFirstMove(false); // so that the pawn piece can only move two squares on first turn

        }
        if (movetoCell.getColor() == Color.BLUE) {
            board.getCell(currentCoord).getCellObserver().deselectPiece();
            board.getCell(movetoCoord).setPiece(currentPiece, currentPiece.getColor());
            board.getCell(currentCoord).removePiece();

            // Look for a win, if no win, observe to update view.
            setWinner(tryWin());

            if (getWinner() == Player.BLACK) {
                observer.win(Player.BLACK);
            } else if (getWinner() == Player.WHITE) {
                observer.win(Player.WHITE);
            } else {
                changeTurn();
                deselectPieces(); // get rid of the blue highlights
                doTurnAction(); // start the cycle again
            }
        }
    }

    public void changeTurn() {
        if (turn == Player.WHITE) {
            setTurn(Player.BLACK);
        } else {
            setTurn(Player.WHITE);
        }
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
