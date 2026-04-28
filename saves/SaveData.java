// SaveData.java
// Tyler Brochu
package ser120.TBchess.saves;

import ser120.TBchess.models.ChessBoard;
import java.util.ArrayList;

public class SaveData {
    private ChessBoard board;
    private ArrayList<String> moveHistory;
    private boolean whiteTurn;

    public SaveData(ChessBoard board, ArrayList<String> moveHistory, boolean whiteTurn) {
        this.board = board;
        this.moveHistory = moveHistory;
        this.whiteTurn = whiteTurn;
    }

    public ChessBoard getBoard() {
        return board;
    }

    public ArrayList<String> getMoveHistory() {
        return moveHistory;
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }
}