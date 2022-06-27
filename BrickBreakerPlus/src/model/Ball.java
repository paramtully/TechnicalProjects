package model;

import java.awt.*;
import java.util.Random;

import static java.lang.Math.abs;

// represents a ball
public class Ball extends Sprite {
    public static final int INITIAL_DX = 2;
    public static final int INITIAL_DY = 2;
    public static final int RADIUS = 7;
    public static final Random RND = new Random();
    public static final int MAX_DX = 10;
    public static final Color COLOR = Color.ORANGE;

    public static final int UP = -1, DOWN = 1;

    private int yDir;
    private int dy;

    // EFFECTS: constructs ball with initial position above paddle
    public Ball(int x, int y) {
        super(x, y,
                2*RADIUS, 2*RADIUS, INITIAL_DX);
        yDir = UP;
        dy = INITIAL_DY;
    }

    public int getDy() {
        return dy;
    }

    public int getYDirection() {
        return yDir;
    }

    @Override
    public void move() {
        super.move();
        y += dy * yDir;

        if (x <= 0 || x >= BBGame.WIDTH) {
            handleXBoundaryCollision();
        }

        if (y <= 0) {
            y = 0;
            handleYBoundaryCollision();
        }
    }

    // MODIFIES: this
    // EFFECTS: changes x direction of ball, assigns random dx
    private void handleXBoundaryCollision() {
        if (xDir == LEFT)  {
            faceRight();
        } else {
            faceLeft();
        }
    }

    // MODIFIES: this
    // EFFECTS: changes y direction of ball
    private void handleYBoundaryCollision() {
        if (yDir == UP) {
            faceDown();
        } else {
            faceUp();
        }
    }

    // EFFECTS: makes this face up
    public void faceUp() {
        yDir = UP;
    }

    // EFFECTS: makes this face down
    public void faceDown() {
        yDir = DOWN;
    }

    // MODIFIES: this
    // EFFECTS: changes y direction of ball to up, and assigns random dx in [1, MAX_DX]
    public void handlePaddleCollision() {
        faceUp();
        dx = RND.nextInt(MAX_DX) + 1;
    }

    // MODIFIES: this
    // EFFECTS: changes direction this is facing on collision with brick
    public void handleBrickCollision(Brick brick) {
        int xPosDifference = abs(x - brick.getX());
        int yPosDifference = abs(y - brick.getY());

        if (xPosDifference > yPosDifference) {
            handleXBoundaryCollision();
        } else {
            handleYBoundaryCollision();
        }
    }

    // MODIFIES: this
    // EFFECTS: multiplies speed by multipliers, dy >= 1
    public void changeDY(double multiplier) {
        dy = (int)Math.max(dy * multiplier, 1);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(COLOR);
        g.fillOval(x - width/2, y - height/2, width, height);
    }


}
