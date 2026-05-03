// ChessBoardPanel
// Tyler Brochu

package ser120.TBchess.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import ser120.TBchess.game.MoveResult;
import ser120.TBchess.game.Piece;
import ser120.TBchess.models.ChessBoard;
import ser120.TBchess.saves.SaveData;
import ser120.TBchess.saves.SaveLoadGame;

public class ChessBoardPanel extends JPanel {
    private static final Color LIGHT_SQUARE = new Color(240, 217, 181);
    private static final Color DARK_SQUARE = new Color(181, 136, 99);
    private static final Color SELECTED_SQUARE = new Color(244, 208, 63);
    private static final String SAVE_FILE = "chess_save.txt";

    private final ChessBoard board;
    private final StatusPanel statusPanel;
    private final CapturedPiecesPanel capturedPiecesPanel;
    private final JButton[][] squares;
    private final SaveLoadGame saveLoad;
    private final List<String> moveHistory;

    private boolean whiteTurn;
    private int selectedRow;
    private int selectedCol;
    private boolean gameOver;
    private boolean replayInProgress;
    private Timer replayTimer;

    public ChessBoardPanel(StatusPanel statusPanel, CapturedPiecesPanel capturedPiecesPanel) {
        this.board = new ChessBoard();
        this.statusPanel = statusPanel;
        this.capturedPiecesPanel = capturedPiecesPanel;
        this.squares = new JButton[8][8];
        this.saveLoad = new SaveLoadGame();
        this.moveHistory = new ArrayList<>();
        this.whiteTurn = true;
        this.selectedRow = -1;
        this.selectedCol = -1;
        this.gameOver = false;
        this.replayInProgress = false;

        setLayout(new GridLayout(8, 8));
        setPreferredSize(new Dimension(640, 640));
        buildBoard();
        refreshBoard();
    }

    private void buildBoard() {
        Font pieceFont = new Font("SansSerif", Font.BOLD, 22);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton square = new JButton();
                square.setFont(pieceFont);
                square.setFocusPainted(false);
                square.setOpaque(true);
                square.setPreferredSize(new Dimension(80, 80));
                final int currentRow = row;
                final int currentCol = col;
                square.addActionListener(e -> handleSquareClick(currentRow, currentCol));
                squares[row][col] = square;
                add(square);
            }
        }
    }

    private void handleSquareClick(int row, int col) {
        if (replayInProgress) {
            statusPanel.setMessage("Replay is running. Wait for it to finish.");
            return;
        }

        if (gameOver) {
            statusPanel.setMessage("The game is over. Start a new game to play again.");
            return;
        }

        Piece clickedPiece = board.getPiece(row, col);

        if (selectedRow == -1) {
            if (clickedPiece == null) {
                statusPanel.setMessage("Select one of the current player's pieces.");
                return;
            }

            if (clickedPiece.isWhite() != whiteTurn) {
                statusPanel.setMessage("It is " + currentPlayerName() + "'s turn.");
                return;
            }

            selectedRow = row;
            selectedCol = col;
            statusPanel.setMessage("Selected " + clickedPiece.getSymbol() + " at " + toAlgebraic(row, col) + ".");
            refreshBoard();
            return;
        }

        if (selectedRow == row && selectedCol == col) {
            clearSelection();
            statusPanel.setMessage("Selection cleared.");
            refreshBoard();
            return;
        }

        if (clickedPiece != null && clickedPiece.isWhite() == whiteTurn) {
            selectedRow = row;
            selectedCol = col;
            statusPanel.setMessage("Selected " + clickedPiece.getSymbol() + " at " + toAlgebraic(row, col) + ".");
            refreshBoard();
            return;
        }

        MoveResult result = board.movePieceDetailed(selectedRow, selectedCol, row, col, whiteTurn);
        if (!result.isSuccess()) {
            statusPanel.setMessage("Illegal move: " + result.getMessage());
            refreshBoard();
            return;
        }

        String moveText = toAlgebraic(selectedRow, selectedCol) + " to " + toAlgebraic(row, col);
        moveHistory.add(toMoveNotation(selectedRow, selectedCol, row, col));
        finishMove(result, moveText, false);
        refreshBoard();
    }

    public void resetGame() {
        stopReplay();
        board.setupBoard();
        whiteTurn = true;
        selectedRow = -1;
        selectedCol = -1;
        gameOver = false;
        replayInProgress = false;
        moveHistory.clear();
        capturedPiecesPanel.clearCapturedPieces();
        statusPanel.setTurn(true);
        statusPanel.setMessage("New game started. White moves first.");
        refreshBoard();
    }

    public void loadGame() {
        stopReplay();
        SaveData data = saveLoad.loadGame(SAVE_FILE);
        if (data == null) {
            statusPanel.setMessage("Could not load " + SAVE_FILE + ".");
            return;
        }

        copyBoardState(data.getBoard());
        whiteTurn = data.isWhiteTurn();
        selectedRow = -1;
        selectedCol = -1;
        replayInProgress = false;
        gameOver = !board.hasKing(true) || !board.hasKing(false);

        moveHistory.clear();
        moveHistory.addAll(data.getMoveHistory());

        capturedPiecesPanel.clearCapturedPieces();
        rebuildCapturedPiecesFromHistory();
        statusPanel.setTurn(whiteTurn);
        if (board.isCheckmate(whiteTurn)) {
            gameOver = true;
            statusPanel.setMessage(currentPlayerName() + " is in checkmate.");
        } else if (board.isInCheck(whiteTurn)) {
            statusPanel.setMessage("Loaded game from " + SAVE_FILE + ". " + currentPlayerName() + " is in check.");
        } else {
            statusPanel.setMessage("Loaded game from " + SAVE_FILE + ".");
        }
        refreshBoard();
    }

    public void saveGame() {
        if (replayInProgress) {
            statusPanel.setMessage("Wait for replay to finish before saving.");
            return;
        }

        saveLoad.saveGame(board, new ArrayList<>(moveHistory), whiteTurn, SAVE_FILE);
        statusPanel.setMessage("Game saved to " + SAVE_FILE + ".");
    }

    public void replayGame() {
        stopReplay();
        if (moveHistory.isEmpty()) {
            statusPanel.setMessage("No moves available to replay.");
            return;
        }

        ArrayList<String> replayMoves = new ArrayList<>(moveHistory);
        board.setupBoard();
        capturedPiecesPanel.clearCapturedPieces();
        whiteTurn = true;
        selectedRow = -1;
        selectedCol = -1;
        gameOver = false;
        replayInProgress = true;
        statusPanel.setTurn(true);
        statusPanel.setMessage("Replay started.");
        refreshBoard();

        final int[] moveIndex = {0};
        replayTimer = new Timer(800, e -> {
            if (moveIndex[0] >= replayMoves.size()) {
                stopReplay();
                statusPanel.setTurn(whiteTurn);
                statusPanel.setMessage("Replay finished.");
                refreshBoard();
                return;
            }

            String move = replayMoves.get(moveIndex[0]);
            if (!applyMoveString(move, true)) {
                stopReplay();
                statusPanel.setMessage("Replay stopped because move data was invalid.");
                refreshBoard();
                return;
            }

            moveIndex[0]++;
            if (gameOver) {
                stopReplay();
            }
            refreshBoard();
        });
        replayTimer.setInitialDelay(0);
        replayTimer.start();
    }

    private void clearSelection() {
        selectedRow = -1;
        selectedCol = -1;
    }

    private void finishMove(MoveResult result, String moveText, boolean replayMode) {
        if (result.getCapturedPiece() != null) {
            capturedPiecesPanel.addCapturedPiece(result.getCapturedPiece().getSymbol(), whiteTurn);
        }

        String movingPlayer = currentPlayerName();
        clearSelection();

        whiteTurn = !whiteTurn;
        statusPanel.setTurn(whiteTurn);

        if (board.isCheckmate(whiteTurn)) {
            gameOver = true;
            statusPanel.setMessage("Checkmate: " + movingPlayer + " wins.");
            return;
        }

        if (board.isStalemate(whiteTurn)) {
            gameOver = true;
            statusPanel.setMessage("Stalemate: no legal moves remain.");
            return;
        }

        if (replayMode) {
            if (board.isInCheck(whiteTurn)) {
                statusPanel.setMessage("Replay move: " + moveText + ". " + currentPlayerName() + " is in check.");
            } else {
                statusPanel.setMessage("Replay move: " + moveText);
            }
        } else {
            if (board.isInCheck(whiteTurn)) {
                statusPanel.setMessage("Move completed: " + moveText + ". " + currentPlayerName() + " is in check.");
            } else {
                statusPanel.setMessage("Move completed: " + moveText);
            }
        }
    }

    private boolean applyMoveString(String move, boolean replayMode) {
        String[] parts = move.trim().split("\\s+");
        if (parts.length != 2) {
            return false;
        }

        int[] from = convert(parts[0]);
        int[] to = convert(parts[1]);
        if (from[0] == -1 || to[0] == -1) {
            return false;
        }

        MoveResult result = board.movePieceDetailed(from[0], from[1], to[0], to[1], whiteTurn);
        if (!result.isSuccess()) {
            return false;
        }

        finishMove(result, parts[0] + " to " + parts[1], replayMode);
        return true;
    }

    private void copyBoardState(ChessBoard sourceBoard) {
        board.clearBoard();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board.setPiece(row, col, sourceBoard.getPiece(row, col));
            }
        }
    }

    private void rebuildCapturedPiecesFromHistory() {
        ChessBoard replayBoard = new ChessBoard();
        boolean replayWhiteTurn = true;

        for (String move : moveHistory) {
            String[] parts = move.trim().split("\\s+");
            if (parts.length != 2) {
                return;
            }

            int[] from = convert(parts[0]);
            int[] to = convert(parts[1]);
            if (from[0] == -1 || to[0] == -1) {
                return;
            }

            MoveResult result = replayBoard.movePieceDetailed(from[0], from[1], to[0], to[1], replayWhiteTurn);
            if (!result.isSuccess()) {
                return;
            }

            if (result.getCapturedPiece() != null) {
                capturedPiecesPanel.addCapturedPiece(result.getCapturedPiece().getSymbol(), replayWhiteTurn);
            }

            replayWhiteTurn = !replayWhiteTurn;
        }
    }

    private int[] convert(String algebraic) {
        if (algebraic == null || algebraic.length() != 2) {
            return new int[] {-1, -1};
        }

        char file = Character.toLowerCase(algebraic.charAt(0));
        char rank = algebraic.charAt(1);
        if (file < 'a' || file > 'h' || rank < '1' || rank > '8') {
            return new int[] {-1, -1};
        }

        return new int[] {8 - (rank - '0'), file - 'a'};
    }

    private String toMoveNotation(int fromRow, int fromCol, int toRow, int toCol) {
        return toAlgebraic(fromRow, fromCol) + " " + toAlgebraic(toRow, toCol);
    }

    private void stopReplay() {
        replayInProgress = false;
        if (replayTimer != null) {
            replayTimer.stop();
            replayTimer = null;
        }
    }

    private void refreshBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton square = squares[row][col];
                Color squareColor = ((row + col) % 2 == 0) ? LIGHT_SQUARE : DARK_SQUARE;
                square.setBackground(squareColor);
                square.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                Piece piece = board.getPiece(row, col);
                square.setText(piece == null ? "" : piece.getSymbol());

                if (row == selectedRow && col == selectedCol) {
                    square.setBackground(SELECTED_SQUARE);
                    square.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                }
            }
        }
        repaint();
    }

    private String currentPlayerName() {
        return whiteTurn ? "White" : "Black";
    }

    private String toAlgebraic(int row, int col) {
        char file = (char) ('a' + col);
        int rank = 8 - row;
        return String.valueOf(file) + rank;
    }
}
