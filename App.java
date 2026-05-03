// App.java
// Tyler Brochu
package ser120.TBchess;

import javax.swing.SwingUtilities;

import ser120.TBchess.ui.ChessFrame;

public class App {
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            ChessFrame frame = new ChessFrame();
            frame.setVisible(true);
        });
    }
}
