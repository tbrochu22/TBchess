// Knight.java
// Tyler Brochu
package ser120.TBchess.game;

import ser120.TBchess.models.ChessBoard;

public class Knight extends Piece {
    public Knight(int row, int col, boolean white) {
        String name;
        if (white) 
            name = "WN";
        else 
            name = "BN";
        super(row, col, white, name);
    }

    @Override
    public boolean canMove(ChessBoard board, int targetRow, int targetCol) {
        int dr = Math.abs(targetRow - row);
        int dc = Math.abs(targetCol - col);

        if (!((dr == 2 && dc == 1) || (dr == 1 && dc == 2))) {
            return false;
        }

        Piece target = board.getPiece(targetRow, targetCol);
        return target == null || target.isWhite() != this.white;
    }
}