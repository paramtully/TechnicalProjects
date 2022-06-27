package model;

import java.awt.*;
import java.util.Objects;

// represents a movable object
public abstract class Sprite {
    public static final int LEFT = -1, RIGHT = 1;

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int xDir;
    protected int dx;

    public Sprite(int x, int y, int width, int height, int dx) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dx = dx;
        xDir = RIGHT;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDx() {
        return dx;
    }

    public int getXDirection() {
        return xDir;
    }


    // MODIFIES: this
    // EFFECTS: moves sprite
    public void move() {
        x += dx * xDir;
        handleBoundary();
    }

    // MODIFIES: this
    // EFFECTS: handles boundary conditions of sprite
    private void handleBoundary() {
        if (x < 0) {
            x = 0;
        } else if (x > BBGame.WIDTH) {
            x = BBGame.WIDTH;
        }
    }

    // MODIFIES: this
    // EFFECTS: makes this face left direction
    public void faceLeft() {
        xDir = LEFT;
    }

    // MODIFIES: this
    // EFFECTS: make this face right direction
    public void faceRight() {
        xDir = RIGHT;
    }

    // EFFECTS: returns true if this has collided with other
    public boolean checkCollision(Sprite other) {
        Rectangle otherHitBox = new Rectangle(other.getX() - other.getWidth()/2,
                other.getY() - other.getHeight()/2, other.getWidth(), other.getHeight());
        Rectangle blockHitBox = new Rectangle(x - width/2, y - height/2, width, height);
        return blockHitBox.intersects(otherHitBox);
    }

    // MODIFIES: g
    // EFFECTS: draws this on g
    public abstract void draw(Graphics g);
}
