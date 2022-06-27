package test;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BBGameTest {
    private BBGame game;

    @BeforeEach
    public void setup() {
        game = new BBGame();
    }

    @Test
    public void testConstructor() {
        assertEquals(BBGame.NUM_BRICKS + 2, game.getSprites().size());
        assertEquals(1, game.getNumBalls());
        assertEquals(0, game.getNumBricksDestroyed());
        assertFalse(game.isGameOver());
        assertFalse(game.isGameWon());

        List<Sprite> sprites = game.getSprites();
        int y = BBGame.YMARGIN + Brick.SIDE_LENGTH/2;
        for (int i = 0; i < BBGame.ROWS ; i++) {
            int x = BBGame.XMARGIN + Brick.SIDE_LENGTH/2;
            for (int j = 0; j < BBGame.COLS; j++) {
                int index = i * BBGame.COLS + j + 1;
                assertEquals(x, sprites.get(index).getX());
                assertEquals(y, sprites.get(index).getY());
                x += Brick.SIDE_LENGTH;
            }
            y += Brick.SIDE_LENGTH;
        }
    }

    @Test
    public void testUpdate() {
        List<Ball> balls = game.getBalls();
        assertEquals(1, balls.size());
        Ball ball = balls.get(0);
        Paddle paddle = game.getPaddle();

        assertEquals(BBGame.WIDTH/2, paddle.getX());
        assertEquals(BBGame.PADDLE_DEFAULT_Y, paddle.getY());
        assertEquals(BBGame.WIDTH/2, ball.getX());
        assertEquals(BBGame.BALL_DEFAULT_Y, ball.getY());

        game.update();

        assertEquals(BBGame.WIDTH/2 + paddle.getDx(), paddle.getX());
        assertEquals(BBGame.PADDLE_DEFAULT_Y, paddle.getY());
        assertEquals(BBGame.WIDTH/2 + ball.getDx(), ball.getX());
        assertEquals(BBGame.BALL_DEFAULT_Y - ball.getDy(), ball.getY());
    }

    @Test
    public void testKeyPressedLeftAndRight() {
        Paddle paddle = game.getPaddle();

        game.keyPressed(KeyEvent.VK_KP_LEFT);
        assertFalse(paddle.isFacingRight());

        game.keyPressed(KeyEvent.VK_KP_RIGHT);
        assertTrue(paddle.isFacingRight());

        game.keyPressed(KeyEvent.VK_LEFT);
        assertFalse(paddle.isFacingRight());

        game.keyPressed(KeyEvent.VK_RIGHT);
        assertTrue(paddle.isFacingRight());
    }

    @Test
    public void testKeyPressedGameOver() {
        playUntilGameOver();
        assertTrue(game.isGameOver());
        game.keyPressed(KeyEvent.VK_R);
        assertFalse(game.isGameOver());
        testConstructor(); // constructor in same state as restarted game
    }

    private void playUntilGameOver() {
        while (game.getBalls().size() > 0) {
            game.update();
        }
    }
}
