import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 * Swing component that draws an 8x8 chess board, handles click-to-select
 * and click-to-move, and delegates move legality to ChessRules.
 *
 * Interaction state machine:
 *   IDLE        -> click occupied square  -> SELECTED(from)
 *   SELECTED    -> click same square      -> IDLE
 *   SELECTED    -> click other square     -> rules.isLegalMove ?
 *                                              move + IDLE
 *                                            : reselect or stay
 */
public class ChessBoardPanel extends JPanel {

    private static final int N = 8;
    private static final int SQUARE = 72;

    private static final Color LIGHT   = new Color(0xE8C08C);
    private static final Color DARK    = new Color(0x8B5A2B);
    private static final Color SELECT  = new Color(0x00E5FF);      // neon cyan
    private static final Color HOVER   = new Color(0xFF, 0xEB, 0x3B, 160); // translucent amber

    private final Piece[][] board = new Piece[N][N];
    private final ChessRules rules = new ChessRules();
    private final PixelSpriteRenderer sprites = new PixelSpriteRenderer();

    // board[row][col]: row 0 = top (black's back rank).
    private int selRow = -1, selCol = -1;
    private int hoverRow = -1, hoverCol = -1;

    private Piece.Color turn = Piece.Color.WHITE;   // White always moves first
    private StatusPanel statusPanel;
    public ChessBoardPanel(StatusPanel stat) {
        setPreferredSize(new Dimension(N * SQUARE, N * SQUARE));
        setBackground(new Color(0x1A1A24));
        setupStartingPosition();
        statusPanel = stat;
        MouseAdapter m = new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { handleClick(e.getX(), e.getY()); }
            @Override public void mouseMoved(MouseEvent e)   { handleHover(e.getX(), e.getY()); }
            @Override public void mouseExited(MouseEvent e)  { hoverRow = hoverCol = -1; repaint(); }
        };
        addMouseListener(m);
        addMouseMotionListener(m);
    }

    private void setupStartingPosition() {
        Piece.Type[] back = {
            Piece.Type.ROOK, Piece.Type.KNIGHT, Piece.Type.BISHOP, Piece.Type.QUEEN,
            Piece.Type.KING, Piece.Type.BISHOP, Piece.Type.KNIGHT, Piece.Type.ROOK
        };
        for (int c = 0; c < N; c++) {
            board[0][c] = new Piece(back[c],          Piece.Color.BLACK);
            board[1][c] = new Piece(Piece.Type.PAWN,  Piece.Color.BLACK);
            board[6][c] = new Piece(Piece.Type.PAWN,  Piece.Color.WHITE);
            board[7][c] = new Piece(back[c],          Piece.Color.WHITE);
        }
    }

    private void handleHover(int x, int y) {
        int col = x / SQUARE, row = y / SQUARE;
        if (row < 0 || row >= N || col < 0 || col >= N) { hoverRow = hoverCol = -1; }
        else { hoverRow = row; hoverCol = col; }
        repaint();
    }

    private void handleClick(int x, int y) {
        int col = x / SQUARE, row = y / SQUARE;
        if (row < 0 || row >= N || col < 0 || col >= N) return;

        if (selRow < 0) {
            // IDLE -> SELECTED (only if the square has a piece)
            if (board[row][col] != null) { selRow = row; selCol = col; }

        } else if (row == selRow && col == selCol) {
            // deselect
            selRow = selCol = -1;
        } else if (rules.isLegalMove(board, selRow, selCol, row, col)) {
            // MOVE

            board[row][col] = board[selRow][selCol];
            board[selRow][selCol] = null;
            selRow = selCol = -1;
            turn = (turn == Piece.Color.WHITE) ? Piece.Color.BLACK : Piece.Color.WHITE;
            statusPanel.setTurn(turn);
            statusPanel.setMessage("messy marvin");
        } else if (board[row][col] != null
                && board[row][col].getColor() == board[selRow][selCol].getColor()) {
            // switch selection to another own piece
            selRow = row; selCol = col;
        }
        // else: illegal target, keep selection
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        // crisp pixels, no smoothing — arcade look.
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                int x = c * SQUARE, y = r * SQUARE;
                g.setColor(((r + c) & 1) == 0 ? LIGHT : DARK);
                g.fillRect(x, y, SQUARE, SQUARE);
                if (r == hoverRow && c == hoverCol && !(r == selRow && c == selCol)) {
                    g.setColor(HOVER);
                    g.fillRect(x, y, SQUARE, SQUARE);
                }
                if (board[r][c] != null) {
                    sprites.draw(g, board[r][c], x, y, SQUARE);
                }
            }
        }
        if (selRow >= 0) {
            g.setColor(SELECT);
            g.setStroke(new BasicStroke(4f));
            g.drawRect(selCol * SQUARE + 2, selRow * SQUARE + 2, SQUARE - 4, SQUARE - 4);
        }
        g.dispose();
    }
}
