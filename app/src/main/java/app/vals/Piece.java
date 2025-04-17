package app.vals;

public enum Piece {
    PAWN(Color.NONE),
    BISHOP(Color.NONE),
    KNIGHT(Color.NONE),
    ROOK(Color.NONE),
    KING(Color.NONE),
    QUEEN(Color.NONE),
    NONE(Color.NONE);

    private boolean firstMove = true; // for the pawns
    private Color color;

    Piece(Color color) {
        setColor(color);
    }

    public boolean getFirstMove() {
        return this.firstMove;
    }
    public Color getColor() {
        return this.color;
    }

    public void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }
    public void setColor(Color color) {
        this.color = color;
    }

}