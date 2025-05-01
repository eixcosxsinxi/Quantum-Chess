package app.vals;

public class Piece {

    private double probability = 1.0;
    private boolean firstMove = true; // for the pawns
    private Color color = Color.NONE;
    private String type = "NONE";
    private int ID = 0;

    public Piece(String type, Color color, boolean firstMove, double probability, int ID) {
        setType(type);
        setColor(color);
        setFirstMove(firstMove);
        setProbability(probability);
        setID(ID);
    }

    public Piece(String type, Color color, boolean firstMove, double probability) {
        this(type, color, firstMove, probability, 0);
    }

    public Piece(String type, Color color, boolean firstMove) {
        this(type, color, firstMove, 1.0);
    }

    public Piece(String type) {
        this(type, Color.NONE, true);
    }

    /* Getters */
    public boolean getFirstMove() {
        return this.firstMove;
    }
    public Color getColor() {
        return this.color;
    }
    public String getType() {
        return this.type;
    }
    public double getProbability() {
        return this.probability;
    }
    public int getID() {
        return this.ID;
    }

    /* Setters */
    public void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setProbability(double probability) {
        this.probability = probability;
    }
    public void setID(int ID) {
        this.ID = ID;
    }

}