// King.java
// Tyler Brochu
package ser120.TBchess.game;

import ser120.TBchess.models.ChessBoard;

public class King extends Piece {
    public King(int row, int col, boolean white) {
        String name;
        if (white) 
            name = "WK";
        else 
            name = "BK";
        super(row, col, white, name);
    }

    @Override
    public boolean canMove(ChessBoard board, int targetRow, int targetCol) {
        int dr = Math.abs(targetRow - row);
        int dc = Math.abs(targetCol - col);

        if (!(dr <= 1 && dc <= 1 && (dr != 0 || dc != 0))) {
            return false;
        }

        Piece target = board.getPiece(targetRow, targetCol);
        return target == null || target.isWhite() != this.white;
    }
}