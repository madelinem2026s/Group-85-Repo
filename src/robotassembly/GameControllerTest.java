
package robotassembly;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

/**
 * Tests the console GameController.
 */
public class GameControllerTest
{

    /**
     * Runs the console game with simulated input and returns everything printed
     * to the console.
     */
    private String runGame(String commands)
    {
        GameModel model = GameModel.randomGame(new Random(42));
        ConsoleView view = new ConsoleView();

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        PrintStream original = System.out;

        try (
            Scanner scanner = new Scanner(commands);
            PrintStream capture = new PrintStream(output))
        {

            System.setOut(capture);

            GameController controller =
                new GameController(model, view, scanner);

            controller.run();
        }
        finally
        {
            System.setOut(original);
        }

        return output.toString();
    }


    /**
     * Tests selecting Easy difficulty.
     */
    @Test
    public void testEasyDifficulty()
    {
        String output = runGame("1\n");

        assertTrue(output.contains("Difficulty: Easy"));
        assertTrue(output.contains("Clue:"));
    }


    /**
     * Tests selecting Medium difficulty.
     */
    @Test
    public void testMediumDifficulty()
    {
        String output = runGame("2\n");

        assertTrue(output.contains("Difficulty: Medium"));
        assertTrue(output.contains("Clue:"));
    }


    /**
     * Tests selecting Hard difficulty.
     */
    @Test
    public void testHardDifficulty()
    {
        String output = runGame("3\n");

        assertTrue(output.contains("Difficulty: Hard"));
        assertTrue(output.contains("Clue:"));
    }


    /**
     * Tests an invalid difficulty followed by a valid difficulty.
     */
    @Test
    public void testInvalidDifficultyThenValid()
    {
        String output = runGame("9\n2\n");

        assertTrue(output.contains("Invalid difficulty"));

        assertTrue(output.contains("Difficulty: Medium"));
    }


    /**
     * Tests non-numeric difficulty input.
     */
    @Test
    public void testNonNumericDifficulty()
    {
        String output = runGame("hello\n1\n");

        assertTrue(output.contains("Invalid difficulty"));

        assertTrue(output.contains("Difficulty: Easy"));
    }


    /**
     * Tests that the game stops safely if no difficulty is entered.
     */
    @Test
    public void testNoDifficultyInput()
    {
        String output = runGame("");

        assertTrue(output.contains("Input ended. Game stopped safely."));
    }


    /**
     * Tests invalid placement input.
     */
    @Test
    public void testInvalidPlacementInput()
    {
        String output = runGame("1\nhello\n");

        assertTrue(output.contains("Enter 1-5 or 'discard'."));
    }


    /**
     * Tests a placement outside the valid range.
     */
    @Test
    public void testInvalidPosition()
    {
        String output = runGame("1\n9\n");

        assertTrue(output.contains("Enter a position from 1 to 5."));
    }


    /**
     * Tests that the controller handles an empty placement command.
     */
    @Test
    public void testEmptyPlacementInput()
    {
        String output = runGame("1\n\n");

        assertTrue(output.contains("Enter 1-5 or 'discard'."));
    }


    /**
     * Tests that the game stops safely when input ends during gameplay.
     */
    @Test
    public void testInputEndsDuringGame()
    {
        String output = runGame("1\n");

        assertTrue(output.contains("Input ended. Game stopped safely."));
    }


    /**
     * Tests that the GUI controller constructor rejects a null model.
     */
    @Test
    public void testNullModel()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> new GameController((GameModel)null));
    }


    /**
     * Tests that the console controller rejects a null view.
     */
    @Test
    public void testNullView()
    {
        GameModel model = GameModel.randomGame(new Random(42));

        assertThrows(
            IllegalArgumentException.class,
            () -> new GameController(model, null, new Scanner("1\n")));
    }


    /**
     * Tests that the console controller rejects a null scanner.
     */
    @Test
    public void testNullScanner()
    {
        GameModel model = GameModel.randomGame(new Random(42));

        assertThrows(
            IllegalArgumentException.class,
            () -> new GameController(model, new ConsoleView(), null));
    }


    /**
     * Tests that selecting Hard difficulty does not display the robot name
     * directly.
     */
    @Test
    public void testHardDoesNotRevealRobotName()
    {
        GameModel model = GameModel.randomGame(new Random(42));

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        PrintStream original = System.out;

        try (
            Scanner scanner = new Scanner("3\n");
            PrintStream capture = new PrintStream(output))
        {

            System.setOut(capture);

            GameController controller =
                new GameController(model, new ConsoleView(), scanner);

            controller.run();
        }
        finally
        {
            System.setOut(original);
        }

        assertFalse(
            output.toString().contains("Robot: " + model.getRobotName()));
    }
}
