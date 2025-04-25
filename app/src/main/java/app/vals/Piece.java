package app.vals;

public class Piece {
    //PAWN,//(Color.NONE, true),
    //BISHOP,//(Color.NONE, true),
    //KNIGHT,//(Color.NONE, true),
    //ROOK,//(Color.NONE, true),
    //KING,//(Color.NONE, true),
    //QUEEN,//(Color.NONE, true),
    //NONE;//,(Color.NONE, true);

    private boolean firstMove = true; // for the pawns
    private Color color = Color.NONE;
    private String type = "NONE";

    public Piece(String type, Color color, boolean firstMove) {
        setColor(color);
        setFirstMove(firstMove);
        setType(type);
    }

    public Piece(String type) {
        this(type, Color.NONE, true);
    }

    public boolean getFirstMove() {
        return this.firstMove;
    }
    public Color getColor() {
        return this.color;
    }
    public String getType() {
        return this.type;
    }

    public void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public void setType(String type) {
        this.type = type;
    }

}