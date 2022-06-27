package model;

import java.awt.*;
import java.util.Random;

import static model.Bonus.BONUS_SIZE;

// a spite that when collected grants a power up
public class PowerUp extends Sprite {
    public static final int RADIUS = 5;
    public static final int DY = 3;
    public static final int DX = 0;
    public static final int NUM_COLORS = 4;
    public static final int PROB_COLOR_CHANGE = 5;
    private static final Random RND = new Random();

    private Bonus bonus;

    // EFFECTS: initializes this with position and dimensions
    public PowerUp(int x, int y) {
        super(x, y, 2*RADIUS, 2*RADIUS, DX);
        bonus = initializeBonus();
    }

    // MODIFIES: this
    // EFFECTS: assigns this a random bonus from select bonuses
    private Bonus initializeBonus() {
        switch (new Random().nextInt(BONUS_SIZE)) {
            case 0: return Bonus.MULTIBALL;
            case 1: return Bonus.WIDEPADDLE;
            default: return Bonus.SLOWBALL;
        }
    }

    @Override
    public void move() {
        super.move();
        y += DY;
    }

    // MODIFIES: this
    // EFFECTS: returns bonus type
    public Bonus getBonus() {
        return bonus;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        if (RND.nextInt(PROB_COLOR_CHANGE) == 0) {
            setGraphicColor(g);
        }
        g.fillOval(x - width/2, y - height/2, width, height);
    }

    private void setGraphicColor(Graphics g) {
        switch (RND.nextInt(NUM_COLORS)) {
            case 0:
                g.setColor(Color.CYAN);
                break;
            case 1:
                g.setColor(Color.GREEN);
                break;
            case 2:
                g.setColor(Color.BLUE);
                break;
            default:
                g.setColor(Color.YELLOW);
                break;
        }
    }
}
