
package robotassembly;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

/**
 * Tests the console view's output.
 */
public class ConsoleViewTest {

    /**
     * Tests that the difficulty menu displays
     * all three difficulty levels.
     */
    @Test
    public void testDifficultyMenu() {
        ConsoleView view = new ConsoleView();

        ByteArrayOutputStream output =
            new ByteArrayOutputStream();

        PrintStream original = System.out;

        try {
            System.setOut(new PrintStream(output));

            view.showDifficultyMenu();
        }
        finally {
            System.setOut(original);
        }

        String text = output.toString();

        assertTrue(text.contains("Easy"));
        assertTrue(text.contains("Medium"));
        assertTrue(text.contains("Hard"));
    }

    /**
     * Tests the message shown when the player
     * enters an invalid difficulty.
     */
    @Test
    public void testInvalidDifficulty() {
        ConsoleView view = new ConsoleView();

        ByteArrayOutputStream output =
            new ByteArrayOutputStream();

        PrintStream original = System.out;

        try {
            System.setOut(new PrintStream(output));

            view.showInvalidDifficulty();
        }
        finally {
            System.setOut(original);
        }

        assertTrue(
            output.toString().contains("Invalid difficulty"));
    }

    /**
     * Tests the message for invalid placement input.
     */
    @Test
    public void testInvalidInput() {
        ConsoleView view = new ConsoleView();

        ByteArrayOutputStream output =
            new ByteArrayOutputStream();

        PrintStream original = System.out;

        try {
            System.setOut(new PrintStream(output));

            view.showInvalidInput();
        }
        finally {
            System.setOut(original);
        }

        assertTrue(
            output.toString().contains("Enter 1-5"));
    }

    /**
     * Tests the feedback for correct placement.
     */
    @Test
    public void testCorrectPlacementMessage() {
        ConsoleView view = new ConsoleView();

        ByteArrayOutputStream output =
            new ByteArrayOutputStream();

        PrintStream original = System.out;

        try {
            System.setOut(new PrintStream(output));

            view.showResult(PlacementResult.CORRECT);
        }
        finally {
            System.setOut(original);
        }

        assertTrue(
            output.toString().contains("Correct placement"));
        assertTrue(
            output.toString().contains("+10"));
    }

    /**
     * Tests the feedback for incorrect placement.
     */
    @Test
    public void testWrongPlacementMessage() {
        ConsoleView view = new ConsoleView();

        ByteArrayOutputStream output =
            new ByteArrayOutputStream();

        PrintStream original = System.out;

        try {
            System.setOut(new PrintStream(output));

            view.showResult(PlacementResult.WRONG);
        }
        finally {
            System.setOut(original);
        }

        assertTrue(
            output.toString().contains("Wrong position"));
        assertTrue(
            output.toString().contains("-5"));
    }

    /**
     * Tests the feedback for correctly discarding a part.
     */
    @Test
    public void testCorrectDiscardMessage() {
        ConsoleView view = new ConsoleView();

        ByteArrayOutputStream output =
            new ByteArrayOutputStream();

        PrintStream original = System.out;

        try {
            System.setOut(new PrintStream(output));

            view.showResult(PlacementResult.CORRECT_DISCARD);
        }
        finally {
            System.setOut(original);
        }

        assertTrue(
            output.toString().contains("Correct discard"));
        assertTrue(
            output.toString().contains("+5"));
    }

    /**
     * Tests the feedback for discarding a needed part.
     */
    @Test
    public void testWrongDiscardMessage() {
        ConsoleView view = new ConsoleView();

        ByteArrayOutputStream output =
            new ByteArrayOutputStream();

        PrintStream original = System.out;

        try {
            System.setOut(new PrintStream(output));

            view.showResult(PlacementResult.WRONG_DISCARD);
        }
        finally {
            System.setOut(original);
        }

        assertTrue(
            output.toString().contains("This part is needed"));
        assertTrue(
            output.toString().contains("-5"));
    }

    /**
     * Tests the feedback for an occupied position.
     */
    @Test
    public void testOccupiedPositionMessage() {
        ConsoleView view = new ConsoleView();

        ByteArrayOutputStream output =
            new ByteArrayOutputStream();

        PrintStream original = System.out;

        try {
            System.setOut(new PrintStream(output));

            view.showResult(PlacementResult.OCCUPIED);
        }
        finally {
            System.setOut(original);
        }

        assertTrue(
            output.toString().contains("already occupied"));
    }

    /**
     * Tests the feedback for an invalid position.
     */
    @Test
    public void testInvalidPositionMessage() {
        ConsoleView view = new ConsoleView();

        ByteArrayOutputStream output =
            new ByteArrayOutputStream();

        PrintStream original = System.out;

        try {
            System.setOut(new PrintStream(output));

            view.showResult(PlacementResult.INVALID_SPOT);
        }
        finally {
            System.setOut(original);
        }

        assertTrue(
            output.toString().contains("1 to 5"));
    }

    /**
     * Tests the message shown when no parts remain.
     */
    @Test
    public void testGameOverMessage() {
        ConsoleView view = new ConsoleView();

        ByteArrayOutputStream output =
            new ByteArrayOutputStream();

        PrintStream original = System.out;

        try {
            System.setOut(new PrintStream(output));

            view.showResult(PlacementResult.GAME_OVER);
        }
        finally {
            System.setOut(original);
        }

        assertTrue(
            output.toString().contains("No parts remain"));
    }
}
