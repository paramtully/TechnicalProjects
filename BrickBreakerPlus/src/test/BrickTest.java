package test;

import model.Ball;
import model.Brick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BrickTest {
    private Brick brick;
    private int x;
    private int y;

    @BeforeEach
    public void setup() {
        x = 20;
        y = 30;
        brick = new Brick(x, y);
    }

    @Test
    public void testConstructor() {
        assertEquals(x, brick.getX());
        assertEquals(y, brick.getY());
        assertEquals(Brick.SIDE_LENGTH, brick.getWidth());
        assertEquals(Brick.SIDE_LENGTH, brick.getHeight());
        assertEquals(Brick.DX, brick.getDx());
    }

    @Test
    public void testCollision() {
        Ball b1 = new Ball(x, y);
        assertTrue(brick.checkCollision(b1));

        Ball b2 = new Ball(x - brick.getWidth()/2, y - brick.getHeight()/2);
        assertTrue(brick.checkCollision(b2));

        Ball b3 = new Ball(x + brick.getWidth()/2, y + brick.getHeight()/2);
        assertTrue(brick.checkCollision(b3));

        Ball b4 = new Ball(x, y + brick.getHeight());
        assertFalse(brick.checkCollision(b4));

        Ball b5 = new Ball(x + brick.getWidth(), y);
        assertFalse(brick.checkCollision(b5));
    }
}
