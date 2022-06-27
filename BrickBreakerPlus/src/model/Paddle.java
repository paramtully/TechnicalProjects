package model;

import java.awt.*;

// represents a paddle that hits a ball
public class Paddle extends Sprite {
    public static final int INITIAL_WIDTH = 45;
    public static final int HEIGHT = 8;
    public static final int DX = 6;
    public static final Color COLOR = Color.CYAN;

    // EFFECTS: constructs this at position (x, y), facing right
    public Paddle(int x, int y) {
        super(x, y, INITIAL_WIDTH, HEIGHT, DX);
    }

    // EFFECTS: returns true if this facing right
    public boolean isFacingRight() {
        return xDir == RIGHT;
    }

    // MODIFIES: this
    // EFFECTS: multiplies width of paddle by factor; width >= 1
    public void changeWidth(double multiplier) {
        width = (int) Math.max(width*multiplier, 1);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(COLOR);
        g.fillRect(x - width/2, y - height/2, width, height);
    }
}
