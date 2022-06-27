package model;

import java.awt.*;

// represents a brick
public class Brick extends Sprite {
    public static final int DX = 0;
    public static final int SIDE_LENGTH = 40;
    public static final Color COLOR = Color.white;


    public Brick(int x, int y) {
        super(x, y, SIDE_LENGTH, SIDE_LENGTH, DX);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(COLOR);
        g.fillRect(x - width/2, y - (height - 1)/2, width - 1, height - 1);
    }
}
