/**
 * Empty placeholder for chess move validation.
 * Assume a real rules engine lives here; for the demo every move is legal
 * (except moving onto a piece you already own).
 */
public class ChessRules {

    /** Return true if the side-to-move may move from (fromR,fromC) to (toR,toC). */
    public boolean isLegalMove(Piece[][] board, int fromR, int fromC, int toR, int toC) {
        if (fromR == toR && fromC == toC) return false;
        Piece mover = board[fromR][fromC];
        if (mover == null) return false;
        Piece target = board[toR][toC];
        if (target != null && target.getColor() == mover.getColor()) return false;
        // delegated to "already built component" — return true for the demo
        return true;
    }
}
