
package robotassembly;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class BattleModelTest
{

    @Test
    public void testStartingHealth()
    {
        BattleModel model = new BattleModel();

        assertEquals(1000, model.getHealth(1));
        assertEquals(1000, model.getHealth(2));
        assertEquals(1, model.getCurrentPlayer());
        assertEquals(1, model.getRound());
        assertEquals("Easy", model.getDifficulty());
        assertEquals(0, model.getLastDamage());
        assertFalse(model.isFinished());
    }


    @Test
    public void testDamageCalculation()
    {
        BattleModel model = new BattleModel();

        int damage = model.completeTurn(1, 50, 4.0);

        assertEquals(200, damage);
        assertEquals(200, model.getLastDamage());
        assertEquals(800, model.getHealth(2));
        assertEquals(1000, model.getHealth(1));
        assertEquals(2, model.getCurrentPlayer());
    }


    @Test
    public void testNegativeScoreDealsZeroDamage()
    {
        BattleModel model = new BattleModel();

        int damage = model.completeTurn(1, -10, 4.0);

        assertEquals(0, damage);
        assertEquals(1000, model.getHealth(2));
    }


    @Test
    public void testPlayerTwoGetsTurnAtZeroHealth()
    {
        BattleModel model = new BattleModel();

        model.completeTurn(1, 250, 4.0);

        assertEquals(0, model.getHealth(2));
        assertFalse(model.isFinished());
        assertEquals(2, model.getCurrentPlayer());
    }


    @Test
    public void testDoubleKnockoutIsTie()
    {
        BattleModel model = new BattleModel();

        model.completeTurn(1, 250, 4.0);
        model.completeTurn(2, 250, 4.0);

        assertEquals(0, model.getHealth(1));
        assertEquals(0, model.getHealth(2));
        assertTrue(model.isFinished());
        assertTrue(model.getWinner().contains("tie"));
    }


    @Test
    public void testSingleKnockout()
    {
        BattleModel model = new BattleModel();

        model.completeTurn(1, 250, 4.0);
        model.completeTurn(2, 10, 4.0);

        assertEquals(0, model.getHealth(2));
        assertEquals(960, model.getHealth(1));
        assertTrue(model.isFinished());
        assertTrue(model.getWinner().contains("Player 1"));
    }


    @Test
    public void testRoundProgression()
    {
        BattleModel model = new BattleModel();

        model.completeTurn(1, 10, 4.0);
        model.completeTurn(2, 10, 4.0);

        assertEquals(2, model.getRound());
        assertEquals("Medium", model.getDifficulty());
        assertEquals(1, model.getCurrentPlayer());

        model.completeTurn(1, 10, 6.0);
        model.completeTurn(2, 10, 6.0);

        assertEquals(3, model.getRound());
        assertEquals("Hard", model.getDifficulty());
        assertEquals(1, model.getCurrentPlayer());
    }


    @Test
    public void testEqualHealthAtEndIsTie()
    {
        BattleModel model = new BattleModel();

        for (int round = 0; round < 3; round++)
        {
            model.completeTurn(1, 10, 2.0);
            model.completeTurn(2, 10, 2.0);
        }

        assertTrue(model.isFinished());
        assertEquals("It's a tie!", model.getWinner());
    }


    @Test
    public void testPlayerOneWinsAfterThreeRounds()
    {
        BattleModel model = new BattleModel();

        for (int round = 0; round < 3; round++)
        {
            model.completeTurn(1, 20, 2.0);
            model.completeTurn(2, 10, 2.0);
        }

        assertTrue(model.isFinished());
        assertTrue(model.getWinner().contains("Player 1"));
        assertTrue(model.getHealth(1) > model.getHealth(2));
    }


    @Test
    public void testPlayerTwoWinsAfterThreeRounds()
    {
        BattleModel model = new BattleModel();

        for (int round = 0; round < 3; round++)
        {
            model.completeTurn(1, 10, 2.0);
            model.completeTurn(2, 20, 2.0);
        }

        assertTrue(model.isFinished());
        assertTrue(model.getWinner().contains("Player 2"));
        assertTrue(model.getHealth(2) > model.getHealth(1));
    }


    @Test
    public void testHealthNeverNegative()
    {
        BattleModel model = new BattleModel();

        model.completeTurn(1, 10000, 12.0);

        assertEquals(0, model.getHealth(2));
    }


    @Test
    public void testZeroDamage()
    {
        BattleModel model = new BattleModel();

        assertEquals(0, model.completeTurn(1, 0, 4.0));
        assertEquals(1000, model.getHealth(2));
        assertFalse(model.isFinished());
    }


    @Test
    public void testInvalidPlayer()
    {
        BattleModel model = new BattleModel();

        assertThrows(IllegalArgumentException.class, () -> model.getHealth(3));
    }


    @Test
    public void testInvalidPlayerZero()
    {
        BattleModel model = new BattleModel();

        assertThrows(IllegalArgumentException.class, () -> model.getHealth(0));
    }


    @Test
    public void testWrongPlayerTurn()
    {
        BattleModel model = new BattleModel();

        assertThrows(
            IllegalArgumentException.class,
            () -> model.completeTurn(2, 10, 4.0));
    }


    @Test
    public void testInvalidPlayerCompletingTurn()
    {
        BattleModel model = new BattleModel();

        assertThrows(
            IllegalArgumentException.class,
            () -> model.completeTurn(3, 10, 4.0));
    }


    @Test
    public void testNegativeMultiplier()
    {
        BattleModel model = new BattleModel();

        assertThrows(
            IllegalArgumentException.class,
            () -> model.completeTurn(1, 10, -2.0));
    }


    @Test
    public void testNaNMultiplier()
    {
        BattleModel model = new BattleModel();

        assertThrows(
            IllegalArgumentException.class,
            () -> model.completeTurn(1, 10, Double.NaN));
    }


    @Test
    public void testInfiniteMultiplier()
    {
        BattleModel model = new BattleModel();

        assertThrows(
            IllegalArgumentException.class,
            () -> model.completeTurn(1, 10, Double.POSITIVE_INFINITY));
    }


    @Test
    public void testCannotPlayAfterBattleEnds()
    {
        BattleModel model = new BattleModel();

        model.completeTurn(1, 250, 4.0);
        model.completeTurn(2, 250, 4.0);

        assertTrue(model.isFinished());

        assertThrows(
            IllegalStateException.class,
            () -> model.completeTurn(1, 10, 4.0));
    }


    @Test
    public void testPlayerOneAlwaysStartsNextRound()
    {
        BattleModel model = new BattleModel();

        model.completeTurn(1, 10, 2.0);
        model.completeTurn(2, 10, 2.0);

        assertEquals(1, model.getCurrentPlayer());

        model.completeTurn(1, 10, 2.0);
        model.completeTurn(2, 10, 2.0);

        assertEquals(1, model.getCurrentPlayer());
    }
}
