package ui;

import model.BBGame;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private static final Color FONT_COLOR = new Color(166, 15, 26);
    private static final String OVER = "Game Over!";
    private static final String WON = "You Won!";
    private static final String LOST = "You Lost!";
    private static final String REPLAY = "R to replay";

    private BBGame game;

    // EFFECTS: sets size and background color of panel
    public GamePanel(BBGame game) {
        setPreferredSize(new Dimension(BBGame.WIDTH, BBGame.HEIGHT));
        setBackground(Color.DARK_GRAY);
        this.game = game;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGame(g);
        if (game.isGameOver()) {
            gameOver(g);
        }
    }

    // MODIFIES: g
    // EFFECTS: draws game over and replays instructions onto g
    private void gameOver(Graphics g) {
        g.setFont(new Font("Arial", 20, 30));
        FontMetrics fm = g.getFontMetrics();
        centreString(OVER, g, fm, BBGame.HEIGHT/2 - 50);

        if (game.isGameWon()) {
            centreString(WON, g, fm, BBGame.HEIGHT/2);
        } else {
            centreString(LOST, g, fm, BBGame.HEIGHT/2);
        }
        centreString(REPLAY, g, fm, BBGame.HEIGHT/2 + 50);
    }

    // MODIFIES: g
    // EFFECTS: draws game over and replays instructions onto g
    private void centreString(String str, Graphics g, FontMetrics fm, int yPos) {
        int width = fm.stringWidth(str);
        g.setColor(FONT_COLOR);
        g.drawString(str, (BBGame.WIDTH - width)/2, yPos);
    }

    // MODIFIES: g
    // EFFECTS: game is drawn onto g
    private void drawGame(Graphics g) {
        game.draw(g);
    }
}
