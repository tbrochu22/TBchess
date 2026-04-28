// App.java
// Tyler Brochu
package ser120.TBchess;

import ser120.TBchess.game.GameManager;

public class App {
    public static void main(String[] args) {
        System.out.println("--- Starting SER120 Chess App ---");
        
        GameManager gm = new GameManager();
        gm.runGame();
        
        System.out.println("--- Session Ended ---");
    }
}