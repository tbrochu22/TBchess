// Pawn.java
// Tyler Brochu
package ser120.TBchess.game;

import ser120.TBchess.models.ChessBoard;

public class Pawn extends Piece {
    public Pawn(int row, int col, boolean white) {
        String name;
        if (white) 
            name = "WP";
        else 
            name = "BP";
        super(row, col, white, name);
    }

    @Override
    public boolean canMove(ChessBoard board, int targetRow, int targetCol) {
        int direction;
        if (white) 
            direction = -1;
        else 
            direction = 1;
        

        int startRow;
        if (white) 
            startRow = 6;
        else
            startRow = 1;

        Piece target = board.getPiece(targetRow, targetCol);

        if (col == targetCol) {
            if (target != null) {
                return false;
            }

            if (targetRow == row + direction) {
                return true;
            }

            if (row == startRow && targetRow == row + 2 * direction) {
                return board.getPiece(row + direction, col) == null;
            }
        }

        if (Math.abs(targetCol - col) == 1 && targetRow == row + direction) {
            return target != null && target.isWhite() != this.white;
        }

        return false;
    }
}