// GameManager.java
// Tyler Brochu
package ser120.TBchess.game;

import ser120.TBchess.models.ChessBoard;
import ser120.TBchess.saves.SaveData;
import ser120.TBchess.saves.SaveLoadGame;
import ser120.TBchess.saves.ReplayManager;

import java.util.ArrayList;
import java.util.Scanner;

public class GameManager {
    private ChessBoard board;
    private SaveLoadGame saveLoad;
    private ArrayList<String> moveHistory;
    private boolean whiteTurn;

    public GameManager() {
        board = new ChessBoard();
        saveLoad = new SaveLoadGame();
        moveHistory = new ArrayList<>();
        whiteTurn = true;
    }

    public void runGame() {
        Scanner in = new Scanner(System.in);
        boolean running = true;

        while (running) {
            board.displayBoard();
            if(whiteTurn)
                System.out.print("White");
            else 
                System.out.print("Black");
            System.out.println(" to move");
            System.out.println("Enter move as: e2 e4");
            System.out.println("Commands: save, load, replay, exit");

            String input = in.nextLine().trim().toLowerCase();

            if (input.equals("exit")) {
                running = false;
            } else if (input.equals("save")) {
                saveLoad.saveGame(board, moveHistory, whiteTurn, "chess_save.txt");
                System.out.println("Game saved.");
            } else if (input.equals("load")) {
                SaveData data = saveLoad.loadGame("chess_save.txt");
                if (data != null) {
                    board = data.getBoard();
                    moveHistory = data.getMoveHistory();
                    whiteTurn = data.isWhiteTurn();
                    System.out.println("Game loaded.");
                }
            } else if (input.equals("replay")) {
                ReplayManager.replay(moveHistory);
            } else {
                if (processMove(input)) {
                    if (!board.hasKing(true)) {
                        board.displayBoard();
                        System.out.println("Black wins.");
                        running = false;
                    } else if (!board.hasKing(false)) {
                        board.displayBoard();
                        System.out.println("White wins.");
                        running = false;
                    } else {
                        whiteTurn = !whiteTurn;
                    }
                } else {
                    System.out.println("Invalid move.");
                }
            }
        }

        in.close();
    }

    private boolean processMove(String input) {
        String[] parts = input.split("\\s+");
        if (parts.length != 2) return false;

        int[] from = convert(parts[0]);
        int[] to = convert(parts[1]);

        if (from[0] == -1 || to[0] == -1)
         return false;

        boolean moved = board.movePiece(from[0], from[1], to[0], to[1], whiteTurn);
        if(moved) {
            moveHistory.add(input);
        }
        return moved;
    }

    public void processReplayMove(String input) {
        processMove(input);
        board.displayBoard();
        whiteTurn = !whiteTurn;
    }

    private int[] convert(String alg) {
        if (alg == null || alg.length() != 2) 
            return new int[]{-1, -1};

        char file = alg.charAt(0);
        char rank = alg.charAt(1);

        if (file < 'a' || file > 'h' || rank < '1' || rank > '8') {
            return new int[]{-1, -1};
        }

        int col = file - 'a';
        int row = 8 - (rank - '0');

        return new int[]{row, col};
    }
}