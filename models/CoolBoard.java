// CoolBoard.java
// Tyler Brochu
package ser120.TBchess.models;

public class CoolBoard extends Board {
    private static final int BLACK_KING_ROW = 0;
    private static final int BLACK_KING_COL = 4;

    // ANSI Escape Codes for Colors
    public static final String RESET = "\u001B[0m";
    public static final String GREEN_BG = "\u001B[42m";
    public static final String BLACK_BG = "\u001B[40m";
    public static final String WHITE_TEXT = "\u001B[37m";
    public static final String YELLOW_TEXT = "\u001B[33m";

    private int kingRow, kingCol;
    private int pawnRow, pawnCol;
    private int rookRow, rookCol;
    private int bishopRow, bishopCol;
    private int knightRow, knightCol;
    private int queenRow, queenCol;


    public CoolBoard(int rows, int cols) {
        super(rows, cols); // Call the original Board constructor
    }

    public void updateKingPosition(int r, int c) {
        this.kingRow = r;
        this.kingCol = c;
    }
    public void updatePawnPosition(int r, int c) {
        this.pawnRow = r;
        this.pawnCol = c;
    }
    public void updateRookPosition(int r, int c) {
        this.rookRow = r;
        this.rookCol = c;
    }
    public void updateBishopPosition(int r, int c) {
        this.bishopRow = r;
        this.bishopCol = c;
    }
    public void updateKnightPosition(int r, int c) {
        this.knightRow = r;
        this.knightCol = c;
    }
    public void updateQueenPosition(int r, int c) {
        this.queenRow = r;
        this.queenCol = c;
    }

    @Override
    public void showBoard() {
        System.out.println(YELLOW_TEXT + "\n=== CHESS ADVENTURE BOARD ===" + RESET);
        
        for (int i = 0; i < 8; i++) { // Using 8 for standard chess
            System.out.print(8 - i);
            for (int j = 0; j < 8; j++) {
                // Alternating background colors like a real chessboard
                String background = ((i + j) % 2 == 0) ? GREEN_BG : BLACK_BG;
                
                String piece;

                if(i == kingRow && j == kingCol)
                    piece = "WK";
                else if(i == pawnRow && j == pawnCol)
                    piece = "WP";
                else if(i == rookRow && j == rookCol)
                    piece = "WR";
                else if(i == bishopRow && j == bishopCol)
                    piece = "WB";
                else if(i == knightRow && j == knightCol)
                    piece = "WN";
                else if(i == queenRow && j == queenCol)
                    piece = "WQ";
                else if(i == BLACK_KING_ROW && j == BLACK_KING_COL)
                    piece = "BK";
                else
                    piece = "--";

                // Print the square with padding to make it look "chunky"
                System.out.print(background + " " + piece + " " + RESET);
            }
            System.out.println(); // New line after each row
        }
        System.out.println("  a   b   c   d   e   f   g   h");
        System.out.println(YELLOW_TEXT + "================================" + RESET);
    }
    
    public static void main(String args[]){
        CoolBoard myBoard = new CoolBoard(8,8);

        myBoard.showBoard();
    }
}
