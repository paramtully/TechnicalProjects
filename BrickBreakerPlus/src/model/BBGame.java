package model;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

// represents a Brick Breaker game
public class BBGame extends Observable {
    public static final int WIDTH = 700;
    public static final int HEIGHT = 500;
    public static final int COLS = 12;
    public static final int ROWS = 7;
    public static final int NUM_BRICKS = COLS * ROWS;
    public static final int XMARGIN = (WIDTH - COLS * Brick.SIDE_LENGTH) / 2;
    public static final int YMARGIN = 2 * Brick.SIDE_LENGTH;
    public static final int PADDLE_DEFAULT_Y = BBGame.HEIGHT - 35;
    public static final int BALL_DEFAULT_Y = PADDLE_DEFAULT_Y - Paddle.HEIGHT/2 - Ball.RADIUS;
    public static final int POWER_UP_PROBABILITY = 15;
    public static final Random RND = new Random();

    public static final double DY_MULTIPLIER = 1.5;
    public static final double BALL_SLOW_MULTIPLIER = 0.7;
    public static final double PADDLE_GROWTH_MULTIPLIER = 1.5;
    public static final double NUM_BALLS_GRANTED = 4;

    public static final String EVENT_SCORE_CHANGED = "SCORE CHANGED";

    private List<Sprite> sprites;
    private Paddle paddle;
    private int numBalls;
    private int numBricksDestroyed;
    private boolean gameOver;
    private boolean gameWon;

    // EFFECTS: constructs this game
    public BBGame() {
        sprites = new ArrayList<>();
        initializeSprites();
        reset();
    }

    public int getNumBalls() {
        return numBalls;
    }

    public int getNumBricksDestroyed() {
        return numBricksDestroyed;
    }

    public List<Sprite> getSprites() {
        return sprites;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public List<Ball> getBalls() {
        ArrayList<Ball> balls = new ArrayList<>();
        for (Sprite s : sprites) {
            if (s instanceof Ball) {
                balls.add((Ball) s);
            }
        }
        return balls;
    }

    // MODIFIES: this
    // EFFECTS: sets up paddle in center of window, and bricks
    private void initializeSprites() {
        sprites.clear();
        paddle = new Paddle(BBGame.WIDTH/2, PADDLE_DEFAULT_Y);
        sprites.add(paddle);
        initializeBricks();
    }

    // MODIFIES: this
    // EFFECTS: sets up bricks with COLS columns and ROWS rows,
    //          with XMARGIN and YMARGIN space from screen borders
    private void initializeBricks() {
        int y = YMARGIN + Brick.SIDE_LENGTH/2;
        for (int i = 0; i < ROWS ; i++) {
            int x = XMARGIN + Brick.SIDE_LENGTH/2;
            for (int j = 0; j < COLS; j++) {
                sprites.add(new Brick(x, y));
                x += Brick.SIDE_LENGTH;
            }
            y += Brick.SIDE_LENGTH;
        }
    }

    // MODIFIES: this
    // EFFECTS: resets number of bricks destroyed to 0, adds ball to game,
    //          starts game over
    private void reset() {
        numBalls = 1;
        numBricksDestroyed = 0;
        gameOver = false;
        gameWon = false;
        sprites.add(new Ball(BBGame.WIDTH/2, BALL_DEFAULT_Y));

        setChanged();
        notifyObservers(EVENT_SCORE_CHANGED);
    }

    // MODIFIES: this
    // EFFECTS: updates balls, paddles, and bricks
    public void update() {
        moveSprites();
        checkCollisions();
        checkGameOver();
    }

    // MODIFIES: this
    // EFFECTS: moves balls, paddles, and bricks
    private void moveSprites() {
        for (Sprite next : sprites) {
            next.move();
        }
    }

    // MODIFIES: this
    // EFFECTS: removes bricks that have been hit, increases score on hit
    //          changes ball y direction and assigns random dx on paddle collision
    private void checkCollisions() {
        List<Sprite> toRemove = new ArrayList<>();
        List<Sprite> toAdd = new ArrayList<>();
        for (Sprite next : sprites) {
            if (next instanceof Brick) {
                checkBrickCollision((Brick) next, toRemove, toAdd);
            } else if (next instanceof Ball) {
                checkBallCollision((Ball) next, toRemove);
            } else if (next instanceof PowerUp) {
                if (next.checkCollision(paddle)) {
                    handlePowerUpCollision(toRemove, toAdd, (PowerUp) next);
                }
            }
        }
        sprites.removeAll(toRemove);
        sprites.addAll(toAdd);
    }

    // MODIFIES: toRemove, toAdd
    // EFFECTS: checks for collision between brick next, and balls in play
    private void checkBrickCollision(Brick next, List<Sprite> toRemove, List<Sprite> toAdd) {
        for (Sprite sprite : sprites) {
            if (sprite instanceof Ball) {
                if (next.checkCollision(sprite)) {
                    handleBrickCollision(toRemove, toAdd, next, (Ball) sprite);
                    break; // only want single collision with brick
                }
            }
        }
    }

    // MODIFIES: toRemove, toAdd
    // EFFECTS: removes bricks that have been hit, increases score on hit
    //          if score is multiple of COLS, increase by DY_MULTIPLIER
    //          randomly spawns a power up
    private void handleBrickCollision(List<Sprite> toRemove, List<Sprite> toAdd, Brick brick, Ball ball) {
        if (!toRemove.contains(brick)) {
            ball.handleBrickCollision(brick);
            toRemove.add(brick);
            numBricksDestroyed++;
            if (numBricksDestroyed % COLS == 0) {
                ball.changeDY(DY_MULTIPLIER);
            }

            spawnPowerUp(toAdd, brick);

            setChanged();
            notifyObservers(EVENT_SCORE_CHANGED);
        }
    }

    // MODIFIES: toRemove
    // EFFECTS: handles ball collision with paddle and going off-screen
    private void checkBallCollision(Ball next, List<Sprite> toRemove) {
        if (next.checkCollision(paddle)) {
            next.handlePaddleCollision();
        } else if (next.getY() >= BBGame.HEIGHT){
            removeBall(toRemove, next);
        }
    }

    // MODIFIES: this
    // EFFECTS: removes ball that has gone off-screen
    private void removeBall(List<Sprite> toRemove, Ball ball) {
        toRemove.add(ball);
        numBalls--;

        setChanged();
        notifyObservers(EVENT_SCORE_CHANGED);
    }

    // MODIFIES: this
    // EFFECTS: randomly may spawn a power up at the brick's position
    private void spawnPowerUp(List<Sprite> toAdd, Brick brick) {
        if (RND.nextInt(POWER_UP_PROBABILITY) == 0) {
            toAdd.add(new PowerUp(brick.getX(), brick.getY()));
        }
    }

    // MODIFIES: this
    // EFFECTS: generates bonus
    private void handlePowerUpCollision(List<Sprite> toRemove, List<Sprite> toAdd, PowerUp powerUp) {
        Bonus b = powerUp.getBonus();
        if (b.equals(Bonus.MULTIBALL)) {
            spawnMultiBall(toAdd);
        } else if (b.equals(Bonus.SLOWBALL)) {
            slowBalls();
        } else if (b.equals(Bonus.WIDEPADDLE)) {
            paddle.changeWidth(PADDLE_GROWTH_MULTIPLIER);
        }

        toRemove.add(powerUp);
    }

    // MODIFIES: this
    // EFFECTS: spawns additional balls
    private void spawnMultiBall(List<Sprite> toAdd) {
        for (int i = 0; i < NUM_BALLS_GRANTED; i++) {
            toAdd.add(new Ball(paddle.getX(), paddle.getY()));
            numBalls++;
        }

        setChanged();
        notifyObservers(EVENT_SCORE_CHANGED);
    }

    // MODIFIES: this
    // EFFECTS: slows all balls in play by constant factor
    private void slowBalls() {
        for (Sprite next : sprites) {
            if (next instanceof Ball) {
                ((Ball) next).changeDY(BALL_SLOW_MULTIPLIER);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: sets game over if ball goes off window, or if all bricks are destroyed
    private void checkGameOver() {
        if (numBalls == 0 || numBricksDestroyed == NUM_BRICKS) {
            gameOver = true;
            gameWon = numBricksDestroyed == NUM_BRICKS;
            initializeSprites();
        }
    }

    // MODIFIES: this
    // EFFECTS: turns paddle, resets game, or exits game in response to keyboard input
    public void keyPressed(int keyCode) {
        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_KP_LEFT) {
            paddle.faceLeft();
        } else if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_KP_RIGHT) {
            paddle.faceRight();
        } else if (keyCode == KeyEvent.VK_R && gameOver) {
            reset();
        } else if (keyCode == KeyEvent.VK_X) {
            System.exit(0);
        }
    }

    // MODIFIES: g
    // EFFECTS: draws sprites onto g
    public void draw(Graphics g) {
        for (Sprite sprite : sprites) {
            sprite.draw(g);
        }
    }

}
