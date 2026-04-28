// Queen.java
// Tyler Brochu
package ser120.TBchess.game;

import ser120.TBchess.models.ChessBoard;

public class Queen extends Piece {
    public Queen(int row, int col, boolean white) {
        String name;
        if (white) 
            name = "WQ";
        else 
            name = "BQ";
        super(row, col, white, name);
    }

    @Override
    public boolean canMove(ChessBoard board, int targetRow, int targetCol) {
        int dr = Math.abs(targetRow - row);
        int dc = Math.abs(targetCol - col);

        boolean straight = (row == targetRow || col == targetCol);
        boolean diagonal = (dr == dc && dr > 0);

        if (!straight && !diagonal) {
            return false;
        }

        if (!board.isPathClear(row, col, targetRow, targetCol)) {
            return false;
        }

        Piece target = board.getPiece(targetRow, targetCol);
        return target == null || target.isWhite() != this.white;
    }
}