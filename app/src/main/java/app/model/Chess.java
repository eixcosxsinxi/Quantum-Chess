package app.model;

import app.*;
import app.vals.*;

import java.util.ArrayList;
import java.util.Random;

public class Chess {

    private static Chess model;

    private Board board;
    private Player turn = Player.WHITE;
    private Player winner = Player.NONE;
    private Observer observer;
    private boolean superposition = false;
    private int ID = 1;

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

    public Cell selectRandomPiece(ArrayList<Cell> cells) {
        double randomValue = new Random().nextDouble();
        double cumulativeProbability = 0.0;

        for (Cell cell : cells) {
            cumulativeProbability += cell.getPiece().getProbability();
            if (randomValue < cumulativeProbability) {
                return cell;
            }
        }

        // Fallback in case of floating-point precision issues
        return cells.getLast();
    }

    public void deselectPieces() {
        board.setColors();
    }

    public void trySuperposition(Cell currentCell) { // if superposition button is clicked before another cell, do superposition
        doSuperpositionAction(currentCell);
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

       /*   // to show the probability of each peice, can change to any property of piece or cell
        for (Cell[] cells : getBoard().getGrid()) {
            for (Cell cell : cells) {
                System.out.print(cell.getPiece().getID() + ", ");
            }
            System.out.println("");
        }
        System.out.println("\n");
        // */
    }

    public void tryTurn(Coordinate coord) {
        var currentCell = getBoard().getCell(coord);
        var currentCellObserver = currentCell.getCellObserver();

        if (currentCell.getPiece().getColor().toString() == getTurn().toString()) { // if piece is same color as turn
            if (!superposition)
                observer.addSuperposition(currentCell); // add the superposition button

            currentCellObserver.selectPiece(); // outline the piece in blue

            selectSquares(currentCell); // select the squares that this current piece can go to

            doMoveAction(currentCell); // this will try a regular move action
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

    public void doSuperpositionAction(Cell currentCell) { // set all onActions to look for another click to parse as a superposition
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
        observer.removeSuperposition(); // set superposition back to default

        var movetoCoord = movetoCell.getCoord();
        var currentCoord = currentCell.getCoord();
        var currentPiece = currentCell.getPiece();
        if (currentPiece.getID() == 0) // ID of 0 means it has had no superposition move
            currentPiece.setID(ID++);

        if (movetoCell.getColor() == Color.BLUE) {
            var probability = board.getCell(currentCoord).getPiece().getProbability();
            probability /= 2;

            board.getCell(currentCoord).getPiece().setProbability(probability);
            var ID = board.getCell(currentCoord).getPiece().getID();
            board.getCell(movetoCoord).setPiece(currentPiece.getType(), currentPiece.getColor(), false, probability, ID);
        } else if (movetoCell.getColor() == Color.RED) {
            // TODO: figure out how to colapse superposition
            // probably will involve ID numbers
            var cellArray = new ArrayList<Cell>(); // creates an array of pieces with same ID for randomizer

            for (Cell[] cells : getBoard().getGrid()) {
                for (Cell cell : cells) {
                    if (cell.getPiece().getID() == ID)
                        System.out.println(cell.getCoord().getRow() + "," + cell.getCoord().getCol());
                        cellArray.add(cell);
                }
            }

            var random = selectRandomPiece(cellArray);
            var piece = random.getPiece();
            System.out.println(random.getCoord().getRow() + "," + random.getCoord().getCol());

            board.getCell(currentCoord).getCellObserver().deselectPiece();
            if (random.getCoord().equals(movetoCoord))
                board.getCell(movetoCoord).removePiece();
            board.getCell(movetoCoord).setPiece(piece.getType(), piece.getColor(), false);

            for (Cell cell : cellArray) {
                if (!cell.getCoord().equals(random.getCoord())) {
                    cell.removePiece();
                    cell.getPiece().setID(0);
                }
            }
        } else if (currentCoord.equals(movetoCoord)) {
        //} else {
            board.getCell(currentCoord).getCellObserver().deselectPiece();
            deselectPieces();
            superposition = false;
            doTurnAction();
        }

        deselectPieces();

        tryTurn(currentCoord); // does the second move part of the superposition
    }

    public void parseMove(Cell currentCell, Cell movetoCell) { // parse the move at the coordinate
        observer.removeSuperposition();

        var movetoCoord = movetoCell.getCoord();
        var currentCoord = currentCell.getCoord();
        var currentPiece = currentCell.getPiece();
        var probability = currentPiece.getProbability();
        var ID = currentPiece.getID();

        if (movetoCell.getColor() == Color.BLUE) {
            board.getCell(currentCoord).getCellObserver().deselectPiece();
            board.getCell(movetoCoord).setPiece(currentPiece.getType(), currentPiece.getColor(), false, probability, ID);
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
            if (currentCell.getPiece().getID() == 0) { // checks if the piece you want to move is not part of a superposition
                board.getCell(currentCoord).getCellObserver().deselectPiece();
                board.getCell(movetoCoord).removePiece();
                board.getCell(movetoCoord).setPiece(currentPiece.getType(), currentPiece.getColor(), false);
                board.getCell(currentCoord).removePiece();
            } else {
                var cellArray = new ArrayList<Cell>(); // creates an array of pieces with same ID for randomizer

                for (Cell[] cells : getBoard().getGrid()) {
                    for (Cell cell : cells) {
                        if (cell.getPiece().getID() == ID) // adds all cells that are also part of the same superposition group
                            cellArray.add(cell);
                    }
                }

                var random = selectRandomPiece(cellArray); // chooses a random cell from the group
                var randCoord = random.getCoord();
                System.out.println("randCoord = " + randCoord.getRow() + "," + randCoord.getCol());
                System.out.println("movetoCoord = " + movetoCoord.getRow() + "," + movetoCoord.getCol() + "\n");
                var piece = random.getPiece();
                piece.setProbability(1.0); // set chosen piece back to normal with no superposition
                piece.setID(0);

                board.getCell(currentCoord).getCellObserver().deselectPiece();

                for (Cell cell : cellArray) {
                    System.out.println(cell.getCoord().getRow() + "," + cell.getCoord().getCol());
                    /*
                    if (!cell.getCoord().equals(random.getCoord())) { // removes any piece that is not the chosen piece
                        cell.removePiece();
                        cell.getPiece().setID(0);
                        cell.getPiece().setProbability(1.0);
                    }

                     */
                }
                selectSquares(random); // select the squares around the chosen cell to try to move the piece there
                parseMove(random, movetoCell); // go through all parseMove branches and move the piece accordingly
                changeTurn();
            }

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
            if (superposition) {
                board.getCell(currentCoord).getPiece().setProbability(probability);
                board.getCell(currentCoord).getCellObserver().deselectPiece();
                deselectPieces();
                changeTurn();
                doTurnAction();
            } else {
                board.getCell(currentCoord).getCellObserver().deselectPiece();
                deselectPieces();
                doTurnAction();
            }
        }
        superposition = false;
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
