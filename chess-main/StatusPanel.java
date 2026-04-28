import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class StatusPanel extends JPanel {

    private String left  = "TURN: WHITE";
    private String messy = "";

    public StatusPanel() {
        setPreferredSize(new Dimension(0, 32));
        setBackground(new Color(0x0C0C14));
    }

    public void setTurn(Piece.Color turn)   { left  = "TURN: " + turn.name(); repaint(); }
    public void setMessage(String message)  { messy = message == null ? "" : message; repaint(); }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        g.setFont(new Font("Monospaced", Font.BOLD, 14));

        g.setColor(new Color(0xE8C08C));
        g.drawString(left, 12, 21);

        g.setColor(new Color(0xFF3860));
        java.awt.FontMetrics fm = g.getFontMetrics();
        g.drawString(messy, getWidth() - fm.stringWidth(messy) - 12, 21);
        g.dispose();
    }
}
