// Rook.java
// Tyler Brochu
package ser120.TBchess.game;

import ser120.TBchess.models.ChessBoard;

public class Rook extends Piece {
    public Rook(int row, int col, boolean white) {
        String name;
        if (white) 
            name = "WR";
        else 
            name = "BR";
        super(row, col, white, name);
    }

    @Override
    public boolean canMove(ChessBoard board, int targetRow, int targetCol) {
        if (targetRow == row && targetCol == col) {
            return false;
        }

        if (targetRow != row && targetCol != col) {
            return false;
        }

        if (!board.isPathClear(row, col, targetRow, targetCol)) {
            return false;
        }

        Piece target = board.getPiece(targetRow, targetCol);
        return target == null || target.isWhite() != this.white;
    }
}