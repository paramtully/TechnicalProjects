package test;

import model.BBGame;
import model.Ball;
import model.Brick;
import model.Sprite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BallTest {
    private Ball ball;
    private int x;
    private int y;

    @BeforeEach
    public void setup() {
        x = 20;
        y = 30;
        ball = new Ball(x, y);
    }

    @Test
    public void testConstructor() {
        assertEquals(x, ball.getX());
        assertEquals(y, ball.getY());
        assertEquals(Ball.INITIAL_DX, ball.getDx());
        assertEquals(Ball.INITIAL_DY, ball.getDy());
        assertEquals(2 * Ball.RADIUS, ball.getWidth());
        assertEquals(2 * Ball.RADIUS, ball.getHeight());
        assertEquals(Sprite.RIGHT, ball.getXDirection());
        assertEquals(Ball.UP, ball.getYDirection());
    }

    @Test
    public void testMoveUpRight() {
        ball.move();

        assertEquals(x + ball.getDx(), ball.getX());
        assertEquals(y - ball.getDy(), ball.getY());
    }

    @Test
    public void testMoveUpLeft() {
        ball.faceLeft();
        ball.move();

        assertEquals(x - ball.getDx(), ball.getX());
        assertEquals(y - ball.getDy(), ball.getY());
    }

    @Test
    public void testMoveDownRight() {
        ball.faceDown();
        ball.move();

        assertEquals(x + ball.getDx(), ball.getX());
        assertEquals(y + ball.getDy(), ball.getY());
    }

    @Test
    public void testMoveDownLeft() {
        ball.faceDown();
        ball.faceLeft();
        ball.move();

        assertEquals(x - ball.getDx(), ball.getX());
        assertEquals(y + ball.getDy(), ball.getY());
    }

    @Test
    public void testXBoundaryCollisionLeftBoundary() {
        Ball b1 = new Ball(1, y);
        b1.faceLeft();
        b1.move();

        assertEquals(0, b1.getX());
        assertEquals(Ball.RIGHT, b1.getXDirection());
        assertEquals(y - b1.getDy(), b1.getY());
        assertEquals(Ball.UP, b1.getYDirection());
    }

    @Test
    public void testXBoundaryCollisionLeftBoundaryCornerCase() {
        Ball b1 = new Ball(Ball.INITIAL_DX, y);
        b1.faceLeft();
        b1.move();

        assertEquals(0, b1.getX());
        assertEquals(Ball.RIGHT, b1.getXDirection());
        assertEquals(y - b1.getDy(), b1.getY());
        assertEquals(Ball.UP, b1.getYDirection());
    }

    @Test
    public void testXBoundaryCollisionRightBoundary() {
        Ball b1 = new Ball(BBGame.WIDTH - 1, y);
        b1.move();

        assertEquals(BBGame.WIDTH, b1.getX());
        assertEquals(Ball.LEFT, b1.getXDirection());
        assertEquals(y - b1.getDy(), b1.getY());
        assertEquals(Ball.UP, b1.getYDirection());
    }

    @Test
    public void testXBoundaryCollisionRightBoundaryCornerCase() {
        Ball b1 = new Ball(BBGame.WIDTH - Ball.INITIAL_DX, y);
        b1.move();

        assertEquals(BBGame.WIDTH, b1.getX());
        assertEquals(Ball.LEFT, b1.getXDirection());
        assertEquals(y - b1.getDy(), b1.getY());
        assertEquals(Ball.UP, b1.getYDirection());
    }

    @Test
    public void testYBoundaryCollisionTopBoundary() {
        Ball b1 = new Ball(x, 1);
        b1.move();

        assertEquals(x + b1.getDx(), b1.getX());
        assertEquals(Ball.RIGHT, b1.getXDirection());
        assertEquals(0, b1.getY());
        assertEquals(Ball.DOWN, b1.getYDirection());
    }

    @Test
    public void testYBoundaryCollisionTopBoundaryCornerCase() {
        Ball b1 = new Ball(x, Ball.INITIAL_DY);
        b1.move();

        assertEquals(x + b1.getDx(), b1.getX());
        assertEquals(Ball.RIGHT, b1.getXDirection());
        assertEquals(0, b1.getY());
        assertEquals(Ball.DOWN, b1.getYDirection());
    }

    @Test
    public void testHandlePaddleCollision() {
        List<Integer> foundDXs = new ArrayList<>(Collections.nCopies(Ball.MAX_DX, 0));
        for (int i = 0; i < Ball.INITIAL_DX * 100; i++) {
            ball.handlePaddleCollision();
            int currentDX = ball.getDx();

            assertEquals(Ball.UP, ball.getYDirection());
            assertTrue(currentDX > 0);
            assertTrue(currentDX <= Ball.MAX_DX);

            KeepScoreOfDXValues(foundDXs, currentDX - 1);
        }

        for (Integer i : foundDXs) {
            assertTrue(i > 0);
        }
    }

    private void KeepScoreOfDXValues(List<Integer> foundValues, int index) {
        int value = foundValues.get(index);
        value++;
        foundValues.set(index, value);
    }

    @Test
    public void testChangeDYIncrease() {
        ball.changeDY(2);
        assertEquals(2 * Ball.INITIAL_DY, ball.getDy());
        ball.changeDY(5);
        assertEquals(2 * 5 * Ball.INITIAL_DY, ball.getDy());
    }

    @Test
    public void testChangeDYDecrease() {
        ball.changeDY(10);
        ball.changeDY(.5);
        assertEquals(5 * Ball.INITIAL_DY, ball.getDy());
    }

    @Test
    public void testChangeDYAtLeast1() {
        ball.changeDY(.001);
        assertEquals(1, ball.getDy());
    }

    @Test
    public void testHandleBrickCollisionXBoundary() {
        Brick b1 = new Brick(x - 1, y);

        ball.handleBrickCollision(b1);
        assertEquals(Ball.LEFT, ball.getXDirection());
        assertEquals(Ball.UP, ball.getYDirection());

        ball.handleBrickCollision(b1);
        assertEquals(Ball.RIGHT, ball.getXDirection());
        assertEquals(Ball.UP, ball.getYDirection());
    }

    @Test
    public void testHandleBrickCollisionYBoundary() {
        Brick b1 = new Brick(x, y - 1);

        ball.handleBrickCollision(b1);
        assertEquals(Ball.RIGHT, ball.getXDirection());
        assertEquals(Ball.DOWN, ball.getYDirection());

        ball.handleBrickCollision(b1);
        assertEquals(Ball.RIGHT, ball.getXDirection());
        assertEquals(Ball.UP, ball.getYDirection());
    }

    @Test
    public void testHandleBrickCollisionXYBoundary() {
        Brick b1 = new Brick(x - 2, y - 1);
        Brick b2 = new Brick(x - 2, y - 3);

        ball.handleBrickCollision(b1);
        assertEquals(Ball.LEFT, ball.getXDirection());
        assertEquals(Ball.UP, ball.getYDirection());

        ball.handleBrickCollision(b2);
        assertEquals(Ball.LEFT, ball.getXDirection());
        assertEquals(Ball.DOWN, ball.getYDirection());
    }

}
