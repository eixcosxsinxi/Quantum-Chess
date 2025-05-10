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
        System.out.println("doTurnAction"); // debug print
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
        System.out.println("button clicked, doSuperpositionAction"); // debug print
        doSuperpositionAction(currentCell);
        superposition = true;
        observer.removeSuperposition();
    }

    public void doTurnAction() {
        System.out.println("doing turnAction\n"); // debug print
        for (Cell[] cells : board.getGrid()) {
            for (Cell cell : cells) {
                var coord = cell.getCoord();
                var cellObserver = cell.getCellObserver();
                var model = Chess.getModelInstance();

                cellObserver.setOnAction(e -> {
                    System.out.println("button clicked, do tryTurn"); // debug print
                    model.tryTurn(coord); // set all buttons when clicked to try that turn
                });
            }
        }
    }

    public void tryTurn(Coordinate coord) {
        System.out.println("trying turn\n"); // debug print
        var currentCell = getBoard().getCell(coord);
        var currentCellObserver = currentCell.getCellObserver();

        if (currentCell.getPiece().getColor().toString() == getTurn().toString()) { // if piece is same color as turn
            if (!superposition)
                observer.addSuperposition(currentCell); // add the superposition button

            System.out.println("added superposition button"); // debug print
            currentCellObserver.selectPiece(); // outline the piece in blue

            selectSquares(currentCell); // select the squares that this current piece can go to

            System.out.println("doMoveAction"); // debug print
            doMoveAction(currentCell); // this will try a regular move action
        }
    }

    public void doMoveAction(Cell currentCell) { // set all onActions to look for another click to parse as a move
        System.out.println("doing moveAction\n"); // debug print
        for (Cell[] cellArray : board.getGrid()) {
            for (Cell cell : cellArray) {
                cell.getCellObserver().setOnAction(e -> {
                    System.out.println("button clicked, parse move"); // debug print
                    var model = Chess.getModelInstance();
                    model.parseMove(currentCell, cell);
                });
            }
        }
    }

    public void doSuperpositionAction(Cell currentCell) { // set all onActions to look for another click to parse as a superposition
        System.out.println("doing superpositionAction\n"); // debug print
        for (Cell[] cellArray : board.getGrid()) {
            for (Cell cell : cellArray) {
                cell.getCellObserver().setOnAction(e -> {
                    System.out.println("button clicked, parse superposition"); // debug print
                    var model = Chess.getModelInstance();
                    model.parseSuperposition(currentCell, cell); // to parse the next click at its coord
                });
            }
        }
    }

    public void parseSuperposition(Cell currentCell, Cell movetoCell) { // parse the move at the coordinate
        System.out.println("parsing superposition\n"); // debug print
        observer.removeSuperposition(); // set superposition back to default

        var movetoCoord = movetoCell.getCoord();
        var currentCoord = currentCell.getCoord();
        var currentPiece = currentCell.getPiece();

        if (currentPiece.getID() == 0) // ID of 0 means it has had no superposition move
            currentPiece.setID(ID++);

        printVals(); // debug print

        /* movetoCell can be one of three colors. Blue, Red, or None.

        * if it is blue, that means the piece is in the clear to move, and it will transfer all
        * attributes to the new cell.

        * if it is red, that means an enemy piece is there, and it will try to 'eat' the enemy piece.

        * if it is none, that means you clicked on the same piece you wanted to move.
        * your piece will stay there, and everything will deselect allowing you to try again.
        * */
        if (movetoCell.getColor() == Color.BLUE) {
            System.out.println("color is blue\n"); // debug print

            var probability = board.getCell(currentCoord).getPiece().getProbability(); // halve the current probability
            probability /= 2;

            board.getCell(currentCoord).getPiece().setProbability(probability); // set probability of current piece

            var ID = board.getCell(currentCoord).getPiece().getID();

            // copy current piece over to chosen spot and do 'regular' turn to move second choice
            board.getCell(movetoCoord).setPiece(currentPiece.getType(), currentPiece.getColor(), false, probability, ID);


            deselectPieces(); // deselect pieces so that tryTurn can select them and there won't be conflict

            tryTurn(currentCoord); // does the second move part of the superposition
        } else if (movetoCell.getColor() == Color.RED) { // BUG: when I capture, sometimes it keeps the old piece there
            System.out.println("color is red"); // debug print

            // TODO: figure out what to do in this position


            deselectPieces(); // deselect pieces so that tryTurn can select them and there won't be conflict

            tryTurn(currentCoord); // does the second move part of the superposition
        } else if (currentCoord.equals(movetoCoord)) {
            System.out.println("clicked the same piece"); // debug print

            board.getCell(currentCoord).getCellObserver().deselectPiece(); // unselects the chosen piece
            currentPiece.setID(currentPiece.getID() - 1);
            deselectPieces(); // resets highlights
            superposition = false; // resets superposition variable
            doTurnAction(); // redoes turn
        }
    }

    /*
    * This method is called whenever a 'regular' move takes place.

    * A regular move is any move that superposition is not true for. After a button is clicked,
    * it will pass its cell to this method which will parse the move just like parseSuperposition().

    * The difference between the two methods is that this one happens as the second move of a superposition
    * and after a regular move.

    * If superposition was selected and this is called for the second half, it will choose a random piece
    * from the list of pieces that have the same ID as the attacker.
    * The chosen piece then survives and the rest are thrown away.
    * */

    public void parseMove(Cell currentCell, Cell movetoCell) { // parse the move at the coordinate
        System.out.println("parsing move\n"); // debug print
        observer.removeSuperposition(); // to make it impossible to click superposition again twice in one move.

        var movetoCoord = movetoCell.getCoord();
        var currentCoord = currentCell.getCoord();
        var currentPiece = currentCell.getPiece();
        var probability = currentPiece.getProbability();
        var ID = currentPiece.getID();

        printVals(); // debug print

        /*
        * Same three options as parseSuperposition(); Red, Blue, or None.

        * Red means an enemy piece is there, Blue means no piece is there,
        * and None means player clicked on same cell.
        * */

        if (movetoCell.getColor() == Color.BLUE) {
            System.out.println("color is blue\n"); // debug print

            movePiece(currentCoord, movetoCoord);

            printVals(); // debug print

            finishTurn();
        } else if (movetoCell.getColor() == Color.RED) {
            System.out.println("color is red\n"); // debug print

            board.getCell(currentCoord).getCellObserver().deselectPiece();

            var randCoord = collapse(currentCoord, movetoCoord);
            if (!randCoord.equals(new Coordinate(-1, -1))) {
                if (randCoord.equals(movetoCoord)) // superposition found in movetoPiece and random chose current movetoPiece
                    movePiece(currentCoord, movetoCoord);
                else if (randCoord.equals(currentCoord)) // superposition found in currentPiece and random chose current piece
                    movePiece(randCoord, movetoCoord);
            }

            finishTurn();
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

    public Coordinate chooseRandom(int ID) {
        var cellArray = new ArrayList<Cell>();

        for (Cell[] cells : getBoard().getGrid()) {
            for (Cell cell : cells) {
                if (cell.getPiece().getID() == ID) { // adds all cells that are also part of the same superposition group
                    cellArray.add(cell);
                }
            }
        }

        var random = selectRandomPiece(cellArray); // chooses a random cell from the group
        var randCoord = random.getCoord();
        var piece = random.getPiece();
        piece.setProbability(1.0); // set chosen piece back to normal with no superposition
        piece.setID(0);

        System.out.println("removing pieces at following coords:"); // debug print
        for (Cell cell : cellArray) {
            if (!cell.getCoord().equals(randCoord)) { // removes any piece that is not the chosen piece
                System.out.println(cell.getCoord().getRow() + "," + cell.getCoord().getCol()); // debug print
                cell.removePiece();
            }
        }
        System.out.print("\n"); // debug print

        deselectPieces();

        return randCoord;
    }

    public Coordinate collapse(Coordinate currentCoord, Coordinate movetoCoord) {
        // check and collapse code here
        board.getCell(currentCoord).getCellObserver().deselectPiece();
        board.getCell(movetoCoord).getCellObserver().deselectPiece();

        var currentPiece = board.getCell(currentCoord).getPiece();
        var movetoPiece = board.getCell(movetoCoord).getPiece();

        var currentID = currentPiece.getID();
        var movetoID = movetoPiece.getID();

        if (currentID == 0) {
            if (movetoID == 0) // if there is no superposition involved i.e. both IDs = 0
                movePiece(currentCoord, movetoCoord);
            else { // only the moveto piece has a superposition
				return chooseRandom(movetoID); // will return the coord of the chosen collapsed piece
            }
        } else {
            if (movetoID == 0) { // only the current piece has a superposition
				return chooseRandom(currentID); // will return the coord of the chosen collapsed piece
            } else { // both pieces have a superposition
                var randMoveCoord = chooseRandom(movetoID); // simply so that the opponent still collapses.
                var randCurrentCoord = chooseRandom(currentID);

                if (randCurrentCoord.equals(currentCoord))
                    movePiece(currentCoord, movetoCoord);
            }
        }
        return new Coordinate(-1, -1);
    }

    public void printVals() { // used for debugging.
        for (Cell[] cells : getBoard().getGrid()) {
            for (Cell cell : cells) {
                System.out.print(cell.getPiece().getID() + ", "); // can use whatever value for debugging here
            }
            System.out.print("\n");
        }
        System.out.println("\n");

    }

    public void finishTurn() { // ties up turn by trying to win otherwise changing turns
        // Look for a win, if no win, observe to update view.
        setWinner(tryWin());

        if (getWinner() == Player.BLACK)
            observer.win(Player.BLACK);
        else if (getWinner() == Player.WHITE)
            observer.win(Player.WHITE);
        else {
            changeTurn();
            deselectPieces(); // get rid of the highlights
            doTurnAction(); // start the cycle again
        }
    }

    public void movePiece(Coordinate currentCoord, Coordinate movetoCoord) { // moves piece at currentCoord to movetoCoord
        var currentPiece = board.getCell(currentCoord).getPiece();

        var currID = currentPiece.getID();
        var currType = currentPiece.getType();
        var currColor = currentPiece.getColor();
        // no need to get firstMove because if this piece moves, it won't be the first move anymore
        var currProbability = currentPiece.getProbability();

        board.getCell(currentCoord).getCellObserver().deselectPiece();
        if (!board.getCell(movetoCoord).getPiece().getType().equals("NONE"))
            board.getCell(movetoCoord).removePiece();
        board.getCell(movetoCoord).setPiece(currType, currColor, false, currProbability, currID); // this preserves piece attributes
        if (!board.getCell(movetoCoord).getPiece().getType().equals("NONE"))
            board.getCell(currentCoord).removePiece();
    }

    public void changeTurn() {
        if (turn == Player.WHITE)
            setTurn(Player.BLACK);
        else
            setTurn(Player.WHITE);
    }

    public Player tryWin() {
        // TODO: write a checkmate checking function that checks both kings
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
        if (whiteKing && !blackKing)
            return Player.WHITE;
        else if (blackKing && !whiteKing)
            return Player.BLACK;
        else
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
