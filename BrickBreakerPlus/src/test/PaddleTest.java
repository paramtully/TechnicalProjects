package test;

import model.Paddle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaddleTest {
    private Paddle paddle;
    private int x;
    private int y;

    @BeforeEach
    public void setup() {
        x = 20;
        y = 30;
        paddle = new Paddle(x, y);
    }

    @Test
    public void testConstructor() {
        assertEquals(x, paddle.getX());
        assertEquals(y, paddle.getY());
        assertEquals(Paddle.INITIAL_WIDTH, paddle.getWidth());
        assertEquals(Paddle.HEIGHT, paddle.getHeight());
        assertEquals(Paddle.DX, paddle.getDx());
        assertTrue(paddle.isFacingRight());
    }

    @Test
    public void testChangeWidthIncrease() {
        assertEquals(Paddle.INITIAL_WIDTH, paddle.getWidth());
        paddle.changeWidth(2);
        assertEquals(2 * Paddle.INITIAL_WIDTH, paddle.getWidth());
    }

    @Test
    public void testChangeWidthAtLease1() {
        paddle.changeWidth(0.000001);
        assertEquals(1, paddle.getWidth());
    }
}
