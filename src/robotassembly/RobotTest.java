
package robotassembly;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RobotTest
{

    private Robot createRobot()
    {
        RobotBlueprint blueprint = new RobotBlueprint(
            "Test Robot",
            "Place the correct parts",
            new Part[] { Part.HEAD, Part.LEFT_ARM, Part.RIGHT_ARM, null,
                Part.RIGHT_LEG });

        return new Robot(blueprint);
    }


    @Test
    public void testConstructorRejectsNull()
    {
        assertThrows(IllegalArgumentException.class, () -> new Robot(null));
    }


    @Test
    public void testValidPositions()
    {
        Robot robot = createRobot();

        for (int i = 1; i <= 5; i++)
        {
            assertTrue(robot.validPosition(i));
        }
    }


    @Test
    public void testInvalidPositions()
    {
        Robot robot = createRobot();

        assertFalse(robot.validPosition(0));
        assertFalse(robot.validPosition(6));
        assertFalse(robot.validPosition(-1));
        assertFalse(robot.validPosition(100));
    }


    @Test
    public void testInitiallyUnoccupied()
    {
        Robot robot = createRobot();

        for (int i = 1; i <= 5; i++)
        {
            assertFalse(robot.isOccupied(i));
        }
    }


    @Test
    public void testIsOccupiedRejectsInvalidPosition()
    {
        Robot robot = createRobot();

        assertThrows(IllegalArgumentException.class, () -> robot.isOccupied(0));

        assertThrows(IllegalArgumentException.class, () -> robot.isOccupied(6));
    }


    @Test
    public void testCorrectPartFits()
    {
        Robot robot = createRobot();

        assertTrue(robot.fits(Part.HEAD, 1));
        assertTrue(robot.fits(Part.LEFT_ARM, 2));
        assertTrue(robot.fits(Part.RIGHT_ARM, 3));
        assertTrue(robot.fits(Part.RIGHT_LEG, 5));
    }


    @Test
    public void testIncorrectPartDoesNotFit()
    {
        Robot robot = createRobot();

        assertFalse(robot.fits(Part.LEFT_ARM, 1));
        assertFalse(robot.fits(Part.HEAD, 2));
        assertFalse(robot.fits(Part.LEFT_LEG, 4));
    }


    @Test
    public void testFitsInvalidPosition()
    {
        Robot robot = createRobot();

        assertFalse(robot.fits(Part.HEAD, 0));
        assertFalse(robot.fits(Part.HEAD, 6));
    }


    @Test
    public void testPlaceCorrectPart()
    {
        Robot robot = createRobot();

        robot.place(Part.HEAD, 1);

        assertTrue(robot.isOccupied(1));
        assertEquals(Part.HEAD, robot.placedAt(1));
    }


    @Test
    public void testPlaceIncorrectPart()
    {
        Robot robot = createRobot();

        assertThrows(
            IllegalArgumentException.class,
            () -> robot.place(Part.LEFT_ARM, 1));

        assertFalse(robot.isOccupied(1));
    }


    @Test
    public void testPlaceInEmptyRequiredSlot()
    {
        Robot robot = createRobot();

        assertThrows(
            IllegalArgumentException.class,
            () -> robot.place(Part.LEFT_LEG, 4));

        assertNull(robot.placedAt(4));
    }


    @Test
    public void testCannotPlaceInOccupiedPosition()
    {
        Robot robot = createRobot();

        robot.place(Part.HEAD, 1);

        assertThrows(
            IllegalArgumentException.class,
            () -> robot.place(Part.HEAD, 1));

        assertEquals(Part.HEAD, robot.placedAt(1));
    }


    @Test
    public void testPlaceInvalidPosition()
    {
        Robot robot = createRobot();

        assertThrows(
            IllegalArgumentException.class,
            () -> robot.place(Part.HEAD, 0));

        assertThrows(
            IllegalArgumentException.class,
            () -> robot.place(Part.HEAD, 6));
    }


    @Test
    public void testPlacedAtInitiallyNull()
    {
        Robot robot = createRobot();

        for (int i = 1; i <= 5; i++)
        {
            assertNull(robot.placedAt(i));
        }
    }


    @Test
    public void testPlacedAtInvalidPosition()
    {
        Robot robot = createRobot();

        assertThrows(IllegalArgumentException.class, () -> robot.placedAt(0));

        assertThrows(IllegalArgumentException.class, () -> robot.placedAt(6));
    }


    @Test
    public void testInitiallyIncomplete()
    {
        Robot robot = createRobot();

        assertFalse(robot.isComplete());
    }


    @Test
    public void testPartiallyCompleteRobot()
    {
        Robot robot = createRobot();

        robot.place(Part.HEAD, 1);
        robot.place(Part.LEFT_ARM, 2);

        assertFalse(robot.isComplete());
    }


    @Test
    public void testCompleteRobot()
    {
        Robot robot = createRobot();

        robot.place(Part.HEAD, 1);
        robot.place(Part.LEFT_ARM, 2);
        robot.place(Part.RIGHT_ARM, 3);
        robot.place(Part.RIGHT_LEG, 5);

        assertTrue(robot.isComplete());
    }


    @Test
    public void testEmptyRobotIsComplete()
    {
        RobotBlueprint blueprint =
            new RobotBlueprint("Empty", "No parts", new Part[5]);

        Robot robot = new Robot(blueprint);

        assertTrue(robot.isComplete());
    }
}
