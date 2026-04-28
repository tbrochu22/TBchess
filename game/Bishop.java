// Bishop.java
// Tyler Brochu
package ser120.TBchess.game;

import ser120.TBchess.models.ChessBoard;

public class Bishop extends Piece {
    public Bishop(int row, int col, boolean white) {
        String name;
        if (white) 
            name = "WB";
        else 
            name = "BB";
        super(row, col, white, name);
    }

    @Override
    public boolean canMove(ChessBoard board, int targetRow, int targetCol) {
        int dr = Math.abs(targetRow - row);
        int dc = Math.abs(targetCol - col);

        if (dr == 0 || dr != dc) {
            return false;
        }

        if (!board.isPathClear(row, col, targetRow, targetCol)) {
            return false;
        }

        Piece target = board.getPiece(targetRow, targetCol);
        return target == null || target.isWhite() != this.white;
    }
}