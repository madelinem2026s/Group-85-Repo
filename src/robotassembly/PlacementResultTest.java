
package robotassembly;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PlacementResultTest
{

    @Test
    public void testAllResultsExist()
    {
        assertEquals(7, PlacementResult.values().length);
    }


    @Test
    public void testCorrect()
    {
        assertEquals(
            PlacementResult.CORRECT,
            PlacementResult.valueOf("CORRECT"));
    }


    @Test
    public void testCorrectDiscard()
    {
        assertEquals(
            PlacementResult.CORRECT_DISCARD,
            PlacementResult.valueOf("CORRECT_DISCARD"));
    }


    @Test
    public void testWrong()
    {
        assertEquals(PlacementResult.WRONG, PlacementResult.valueOf("WRONG"));
    }


    @Test
    public void testWrongDiscard()
    {
        assertEquals(
            PlacementResult.WRONG_DISCARD,
            PlacementResult.valueOf("WRONG_DISCARD"));
    }


    @Test
    public void testOccupied()
    {
        assertEquals(
            PlacementResult.OCCUPIED,
            PlacementResult.valueOf("OCCUPIED"));
    }


    @Test
    public void testInvalidSpot()
    {
        assertEquals(
            PlacementResult.INVALID_SPOT,
            PlacementResult.valueOf("INVALID_SPOT"));
    }


    @Test
    public void testGameOver()
    {
        assertEquals(
            PlacementResult.GAME_OVER,
            PlacementResult.valueOf("GAME_OVER"));
    }


    @Test
    public void testInvalidResult()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> PlacementResult.valueOf("INVALID"));
    }


    @Test
    public void testResultsAreDifferent()
    {
        assertNotEquals(PlacementResult.CORRECT, PlacementResult.WRONG);

        assertNotEquals(
            PlacementResult.CORRECT_DISCARD,
            PlacementResult.WRONG_DISCARD);

        assertNotEquals(PlacementResult.OCCUPIED, PlacementResult.INVALID_SPOT);
    }
}
