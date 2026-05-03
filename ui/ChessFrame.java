// ChessFrame
// Tyler Brochu

package ser120.TBchess.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ChessFrame extends JFrame {
    public ChessFrame() {
        super("SER 120 Final Project - Chess GUI");

        StatusPanel statusPanel = new StatusPanel();
        CapturedPiecesPanel capturedPiecesPanel = new CapturedPiecesPanel();
        ChessBoardPanel boardPanel = new ChessBoardPanel(statusPanel, capturedPiecesPanel);

        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(new Color(210, 199, 182));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));

        JButton newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        newGameButton.addActionListener(e -> boardPanel.resetGame());
        topPanel.add(newGameButton);

        JButton loadButton = new JButton("Load");
        loadButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        loadButton.addActionListener(e -> boardPanel.loadGame());
        topPanel.add(loadButton);

        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveButton.addActionListener(e -> boardPanel.saveGame());
        topPanel.add(saveButton);

        JButton replayButton = new JButton("Replay");
        replayButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        replayButton.addActionListener(e -> boardPanel.replayGame());
        topPanel.add(replayButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        exitButton.addActionListener(e -> dispose());
        topPanel.add(exitButton);

        add(topPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(capturedPiecesPanel, BorderLayout.EAST);
        add(statusPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }
}
