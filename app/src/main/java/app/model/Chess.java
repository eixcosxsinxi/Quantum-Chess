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
    private boolean superposition = false;

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
        doTurnAction();
    }

    public boolean inCheck() {
        // TODO: write inCheck.
        /*
        * probably will highlight all squares of all pieces on board and check if king is on blue square.
        * */
        return false; // for now...
    }

    private boolean inRange(int row, int col) { // to check if a cell coord is in range
        int max = getBoard().getRows() - 1;
        return row >= 0 && row <= max && col >= 0 && col <= max;
    }

    private boolean colorCell(int row, int col, Color color) { // the colorCell logic
        if (inRange(row, col)) {
            var cell = getBoard().getCell(row, col);
            var pieceColor = cell.getPiece().getColor();

            if (pieceColor.equals(Color.NONE)) {
                cell.setColor(Color.BLUE);
                return true;
            } else if (!pieceColor.equals(color)) {
                cell.setColor(Color.RED);
            }
        }
        return false;
    }

    public void colorPawnCell(int row, int col, Color color) {
        var piece = getBoard().getCell(row, col).getPiece();
        var boardRows = getBoard().getRows();
        var boardCols = getBoard().getCols();

        if (color == Color.WHITE) {
            if (row > 0 && getBoard().getCell(row - 1, col).getPiece().getColor() == Color.NONE) {
                getBoard().getCell(row - 1, col).setColor(Color.BLUE);
                if (row > 1 && piece.getFirstMove()) {
                    var cell = getBoard().getCell(row - 2, col);
                    if (cell.getPiece().getColor() == Color.NONE)
                        cell.setColor(Color.BLUE);
                }
            }
            if (row > 0 && col > 0) {
                var upLeftColor = getBoard().getCell(row - 1, col - 1).getPiece().getColor();
                if (upLeftColor != Color.NONE && upLeftColor != color)
                    getBoard().getCell(row - 1, col - 1).setColor(Color.RED);
            }
            if (row > 0 && col < boardCols - 1) {
                var upRightColor = getBoard().getCell(row - 1, col + 1).getPiece().getColor();
                if (upRightColor != Color.NONE && upRightColor != color)
                    getBoard().getCell(row - 1, col + 1).setColor(Color.RED);
            }
        } else {
            if (row < boardRows - 1 && getBoard().getCell(row + 1, col).getPiece().getColor() == Color.NONE) {
                getBoard().getCell(row + 1, col).setColor(Color.BLUE);
                if (row < boardRows - 2 && piece.getFirstMove()) {
                    var cell = getBoard().getCell(row + 2, col);
                    if (cell.getPiece().getColor() == Color.NONE)
                        cell.setColor(Color.BLUE);
                }
            }
            if (row < boardRows - 1 && col > 0) {
                var downLeftColor = getBoard().getCell(row + 1, col - 1).getPiece().getColor();
                if (downLeftColor != Color.NONE && downLeftColor != color)
                    getBoard().getCell(row + 1, col - 1).setColor(Color.RED);
            }
            if (row < boardRows - 1 && col < boardCols - 1) {
                var downRightColor = getBoard().getCell(row + 1, col + 1).getPiece().getColor();
                if (downRightColor != Color.NONE && downRightColor != color)
                    getBoard().getCell(row + 1, col + 1).setColor(Color.RED);
            }
        }
    }

    public void selectSquares(Cell currentCell) {

        var piece = currentCell.getPiece();
        var type = piece.getType();
        var color = piece.getColor();
        var boardState = getBoard();
        var coord = currentCell.getCoord();

        var coordRow = coord.getRow();
        var coordCol = coord.getCol();
        var boardRows = boardState.getRows();
        var boardCols = boardState.getCols();

        switch (type) { // highlight squares according to each type of piece
            case "KING" -> {
                // TODO: write logic for king in check
                if (!inCheck()) {
                    for (int i = -1; i <= 1; ++i) {
                        for (int j = -1; j <= 1; ++j) {
                            if (i != 0 || j != 0) {
                                colorCell(coordRow + i, coordCol + j, color);
                            }
                        }
                    }
                }
            }
            case "QUEEN" -> {
                int top = coordRow + 1;
                int bottom = boardRows - coordRow;
                int left = coordCol + 1;
                int right = boardCols - coordCol;

                int end = Math.min(right, bottom);
                for (int i = 1; i < end; i++) { // highlights the SE diagonal
                    if (!colorCell(coordRow + i, coordCol + i, color))
                        break;
                }
                end = Math.min(left, top);
                for (int i = 1; i < end; i++) { // highlights the NW diagonal
                    if (!colorCell(coordRow - i, coordCol - i, color))
                        break;
                }
                end = Math.min(right, top);
                for (int i = 1; i < end; i++) { // highlights the NE diagonal
                    if (!colorCell(coordRow - i, coordCol + i, color))
                        break;
                }
                end = Math.min(left, bottom);
                for (int i = 1; i < end; i++) { // highlights the SW diagonal
                    if (!colorCell(coordRow + i, coordCol - i, color))
                        break;
                }
                for (int i = 1; i < top; i++) { // highlights the N column
                    if (!colorCell(coordRow - i, coordCol, color))
                        break;
                }
                for (int i = 1; i < bottom; i++) { // highlights the S column
                    if (!colorCell(coordRow + i, coordCol, color))
                        break;
                }
                for (int i = 1; i < left; i++) { // highlights the W row
                    if (!colorCell(coordRow, coordCol - i, color))
                        break;
                }
                for (int i = 1; i < right; i++) { // highlights the E row
                    if (!colorCell(coordRow, coordCol + i, color))
                        break;
                }
            }
            case "ROOK" -> {
                int top = coordRow + 1;
                int bottom = boardRows - coordRow;
                int left = coordCol + 1;
                int right = boardCols - coordCol;

                for (int i = 1; i < top; i++) { // highlights the N column
                    if (!colorCell(coordRow - i, coordCol, color))
                        break;
                }
                for (int i = 1; i < bottom; i++) { // highlights the S column
                    if (!colorCell(coordRow + i, coordCol, color))
                        break;
                }
                for (int i = 1; i < left; i++) { // highlights the W row
                    if (!colorCell(coordRow, coordCol - i, color))
                        break;
                }
                for (int i = 1; i < right; i++) { // highlights the E row
                    if (!colorCell(coordRow, coordCol + i, color))
                        break;
                }
            }
            case "BISHOP" -> {
                int top = coordRow + 1;
                int bottom = boardRows - coordRow;
                int left = coordCol + 1;
                int right = boardCols - coordCol;

                int end = Math.min(right, bottom);
                for (int i = 1; i < end; i++) { // highlights the SE diagonal
                    if (!colorCell(coordRow + i, coordCol + i, color))
                        break;
                }
                end = Math.min(left, top);
                for (int i = 1; i < end; i++) { // highlights the NW diagonal
                    if (!colorCell(coordRow - i, coordCol - i, color))
                        break;
                }
                end = Math.min(right, top);
                for (int i = 1; i < end; i++) { // highlights the NE diagonal
                    if (!colorCell(coordRow - i, coordCol + i, color))
                        break;
                }
                end = Math.min(left, bottom);
                for (int i = 1; i < end; i++) { // highlights the SW diagonal
                    if (!colorCell(coordRow + i, coordCol - i, color))
                        break;
                }
            }
            case "KNIGHT" -> {
                if (coordRow > 0 && coordCol > 1) // up 1 left 2
                    colorCell(coordRow - 1, coordCol - 2, color);
                if (coordRow > 0 && coordCol < boardCols - 2) // up 1 right 2
                    colorCell(coordRow - 1, coordCol + 2, color);
                if (coordRow < boardRows - 1 && coordCol > 1) // down 1 left 2
                    colorCell(coordRow + 1, coordCol - 2, color);
                if (coordRow < boardRows - 1 && coordCol < boardCols - 2) // down 1 right 2
                    colorCell(coordRow + 1, coordCol + 2, color);
                if (coordRow < boardRows - 2 && coordCol > 0) // down 2 left 1
                    colorCell(coordRow + 2, coordCol - 1, color);
                if (coordRow < boardRows - 2 && coordCol < boardCols - 1) // down 2 right 1
                    colorCell(coordRow + 2, coordCol + 1, color);
                if (coordRow > 1 && coordCol > 0) // up 2 left 1
                    colorCell(coordRow - 2, coordCol - 1, color);
                if (coordRow > 1 && coordCol < boardCols - 1) // up 2 right 1
                    colorCell(coordRow - 2, coordCol + 1, color);
            }
            case "PAWN" -> {
                colorPawnCell(coordRow, coordCol, color);
            }
            case "NONE" -> {}
            default -> {}
        }
    }

    public void deselectPieces() {
        board.setColors();
    }

    public void trySuperposition() {
        superposition = true;
        observer.removeSuperposition();
    }

    public void doTurnAction() {
        for (Cell[] cells : board.getGrid()) {
            for (Cell cell : cells) {
                var coord = cell.getCoord();
                var cellObserver = cell.getCellObserver();
                var model = Chess.getModelInstance();

                cellObserver.setOnAction(e -> {
                    model.tryTurn(coord); // set all buttons when clicked to try that turn
                });
            }
        }
    }

    public void tryTurn(Coordinate coord) {

        observer.addSuperposition(); // when a button is clicked, add the superposition button

        var currentCell = getBoard().getCell(coord);
        var currentCellObserver = currentCell.getCellObserver();

        if (currentCell.getPiece().getColor().toString() == getTurn().toString()) { // if piece is same color as turn
            currentCellObserver.selectPiece(); // outline the piece in blue

            selectSquares(currentCell); // select the squares that this current piece can go to

            doSuperpositionAction(currentCell); // this will try the superposition action method
        }
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

    public void doSuperpositionAction(Cell currentCell) { // set all onActions to look for another click to parse as a move
        for (Cell[] cellArray : board.getGrid()) {
            for (Cell cell : cellArray) {
                cell.getCellObserver().setOnAction(e -> {
                    var model = Chess.getModelInstance();
                    model.parseSuperposition(currentCell, cell); // to parse the next click at its coord
                });
            }
        }
    }
    public void parseSuperposition(Cell currentCell, Cell movetoCell) { // parse the move at the coordinate
        if (superposition) { // if superposition truly has been enabled before the next board click
            superposition = false; // set superposition back to default
            observer.removeSuperposition();

            var movetoCoord = movetoCell.getCoord();
            var currentCoord = currentCell.getCoord();
            var currentPiece = currentCell.getPiece();

            if (movetoCell.getColor() == Color.BLUE) {
                board.getCell(movetoCoord).setPiece(currentPiece.getType(), currentPiece.getColor(), false);
            } else if (movetoCell.getColor() == Color.RED) {
                board.getCell(movetoCoord).removePiece();
                board.getCell(movetoCoord).setPiece(currentPiece.getType(), currentPiece.getColor(), false);
            } else if (currentCoord.equals(movetoCoord)) {
                board.getCell(currentCoord).getCellObserver().deselectPiece();
                deselectPieces();
                superposition = true;
                doSuperpositionAction(currentCell);
            }

            deselectPieces();

            tryTurn(currentCoord); // does the second move part of the superposition
        } else {
            parseMove(currentCell, movetoCell); // this will happen if superposition button has not been clicked before a board spot
        }
    }

    public void parseMove(Cell currentCell, Cell movetoCell) { // parse the move at the coordinate
        superposition = false;
        observer.removeSuperposition();

        var movetoCoord = movetoCell.getCoord();
        var currentCoord = currentCell.getCoord();
        var currentPiece = currentCell.getPiece();

        if (movetoCell.getColor() == Color.BLUE) {
            board.getCell(currentCoord).getCellObserver().deselectPiece();
            board.getCell(movetoCoord).setPiece(currentPiece.getType(), currentPiece.getColor(), false);
            board.getCell(currentCoord).removePiece();

            // Look for a win, if no win, observe to update view.
            setWinner(tryWin());

            if (getWinner() == Player.BLACK)
                observer.win(Player.BLACK);
            else if (getWinner() == Player.WHITE)
                observer.win(Player.WHITE);
            else {
                changeTurn();
                deselectPieces(); // get rid of the blue highlights
                doTurnAction(); // start the cycle again
            }
        } else if (movetoCell.getColor() == Color.RED) {
            board.getCell(currentCoord).getCellObserver().deselectPiece();
            board.getCell(movetoCoord).removePiece();
            board.getCell(movetoCoord).setPiece(currentPiece.getType(), currentPiece.getColor(), false);
            board.getCell(currentCoord).removePiece();

            // Look for a win, if no win, observe to update view.
            setWinner(tryWin());

            if (getWinner() == Player.BLACK)
                observer.win(Player.BLACK);
            else if (getWinner() == Player.WHITE)
                observer.win(Player.WHITE);
            else {
                changeTurn();
                deselectPieces(); // get rid of the blue highlights
                doTurnAction(); // start the cycle again
            }
        } else if (currentCoord.equals(movetoCoord)) {
            board.getCell(currentCoord).getCellObserver().deselectPiece();
            deselectPieces();
            doTurnAction();
        }
    }

    public void changeTurn() {
        if (turn == Player.WHITE)
            setTurn(Player.BLACK);
        else
            setTurn(Player.WHITE);
    }

    public Player tryWin() {
        var board = getBoard();
        var cells = board.getGrid();

        boolean whiteKing = false;
        boolean blackKing = false;

        for (Cell[] cellArray : cells) {
            for (Cell cell : cellArray) {
                if (cell.getPiece().getType().equals("KING")) {
                    if (cell.getPiece().getColor() == Color.WHITE)
                        whiteKing = true;
                    else if (cell.getPiece().getColor() == Color.BLACK)
                        blackKing = true;
                }
            }
        }
        if (whiteKing && blackKing)
            return Player.NONE;
        else if (whiteKing)
            return Player.WHITE;
        else
            return Player.BLACK;
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
