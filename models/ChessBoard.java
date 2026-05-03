// ChessBoard.java
// Tyler Brochu
package ser120.TBchess.models;

import ser120.TBchess.game.*;

public class ChessBoard {
    private Piece[][] board;

    public ChessBoard() {
        board = new Piece[8][8];
        setupBoard();
    }

    public void setupBoard() {
        board = new Piece[8][8];
        // Row, col, black
        board[0][0] = new Rook(0, 0, false);
        board[0][1] = new Knight(0, 1, false);
        board[0][2] = new Bishop(0, 2, false);
        board[0][3] = new Queen(0, 3, false);
        board[0][4] = new King(0, 4, false);
        board[0][5] = new Bishop(0, 5, false);
        board[0][6] = new Knight(0, 6, false);
        board[0][7] = new Rook(0, 7, false);

        for (int c = 0; c < 8; c++) {
            board[1][c] = new Pawn(1, c, false);
        }

        for (int c = 0; c < 8; c++) {
            board[6][c] = new Pawn(6, c, true);
        }
        // Row, col, white
        board[7][0] = new Rook(7, 0, true);
        board[7][1] = new Knight(7, 1, true);
        board[7][2] = new Bishop(7, 2, true);
        board[7][3] = new Queen(7, 3, true);
        board[7][4] = new King(7, 4, true);
        board[7][5] = new Bishop(7, 5, true);
        board[7][6] = new Knight(7, 6, true);
        board[7][7] = new Rook(7, 7, true);
    }

    public Piece getPiece(int row, int col) {
        if (!inBounds(row, col)) return null;
        return board[row][col];
    }

    public void setPiece(int row, int col, Piece piece) {
        if (inBounds(row, col)) {
            board[row][col] = piece;
        }
    }

    public boolean inBounds(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public boolean movePiece(int fromRow, int fromCol, int toRow, int toCol, boolean whiteTurn) {
        return movePieceDetailed(fromRow, fromCol, toRow, toCol, whiteTurn).isSuccess();
    }

    public MoveResult movePieceDetailed(int fromRow, int fromCol, int toRow, int toCol, boolean whiteTurn) {
        if (!inBounds(fromRow, fromCol) || !inBounds(toRow, toCol)) {
            return new MoveResult(false, "That square is outside the board.", null);
        }

        Piece piece = board[fromRow][fromCol];
        if (piece == null) {
            return new MoveResult(false, "Select a piece before moving.", null);
        }

        if (piece.isWhite() != whiteTurn) {
            return new MoveResult(false, "It is not that piece's turn.", null);
        }

        Piece target = board[toRow][toCol];
        if (target != null && target.isWhite() == piece.isWhite()) {
            return new MoveResult(false, "You cannot capture your own piece.", null);
        }

        if (target instanceof King) {
            return new MoveResult(false, "You cannot capture the king directly.", null);
        }

        if (!piece.canMove(this, toRow, toCol)) {
            return new MoveResult(false, "That move is illegal for this piece.", null);
        }

        if (!isSafeMove(fromRow, fromCol, toRow, toCol, whiteTurn)) {
            return new MoveResult(false, "That move would leave your king in check.", null);
        }

        board[toRow][toCol] = piece;
        board[fromRow][fromCol] = null;
        piece.setPosition(toRow, toCol);

        return new MoveResult(true, "Move completed.", target);
    }

    public boolean isInCheck(boolean white) {
        int[] kingPosition = findKing(white);
        if (kingPosition[0] == -1) {
            return false;
        }

        int kingRow = kingPosition[0];
        int kingCol = kingPosition[1];

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece == null || piece.isWhite() == white) {
                    continue;
                }

                if (piece.canMove(this, kingRow, kingCol)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hasAnyLegalMove(boolean white) {
        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {
                Piece piece = board[fromRow][fromCol];
                if (piece == null || piece.isWhite() != white) {
                    continue;
                }

                for (int toRow = 0; toRow < 8; toRow++) {
                    for (int toCol = 0; toCol < 8; toCol++) {
                        Piece target = board[toRow][toCol];
                        if (target != null && target.isWhite() == white) {
                            continue;
                        }

                        if (target instanceof King) {
                            continue;
                        }

                        if (!piece.canMove(this, toRow, toCol)) {
                            continue;
                        }

                        if (isSafeMove(fromRow, fromCol, toRow, toCol, white)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean isCheckmate(boolean white) {
        return isInCheck(white) && !hasAnyLegalMove(white);
    }

    public boolean isStalemate(boolean white) {
        return !isInCheck(white) && !hasAnyLegalMove(white);
    }

    private int[] findKing(boolean white) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece instanceof King && piece.isWhite() == white) {
                    return new int[] {row, col};
                }
            }
        }

        return new int[] {-1, -1};
    }

    private boolean isSafeMove(int fromRow, int fromCol, int toRow, int toCol, boolean whiteTurn) {
        Piece movingPiece = board[fromRow][fromCol];
        Piece capturedPiece = board[toRow][toCol];
        int originalRow = movingPiece.getRow();
        int originalCol = movingPiece.getCol();

        board[toRow][toCol] = movingPiece;
        board[fromRow][fromCol] = null;
        movingPiece.setPosition(toRow, toCol);

        boolean safe = !isInCheck(whiteTurn);

        board[fromRow][fromCol] = movingPiece;
        board[toRow][toCol] = capturedPiece;
        movingPiece.setPosition(originalRow, originalCol);

        return safe;
    }

    public boolean isPathClear(int fromRow, int fromCol, int toRow, int toCol) {
        // if toRow is larger than fromRow, return 1
        // if toRow is the same as fromRow, return 0
        // if toRow is smaller than fromRow, return -1

        int rowStep = Integer.compare(toRow, fromRow);
        int colStep = Integer.compare(toCol, fromCol);

        int r = fromRow + rowStep;
        int c = fromCol + colStep;

        while (r != toRow || c != toCol) {
            if (board[r][c] != null) return false;
            r += rowStep;
            c += colStep;
        }

        return true;
    }

    public boolean hasKing(boolean white) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p instanceof King && p.isWhite() == white) {
                    return true;
                }
            }
        }
        return false;
    }

    public void clearBoard() {
        board = new Piece[8][8];
    }

    public void displayBoard() {
        System.out.println();
        for (int r = 0; r < 8; r++) {
            System.out.print((8 - r) + " ");
            for (int c = 0; c < 8; c++) {
                if (board[r][c] == null) {
                    System.out.print("-- ");
                } else {
                    System.out.print(board[r][c].getSymbol() + " ");
                }
            }
            System.out.println();
        }
        System.out.println("  a  b  c  d  e  f  g  h");
        System.out.println();
    }
}
