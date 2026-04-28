// SaveLoadGame.java
// Tyler Brochu
package ser120.TBchess.saves;

import ser120.TBchess.models.ChessBoard;
import ser120.TBchess.game.*;

import java.io.*;
import java.util.ArrayList;

public class SaveLoadGame {

    public void saveGame(ChessBoard board, ArrayList<String> moveHistory, boolean whiteTurn, String filename) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            out.println(whiteTurn);

            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    Piece p = board.getPiece(r, c);
                    if (p == null) {
                        out.println("null");
                    } else {
                        out.println(p.getClass().getSimpleName() + "," + r + "," + c + "," + p.isWhite());
                    }
                }
            }

            out.println("MOVES");
            for (String move : moveHistory) {
                out.println(move);
            }
        } catch (IOException e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    public SaveData loadGame(String filename) {
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            boolean whiteTurn = Boolean.parseBoolean(in.readLine());

            ChessBoard board = new ChessBoard();
            board.clearBoard();

            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    String line = in.readLine();
                    if (!line.equals("null")) {
                        String[] parts = line.split(",");
                        String type = parts[0];
                        int row = Integer.parseInt(parts[1]);
                        int col = Integer.parseInt(parts[2]);
                        boolean white = Boolean.parseBoolean(parts[3]);

                        Piece piece = createPiece(type, row, col, white);
                        board.setPiece(row, col, piece);
                    }
                }
            }

            ArrayList<String> moveHistory = new ArrayList<>();
            String marker = in.readLine();

            if ("MOVES".equals(marker)) {
                String line;
                while ((line = in.readLine()) != null) {
                    moveHistory.add(line);
                }
            }

            return new SaveData(board, moveHistory, whiteTurn);

        } catch (IOException e) {
            System.out.println("Load failed: " + e.getMessage());
            return null;
        }
    }

    private Piece createPiece(String type, int row, int col, boolean white) {
        switch (type) {
            case "King":
                return new King(row, col, white);
            case "Queen":
                return new Queen(row, col, white);
            case "Rook":
                return new Rook(row, col, white);
            case "Bishop":
                return new Bishop(row, col, white);
            case "Knight":
                return new Knight(row, col, white);
            case "Pawn":
                return new Pawn(row, col, white);
            default:
                return null;
        }
    }
}