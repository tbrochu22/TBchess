// StatusPanel
// Tyler Brochu

package ser120.TBchess.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusPanel extends JPanel {
    private final JLabel turnLabel;
    private final JLabel messageLabel;

    public StatusPanel() {
        setLayout(new BorderLayout(12, 0));
        setBackground(new Color(27, 31, 35));
        setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        turnLabel = buildLabel();
        messageLabel = buildLabel();
        messageLabel.setHorizontalAlignment(JLabel.RIGHT);

        add(turnLabel, BorderLayout.WEST);
        add(messageLabel, BorderLayout.CENTER);

        setTurn(true);
        setMessage("Select a piece, then click where you want to move it.");
    }

    private JLabel buildLabel() {
        JLabel label = new JLabel();
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        return label;
    }

    public void setTurn(boolean whiteTurn) {
        turnLabel.setText("Turn: " + (whiteTurn ? "White" : "Black"));
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}
