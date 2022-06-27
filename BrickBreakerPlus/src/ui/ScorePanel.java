package ui;

import model.BBGame;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class ScorePanel extends JPanel implements Observer {
    private static final String DESTROYED_TXT = "Blocks Destroyed: ";
    private static final String REMAINING_TXT = "Blocks Remaining: ";
    private static final String REMAINING_BALLS = "Balls Remaining: ";
    private static final int LBL_WIDTH = 200;
    private static final int LBL_HEIGHT = 30;
    private static final Color COLOR = Color.DARK_GRAY;

    private JLabel destroyedLabel;
    private JLabel remainingLabel;
    private JLabel remainingBallsLabel;

    // EFFECTS: sets background color and draws initial labels
    public ScorePanel() {
        setBackground(COLOR);
        remainingLabel = new JLabel(REMAINING_TXT + 0);
        remainingLabel.setPreferredSize(new Dimension(LBL_WIDTH, LBL_HEIGHT));
        destroyedLabel = new JLabel(DESTROYED_TXT + BBGame.COLS * BBGame.ROWS);
        destroyedLabel.setPreferredSize(new Dimension(LBL_WIDTH, LBL_HEIGHT));
        remainingBallsLabel = new JLabel(REMAINING_BALLS + 1);
        remainingBallsLabel.setPreferredSize(new Dimension(LBL_WIDTH, LBL_HEIGHT));
        add(remainingBallsLabel);
        add(remainingLabel);
        add(destroyedLabel);
    }

    @Override
    public void update(Observable o, Object arg) {
        int numBricksDestroyed = ((BBGame) o).getNumBricksDestroyed();
        remainingLabel.setText(REMAINING_TXT + (BBGame.NUM_BRICKS - numBricksDestroyed));
        destroyedLabel.setText(DESTROYED_TXT + numBricksDestroyed);
        remainingBallsLabel.setText(REMAINING_BALLS + ((BBGame) o).getNumBalls());
    }
}
