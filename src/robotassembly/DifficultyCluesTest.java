
package robotassembly;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Tests the difficulty-based clues for each robot.
 */
public class DifficultyCluesTest
{

    /**
     * Tests the Easy clue for a Headless robot.
     */
    @Test
    public void testEasyClue()
    {
        String clue = DifficultyClues.getClue("Headless", "Easy", "Fallback");

        assertEquals(
            "This robot has no head. Discard the head. "
                + "All other parts go in their normal positions.",
            clue);
    }


    /**
     * Tests the Medium clue for a Headless robot.
     */
    @Test
    public void testMediumClue()
    {
        String clue = DifficultyClues.getClue("Headless", "Medium", "Fallback");

        assertEquals(
            "I can walk and reach, but nothing sits "
                + "above my shoulders. My limbs remain "
                + "where you expect them.",
            clue);
    }


    /**
     * Tests the Hard clue for a Headless robot.
     */
    @Test
    public void testHardClue()
    {
        String clue = DifficultyClues.getClue("Headless", "Hard", "Fallback");

        assertEquals(
            "Four limbs serve my frame, yet no crown "
                + "rests above. My paths and grasp remain unchanged.",
            clue);
    }


    /**
     * Tests that Armless has different clues for each difficulty.
     */
    @Test
    public void testArmlessClues()
    {
        String easy = DifficultyClues.getClue("Armless", "Easy", "Fallback");
        String medium =
            DifficultyClues.getClue("Armless", "Medium", "Fallback");
        String hard = DifficultyClues.getClue("Armless", "Hard", "Fallback");

        assertNotEquals(easy, medium);
        assertNotEquals(medium, hard);
        assertNotEquals(easy, hard);
    }


    /**
     * Tests that Legless has different clues for each difficulty.
     */
    @Test
    public void testLeglessClues()
    {
        String easy = DifficultyClues.getClue("Legless", "Easy", "Fallback");
        String medium =
            DifficultyClues.getClue("Legless", "Medium", "Fallback");
        String hard = DifficultyClues.getClue("Legless", "Hard", "Fallback");

        assertNotEquals(easy, medium);
        assertNotEquals(medium, hard);
        assertNotEquals(easy, hard);
    }


    /**
     * Tests that Swapped has different clues for each difficulty.
     */
    @Test
    public void testSwappedClues()
    {
        String easy = DifficultyClues.getClue("Swapped", "Easy", "Fallback");
        String medium =
            DifficultyClues.getClue("Swapped", "Medium", "Fallback");
        String hard = DifficultyClues.getClue("Swapped", "Hard", "Fallback");

        assertNotEquals(easy, medium);
        assertNotEquals(medium, hard);
        assertNotEquals(easy, hard);
    }


    /**
     * Tests that Mixed has different clues for each difficulty.
     */
    @Test
    public void testMixedClues()
    {
        String easy = DifficultyClues.getClue("Mixed", "Easy", "Fallback");
        String medium = DifficultyClues.getClue("Mixed", "Medium", "Fallback");
        String hard = DifficultyClues.getClue("Mixed", "Hard", "Fallback");

        assertNotEquals(easy, medium);
        assertNotEquals(medium, hard);
        assertNotEquals(easy, hard);
    }


    /**
     * Tests that an unknown robot returns the fallback clue.
     */
    @Test
    public void testUnknownRobot()
    {
        String clue =
            DifficultyClues.getClue("Unknown", "Hard", "Fallback clue");

        assertEquals("Fallback clue", clue);
    }


    /**
     * Tests that robot names are not case-sensitive.
     */
    @Test
    public void testCaseInsensitiveRobotName()
    {
        String normal = DifficultyClues.getClue("Headless", "Hard", "Fallback");

        String lowercase =
            DifficultyClues.getClue("headless", "Hard", "Fallback");

        assertEquals(normal, lowercase);
    }


    /**
     * Tests that Hard clues do not directly reveal the robot's name.
     */
    @Test
    public void testHardCluesDoNotRevealRobotName()
    {
        String[] robots =
            { "Headless", "Armless", "Legless", "Swapped", "Mixed" };

        for (String robot : robots)
        {
            String clue = DifficultyClues.getClue(robot, "Hard", "Fallback");

            assertFalse(clue.toLowerCase().contains(robot.toLowerCase()));
        }
    }
}
