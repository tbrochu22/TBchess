// ReplayManager.java
// Tyler Brochu
package ser120.TBchess.saves;

import ser120.TBchess.game.GameManager;
import java.util.List;

public class ReplayManager {

    public static void replay(List<String> moves) {
        GameManager replayGame = new GameManager();

        System.out.println("Replaying game...");
        for (String move : moves) {
            try {
                // pause the currently running thread for 1 second
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println("Move: " + move);
            replayGame.processReplayMove(move);
        }
    }
}