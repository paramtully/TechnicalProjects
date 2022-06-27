package ui;

import model.BBGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BrickBreaker extends JFrame {
    private static final int INTERVAL = 20;

    private GamePanel gp;
    private ScorePanel sp;
    private BBGame game;
    private Timer t;

    // EFFECTS: sets up window in which BrickBreaker is played
    public BrickBreaker() {
        super("Brick Breaker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);

        game = new BBGame();
        gp = new GamePanel(game);
        sp = new ScorePanel();
        game.addObserver(sp);
        add(gp);
        add(sp, BorderLayout.NORTH);

        addKeyListener(new KeyHandler());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        addTimer();
        t.start();
    }

    // EFFECTS: initializes a timer and updates each INTERVAL milliseconds
    private void addTimer() {
        t = new Timer(INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.update();
                gp.repaint();
            }
        });
    }

    private class KeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            game.keyPressed(e.getKeyCode());
        }
    }

    public static void main(String[] args) {
        new BrickBreaker();
    }
}
