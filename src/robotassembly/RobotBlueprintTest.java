
package robotassembly;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RobotBlueprintTest
{

    private RobotBlueprint createBlueprint()
    {
        return new RobotBlueprint(
            "Test Robot",
            "A test clue",
            new Part[] { Part.HEAD, Part.LEFT_ARM, Part.RIGHT_ARM, null,
                Part.RIGHT_LEG });
    }


    @Test
    public void testConstructorAndGetters()
    {
        RobotBlueprint blueprint = createBlueprint();

        assertEquals("Test Robot", blueprint.getName());
        assertEquals("A test clue", blueprint.getClue());
    }


    @Test
    public void testRequiredAt()
    {
        RobotBlueprint blueprint = createBlueprint();

        assertEquals(Part.HEAD, blueprint.requiredAt(1));
        assertEquals(Part.LEFT_ARM, blueprint.requiredAt(2));
        assertEquals(Part.RIGHT_ARM, blueprint.requiredAt(3));
        assertNull(blueprint.requiredAt(4));
        assertEquals(Part.RIGHT_LEG, blueprint.requiredAt(5));
    }


    @Test
    public void testInvalidPositionZero()
    {
        RobotBlueprint blueprint = createBlueprint();

        assertThrows(
            IllegalArgumentException.class,
            () -> blueprint.requiredAt(0));
    }


    @Test
    public void testInvalidPositionSix()
    {
        RobotBlueprint blueprint = createBlueprint();

        assertThrows(
            IllegalArgumentException.class,
            () -> blueprint.requiredAt(6));
    }


    @Test
    public void testNeedsRequiredParts()
    {
        RobotBlueprint blueprint = createBlueprint();

        assertTrue(blueprint.needs(Part.HEAD));
        assertTrue(blueprint.needs(Part.LEFT_ARM));
        assertTrue(blueprint.needs(Part.RIGHT_ARM));
        assertTrue(blueprint.needs(Part.RIGHT_LEG));
    }


    @Test
    public void testDoesNotNeedMissingPart()
    {
        RobotBlueprint blueprint = createBlueprint();

        assertFalse(blueprint.needs(Part.LEFT_LEG));
    }


    @Test
    public void testRequiredCount()
    {
        RobotBlueprint blueprint = createBlueprint();

        assertEquals(4, blueprint.requiredCount());
    }


    @Test
    public void testEmptyBlueprint()
    {
        RobotBlueprint blueprint =
            new RobotBlueprint("Empty", "No parts needed", new Part[5]);

        assertEquals(0, blueprint.requiredCount());
        assertFalse(blueprint.needs(Part.HEAD));
    }


    @Test
    public void testFullBlueprint()
    {
        RobotBlueprint blueprint =
            new RobotBlueprint("Full", "All parts needed", Part.values());

        assertEquals(5, blueprint.requiredCount());
    }


    @Test
    public void testNullName()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> new RobotBlueprint(null, "Clue", new Part[5]));
    }


    @Test
    public void testNullClue()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> new RobotBlueprint("Robot", null, new Part[5]));
    }


    @Test
    public void testNullRequiredArray()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> new RobotBlueprint("Robot", "Clue", null));
    }


    @Test
    public void testTooFewPositions()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> new RobotBlueprint("Robot", "Clue", new Part[4]));
    }


    @Test
    public void testTooManyPositions()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> new RobotBlueprint("Robot", "Clue", new Part[6]));
    }


    @Test
    public void testDuplicatePartRejected()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> new RobotBlueprint(
                "Robot",
                "Clue",
                new Part[] { Part.HEAD, Part.HEAD, null, null, null }));
    }


    @Test
    public void testRequiredArrayIsCopied()
    {
        Part[] parts = { Part.HEAD, Part.LEFT_ARM, null, null, null };

        RobotBlueprint blueprint = new RobotBlueprint("Robot", "Clue", parts);

        parts[0] = Part.RIGHT_ARM;

        assertEquals(Part.HEAD, blueprint.requiredAt(1));
    }


    @Test
    public void testNeedsNull()
    {
        RobotBlueprint blueprint = createBlueprint();

        assertTrue(blueprint.needs(null));
    }
}
