// Piece.java
// Tyler Brochu
package ser120.TBchess.game;

import ser120.TBchess.models.ChessBoard;

public abstract class Piece {
    protected int row;
    protected int col;
    protected boolean white;
    protected String symbol;

    public Piece(int row, int col, boolean white, String symbol) {
        this.row = row;
        this.col = col;
        this.white = white;
        this.symbol = symbol;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isWhite() {
        return white;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public abstract boolean canMove(ChessBoard board, int targetRow, int targetCol);
}