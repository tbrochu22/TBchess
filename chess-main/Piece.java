public class Piece {

    public enum Type { PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING }
    public enum Color { WHITE, BLACK }

    private final Type type;
    private final Color color;

    public Piece(Type type, Color color) {
        this.type = type;
        this.color = color;
    }

    public Type getType()   { return type; }
    public Color getColor() { return color; }
}
