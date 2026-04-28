import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

/** Entry point: opens a window containing the chess board demo. */
public class ChessDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Demo for Chess");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            //frame.setContentPane(new ChessBoardPanel());
            frame.setLayout(new BorderLayout());
            StatusPanel sp = new StatusPanel();
            frame.add(new ChessBoardPanel(sp),  BorderLayout.CENTER);

            frame.add(sp, BorderLayout.SOUTH);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
