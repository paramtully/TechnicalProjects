package test;

import model.Bonus;
import model.PowerUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PowerUpTest {
    private static final int X = 4;
    private static final int Y = 5;
    private static final int NUM_TESTS = 100;
    private PowerUp pu;

    @BeforeEach
    public void setup() {
        pu = new PowerUp(X, Y);
    }

    @Test
    public void testConstructor() {
        assertEquals(X, pu.getX());
        assertEquals(Y, pu.getY());
        assertEquals(2*PowerUp.RADIUS, pu.getWidth());
        assertEquals(2*PowerUp.RADIUS, pu.getHeight());
        assertEquals(PowerUp.DX, pu.getDx());
    }

    @Test
    // REQUIRES: only 3 bonuses; Multiball, Slowball, Widepaddle
    public void testInitializeBonus() {
        int[] arr = {0, 0, 0};
        for (int i = 0; i < NUM_TESTS; i++) {
            PowerUp p = new PowerUp(X, Y);
            if (p.getBonus() == Bonus.MULTIBALL) {
                arr[0]++;
            } else if (p.getBonus() == Bonus.SLOWBALL) {
                arr[1]++;
            } else if (p.getBonus() == Bonus.WIDEPADDLE) {
                arr[2]++;
            } else {
                fail("Unexpected Bonus was given...");
            }
        }
        for (int i : arr) {
            assertTrue(i > 0);
        }
    }

    @Test
    public void testMove() {
        for (int i = 1; i < NUM_TESTS; i++) {
            pu.move();
            assertEquals(Y + (i * PowerUp.DY), pu.getY());
        }
    }
}
