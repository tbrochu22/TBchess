import java.awt.Color;
import java.awt.Graphics2D;
import java.util.EnumMap;
import java.util.Map;

/**
 * Renders chess pieces as 8x8 pixel sprites in an 80s arcade block style.
 * Each sprite is a 1-bit bitmap; the renderer derives a dark outline
 * automatically and lights the top row as a highlight.
 */
public class PixelSpriteRenderer {

    private static final int GRID = 8;

    // Retro palette.
    private static final Color WHITE_BODY    = new Color(0xF5F5DC); // cream
    private static final Color WHITE_HILITE  = new Color(0xFFFFFF);
    private static final Color BLACK_BODY    = new Color(0xE53935); // arcade red
    private static final Color BLACK_HILITE  = new Color(0xFFD54F); // amber
    private static final Color OUTLINE       = new Color(0x101018); // near-black

    private static final Map<Piece.Type, String[]> SPRITES = new EnumMap<>(Piece.Type.class);
    static {
        SPRITES.put(Piece.Type.PAWN, new String[] {
            "........",
            "...##...",
            "..####..",
            "...##...",
            "..####..",
            ".######.",
            "########",
            "########",
        });
        SPRITES.put(Piece.Type.ROOK, new String[] {
            "#.#.#.#.",
            "########",
            ".######.",
            ".######.",
            ".######.",
            ".######.",
            "########",
            "########",
        });
        SPRITES.put(Piece.Type.KNIGHT, new String[] {
            "..####..",
            ".######.",
            "##.####.",
            "##.####.",
            "..######",
            "...#####",
            "..######",
            "########",
        });
        SPRITES.put(Piece.Type.BISHOP, new String[] {
            "...##...",
            "..####..",
            "..####..",
            "...##...",
            "..####..",
            ".######.",
            "########",
            "########",
        });
        SPRITES.put(Piece.Type.QUEEN, new String[] {
            "#.#.#.#.",
            "########",
            ".######.",
            ".######.",
            "..####..",
            "..####..",
            ".######.",
            "########",
        });
        SPRITES.put(Piece.Type.KING, new String[] {
            "...##...",
            ".######.",
            "###..###",
            "########",
            ".######.",
            "..####..",
            ".######.",
            "########",
        });
    }

    /** Draw the given piece filling the square at (x,y) with side pixels wide. */
    public void draw(Graphics2D g, Piece piece, int x, int y, int size) {
        String[] bits = SPRITES.get(piece.getType());
        if (bits == null) return;

        // Inset so pieces don't touch the square edge.
        int inset = Math.max(2, size / 10);
        int spriteSize = size - inset * 2;
        int px = spriteSize / GRID;              // size of one "big pixel"
        int ox = x + inset + (spriteSize - px * GRID) / 2;
        int oy = y + inset + (spriteSize - px * GRID) / 2;

        Color body   = piece.getColor() == Piece.Color.WHITE ? WHITE_BODY   : BLACK_BODY;
        Color hilite = piece.getColor() == Piece.Color.WHITE ? WHITE_HILITE : BLACK_HILITE;

        // Pass 1: outline — every empty cell touching a body cell.
        g.setColor(OUTLINE);
        for (int r = 0; r < GRID; r++) {
            for (int c = 0; c < GRID; c++) {
                if (bits[r].charAt(c) == '#') continue;
                if (touchesBody(bits, r, c)) {
                    g.fillRect(ox + c * px, oy + r * px, px, px);
                }
            }
        }
        // Pass 2: body + highlight top of each column-run.
        for (int r = 0; r < GRID; r++) {
            for (int c = 0; c < GRID; c++) {
                if (bits[r].charAt(c) != '#') continue;
                boolean top = (r == 0) || bits[r - 1].charAt(c) != '#';
                g.setColor(top ? hilite : body);
                g.fillRect(ox + c * px, oy + r * px, px, px);
            }
        }
    }

    private static boolean touchesBody(String[] bits, int r, int c) {
        int[][] n = {{-1,0},{1,0},{0,-1},{0,1}};
        for (int[] d : n) {
            int rr = r + d[0], cc = c + d[1];
            if (rr < 0 || rr >= GRID || cc < 0 || cc >= GRID) continue;
            if (bits[rr].charAt(cc) == '#') return true;
        }
        return false;
    }
}
