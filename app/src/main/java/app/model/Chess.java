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
        System.out.println("doTurnAction");
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
        System.out.println("button clicked, doSuperpositionAction");
        doSuperpositionAction(currentCell);
        superposition = true;
        observer.removeSuperposition();
    }

    public void doTurnAction() {
        System.out.println("doing turnAction\n");
        for (Cell[] cells : board.getGrid()) {
            for (Cell cell : cells) {
                var coord = cell.getCoord();
                var cellObserver = cell.getCellObserver();
                var model = Chess.getModelInstance();

                cellObserver.setOnAction(e -> {
                    System.out.println("button clicked, do tryTurn");
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
        System.out.println("trying turn\n");
        var currentCell = getBoard().getCell(coord);
        var currentCellObserver = currentCell.getCellObserver();

        if (currentCell.getPiece().getColor().toString() == getTurn().toString()) { // if piece is same color as turn
            if (!superposition)
                observer.addSuperposition(currentCell); // add the superposition button

            System.out.println("added superposition button");
            currentCellObserver.selectPiece(); // outline the piece in blue

            selectSquares(currentCell); // select the squares that this current piece can go to

            System.out.println("doMoveAction");
            doMoveAction(currentCell); // this will try a regular move action
        }
    }

    public void doMoveAction(Cell currentCell) { // set all onActions to look for another click to parse as a move
        System.out.println("doing moveAction\n");
        for (Cell[] cellArray : board.getGrid()) {
            for (Cell cell : cellArray) {
                cell.getCellObserver().setOnAction(e -> {
                    System.out.println("button clicked, parse move");
                    var model = Chess.getModelInstance();
                    model.parseMove(currentCell, cell);
                });
            }
        }
    }

    public void doSuperpositionAction(Cell currentCell) { // set all onActions to look for another click to parse as a superposition
        System.out.println("doing superpositionAction\n");
        for (Cell[] cellArray : board.getGrid()) {
            for (Cell cell : cellArray) {
                cell.getCellObserver().setOnAction(e -> {
                    System.out.println("button clicked, parse superposition");
                    var model = Chess.getModelInstance();
                    model.parseSuperposition(currentCell, cell); // to parse the next click at its coord
                });
            }
        }
    }

    public void parseSuperposition(Cell currentCell, Cell movetoCell) { // parse the move at the coordinate
        System.out.println("parsing superposition\n");
        observer.removeSuperposition(); // set superposition back to default

        var movetoCoord = movetoCell.getCoord();
        var currentCoord = currentCell.getCoord();
        var currentPiece = currentCell.getPiece();
        if (currentPiece.getID() == 0) { // ID of 0 means it has had no superposition move
            currentPiece.setID(ID++);
            System.out.println("current piece got new ID of " + (ID-1) + "\n");
        }
        System.out.println("current IDs");
        for (Cell[] cells : getBoard().getGrid()) {
            for (Cell cell : cells) {
                var print = cell.getPiece().getID();
                System.out.print(print + ", ");
            }
            System.out.print("\n");
        }
        System.out.print("\n");

        if (movetoCell.getColor() == Color.BLUE) {
            System.out.println("color is blue\n");
            var probability = board.getCell(currentCoord).getPiece().getProbability();
            probability /= 2;

            System.out.println("current piece probability is " + probability + "\n");
            board.getCell(currentCoord).getPiece().setProbability(probability);
            var ID = board.getCell(currentCoord).getPiece().getID();
            System.out.println("current and new piece ID is " + ID + "\n");
            board.getCell(movetoCoord).setPiece(currentPiece.getType(), currentPiece.getColor(), false, probability, ID);
        } else if (movetoCell.getColor() == Color.RED) {
            System.out.println("color is red");
            // TODO: figure out how to colapse superposition
            // probably will involve ID numbers
        } else if (currentCoord.equals(movetoCoord)) {
            System.out.println("clicked the same piece");
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
        System.out.println("parsing move\n");
        observer.removeSuperposition();

        var movetoCoord = movetoCell.getCoord();
        var currentCoord = currentCell.getCoord();
        var currentPiece = currentCell.getPiece();
        var probability = currentPiece.getProbability();
        var ID = currentPiece.getID();
        System.out.println("new piece ID is " + ID + "\n");

        System.out.println("current IDs");
        for (Cell[] cells : getBoard().getGrid()) {
            for (Cell cell : cells) {
                var print = cell.getPiece().getID();
                System.out.print(print + ", ");
            }
            System.out.print("\n");
        }
        System.out.print("\n");

        if (movetoCell.getColor() == Color.BLUE) {
            System.out.println("color is blue\n");
            board.getCell(currentCoord).getCellObserver().deselectPiece();
            board.getCell(movetoCoord).setPiece(currentPiece.getType(), currentPiece.getColor(), false, probability, ID);
            board.getCell(currentCoord).removePiece();

            System.out.println("old piece ID is " + board.getCell(currentCoord).getPiece().getID() + "\n");

            System.out.println("current IDs");
            for (Cell[] cells : getBoard().getGrid()) {
                for (Cell cell : cells) {
                    var print = cell.getPiece().getID();
                    System.out.print(print + ", ");
                }
                System.out.print("\n");
            }
            System.out.print("\n");

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
            System.out.println("color is red\n");
            if (currentCell.getPiece().getID() == 0) { // checks if the piece you want to move is not part of a superposition
                // TODO: write a colapse code for enemy superposition
                board.getCell(currentCoord).getCellObserver().deselectPiece();
                board.getCell(movetoCoord).removePiece();
                board.getCell(movetoCoord).setPiece(currentPiece.getType(), currentPiece.getColor(), false);
                board.getCell(currentCoord).removePiece();
            } else {
                var movetoPiece = board.getCell(movetoCoord).getPiece();
                var oldType = movetoPiece.getType();
                var oldColor = movetoPiece.getColor();
                var oldFisrtMove = movetoPiece.getFirstMove();
                var oldProbability = movetoPiece.getProbability();
                var oldID = movetoPiece.getID();

                System.out.println("moveToPiece piece is:");
                System.out.println("Type: " + movetoPiece.getType());
                System.out.println("Color: " + movetoPiece.getColor().toString());
                System.out.println("FirstMove: " + movetoPiece.getFirstMove());
                System.out.println("Probability: " + movetoPiece.getProbability());
                System.out.println("ID: " + movetoPiece.getID() + "\n");

                board.getCell(currentCoord).getCellObserver().deselectPiece();
                board.getCell(movetoCoord).removePiece();
                board.getCell(movetoCoord).setPiece(currentPiece.getType(), currentPiece.getColor(), false, currentPiece.getProbability(), currentPiece.getID());
                board.getCell(currentCoord).removePiece();
                System.out.println("current IDs");
                for (Cell[] cells : getBoard().getGrid()) {
                    for (Cell cell : cells) {
                        System.out.print(cell.getPiece().getID() + ", ");
                    }
                    System.out.print("\n");
                }
                System.out.println("\n");

                var cellArray = new ArrayList<Cell>(); // creates an array of pieces with same ID for randomizer
                System.out.println("cellArray:");

                for (Cell[] cells : getBoard().getGrid()) {
                    for (Cell cell : cells) {
                        if (cell.getPiece().getID() == ID) { // adds all cells that are also part of the same superposition group
                            cellArray.add(cell);
                            System.out.println(cell.getCoord().getRow() + "," + cell.getCoord().getCol());
                        }
                    }
                }

                var random = selectRandomPiece(cellArray); // chooses a random cell from the group
                var randCoord = random.getCoord();
                System.out.println("randCoord = " + randCoord.getRow() + "," + randCoord.getCol());
                System.out.println("movetoCoord = " + movetoCoord.getRow() + "," + movetoCoord.getCol() + "\n");
                var piece = random.getPiece();
                piece.setProbability(1.0); // set chosen piece back to normal with no superposition
                piece.setID(0);

                //board.getCell(currentCoord).getCellObserver().deselectPiece();

                System.out.println("removing pieces at following coords:");
                for (Cell cell : cellArray) {
                    if (!cell.getCoord().equals(random.getCoord())) { // removes any piece that is not the chosen piece
                        System.out.println(cell.getCoord().getRow() + "," + cell.getCoord().getCol());
                        cell.removePiece();
                    }
                }
                System.out.print("\n");

                deselectPieces();

               board.getCell(movetoCoord).setPiece(oldType, oldColor, oldFisrtMove, oldProbability, oldID);
                var randPiece = board.getCell(randCoord).getPiece();
                if (random.getCoord().equals(movetoCoord))
                    board.getCell(movetoCoord).setPiece(randPiece.getType(), randPiece.getColor(), false);
                else
                    board.getCell(movetoCoord).setPiece(movetoPiece.getType(), movetoPiece.getColor(), false, movetoPiece.getProbability(), movetoPiece.getID());
                //selectSquares(random); // select the squares around the chosen cell to try to move the piece there
                //parseMove(random, movetoCell); // go through all parseMove branches and move the piece accordingly
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
