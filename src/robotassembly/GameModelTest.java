
package robotassembly;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameModelTest {

    private GameModel model;

    @BeforeEach
    public void setUp() {
        model = GameModel.randomGame(new Random(42));
    }

    @Test
    public void testNewGameStartsWithZeroScore() {
        assertEquals(0, model.getScore());
    }

    @Test
    public void testNewGameHasParts() {
        assertFalse(model.isFinished());
        assertNotNull(model.getCurrentPart());
        assertTrue(model.remainingParts() > 0);
    }

    @Test
    public void testRobotNameExists() {
        assertNotNull(model.getRobotName());
        assertFalse(model.getRobotName().isEmpty());
    }

    @Test
    public void testClueExists() {
        assertNotNull(model.getClue());
        assertFalse(model.getClue().isEmpty());
    }

    @Test
    public void testInvalidPositionZero() {
        assertEquals(
            PlacementResult.INVALID_SPOT,
            model.placePart(0)
        );
    }

    @Test
    public void testInvalidPositionSix() {
        assertEquals(
            PlacementResult.INVALID_SPOT,
            model.placePart(6)
        );
    }

    @Test
    public void testInvalidPositionKeepsCurrentPart() {
        Part before = model.getCurrentPart();
        int remaining = model.remainingParts();

        model.placePart(0);

        assertEquals(before, model.getCurrentPart());
        assertEquals(remaining, model.remainingParts());
    }

    @Test
    public void testCorrectPlacementIncreasesScore() {
        Part current = model.getCurrentPart();
        int correctSpot = findCorrectSpot(current);

        if (correctSpot == -1) {
            // This part should be discarded, not placed.
            return;
        }

        int before = model.remainingParts();

        assertEquals(
            PlacementResult.CORRECT,
            model.placePart(correctSpot)
        );

        assertEquals(10, model.getScore());
        assertEquals(before - 1, model.remainingParts());
        assertEquals(current, model.placedAt(correctSpot));
    }

    @Test
    public void testWrongPlacementKeepsPartInQueue() {
        Part current = model.getCurrentPart();
        int correctSpot = findCorrectSpot(current);
        int wrongSpot = correctSpot == 1 ? 2 : 1;

        int before = model.remainingParts();

        assertEquals(
            PlacementResult.WRONG,
            model.placePart(wrongSpot)
        );

        assertEquals(-5, model.getScore());
        assertEquals(before, model.remainingParts());
        assertEquals(current, model.getCurrentPart());
    }

    @Test
    public void testWrongDiscardKeepsPartInQueue() {
        Part current = model.getCurrentPart();

        if (findCorrectSpot(current) == -1) {
            // Skip this scenario if the first part is unwanted.
            return;
        }

        int before = model.remainingParts();

        assertEquals(
            PlacementResult.WRONG_DISCARD,
            model.discardPart()
        );

        assertEquals(-5, model.getScore());
        assertEquals(before, model.remainingParts());
        assertEquals(current, model.getCurrentPart());
    }

    @Test
    public void testFinishEntireGame() {
        int safetyCounter = 0;

        while (!model.isFinished() && safetyCounter < 20) {
            Part current = model.getCurrentPart();
            assertNotNull(current);

            int spot = findCorrectSpot(current);

            if (spot == -1) {
                assertEquals(
                    PlacementResult.CORRECT_DISCARD,
                    model.discardPart()
                );
            }
            else {
                assertEquals(
                    PlacementResult.CORRECT,
                    model.placePart(spot)
                );
            }

            safetyCounter++;
        }

        assertTrue(model.isFinished());
        assertEquals(0, model.remainingParts());
        assertNull(model.getCurrentPart());
    }

    /**
     * Finds the correct location for a part using
     * the original robot blueprint.
     *
     * Returns -1 if the part should be discarded.
     */
    private int findCorrectSpot(Part part) {
        for (RobotBlueprint blueprint : Blueprints.all()) {
            if (blueprint.getName().equals(model.getRobotName())) {
                // Check the expected parts for this blueprint.
                Part[] expected = expectedParts(blueprint.getName());

                for (int i = 0; i < expected.length; i++) {
                    if (expected[i] == part) {
                        return i + 1;
                    }
                }
            }
        }

        return -1;
    }

    private Part[] expectedParts(String robotName) {
        switch (robotName) {
            case "Headless":
                return new Part[] {
                    null, Part.LEFT_ARM, Part.RIGHT_ARM,
                    Part.LEFT_LEG, Part.RIGHT_LEG
                };

            case "Armless":
                return new Part[] {
                    Part.HEAD, null, null,
                    Part.LEFT_LEG, Part.RIGHT_LEG
                };

            case "Legless":
                return new Part[] {
                    Part.HEAD, Part.LEFT_ARM, Part.RIGHT_ARM,
                    null, null
                };

            case "Swapped":
                return new Part[] {
                    Part.LEFT_LEG, Part.LEFT_ARM, Part.RIGHT_ARM,
                    Part.HEAD, Part.RIGHT_LEG
                };

            case "Mixed":
                return new Part[] {
                    Part.RIGHT_ARM, Part.LEFT_ARM, null,
                    Part.LEFT_LEG, Part.RIGHT_LEG
                };

            default:
                throw new IllegalArgumentException(
                    "Unknown robot: " + robotName
                );
        }
    }
}
