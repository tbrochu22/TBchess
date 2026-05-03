// MoveResult.java
// Tyler Brochu
package ser120.TBchess.game;

public class MoveResult {
    private final boolean success;
    private final String message;
    private final Piece capturedPiece;

    public MoveResult(boolean success, String message, Piece capturedPiece) {
        this.success = success;
        this.message = message;
        this.capturedPiece = capturedPiece;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }
}
