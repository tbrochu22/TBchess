// CapturedPiecesPanel
// Tyler Brochu

package ser120.TBchess.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class CapturedPiecesPanel extends JPanel {
    private final List<String> capturedByWhite;
    private final List<String> capturedByBlack;
    private final JTextArea whiteArea;
    private final JTextArea blackArea;

    public CapturedPiecesPanel() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(244, 236, 220));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(122, 94, 66), 2),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        capturedByWhite = new ArrayList<>();
        capturedByBlack = new ArrayList<>();

        JLabel title = new JLabel("Captured Pieces");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 10));
        content.setOpaque(false);

        whiteArea = buildArea();
        blackArea = buildArea();

        content.add(buildSection("Captured by White", whiteArea), BorderLayout.NORTH);
        content.add(buildSection("Captured by Black", blackArea), BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        refreshText();
    }

    private JPanel buildSection(String title, JTextArea area) {
        JPanel section = new JPanel(new BorderLayout(0, 6));
        section.setOpaque(false);

        JLabel label = new JLabel(title);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        section.add(label, BorderLayout.NORTH);
        section.add(area, BorderLayout.CENTER);

        return section;
    }

    private JTextArea buildArea() {
        JTextArea area = new JTextArea(7, 14);
        area.setEditable(false);
        area.setFocusable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Monospaced", Font.BOLD, 14));
        area.setBackground(new Color(255, 250, 242));
        area.setBorder(BorderFactory.createLineBorder(new Color(165, 139, 110), 1));
        return area;
    }

    public void addCapturedPiece(String pieceSymbol, boolean capturedByWhiteTurn) {
        if (capturedByWhiteTurn) {
            capturedByWhite.add(pieceSymbol);
        } else {
            capturedByBlack.add(pieceSymbol);
        }
        refreshText();
    }

    public void clearCapturedPieces() {
        capturedByWhite.clear();
        capturedByBlack.clear();
        refreshText();
    }

    private void refreshText() {
        whiteArea.setText(formatPieces(capturedByWhite));
        blackArea.setText(formatPieces(capturedByBlack));
    }

    private String formatPieces(List<String> pieces) {
        if (pieces.isEmpty()) {
            return "None";
        }
        return String.join("  ", pieces);
    }
}
