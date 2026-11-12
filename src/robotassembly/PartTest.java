
package robotassembly;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PartTest
{

    @Test
    public void testAllPartsExist()
    {
        assertEquals(5, Part.values().length);
    }


    @Test
    public void testHeadLabel()
    {
        assertEquals("Head", Part.HEAD.toString());
    }


    @Test
    public void testLeftArmLabel()
    {
        assertEquals("Left arm", Part.LEFT_ARM.toString());
    }


    @Test
    public void testRightArmLabel()
    {
        assertEquals("Right arm", Part.RIGHT_ARM.toString());
    }


    @Test
    public void testLeftLegLabel()
    {
        assertEquals("Left leg", Part.LEFT_LEG.toString());
    }


    @Test
    public void testRightLegLabel()
    {
        assertEquals("Right leg", Part.RIGHT_LEG.toString());
    }


    @Test
    public void testPartValuesAreUnique()
    {
        assertNotEquals(Part.LEFT_ARM, Part.RIGHT_ARM);
        assertNotEquals(Part.LEFT_LEG, Part.RIGHT_LEG);
        assertNotEquals(Part.HEAD, Part.LEFT_ARM);
    }


    @Test
    public void testValueOf()
    {
        assertEquals(Part.HEAD, Part.valueOf("HEAD"));
        assertEquals(Part.LEFT_ARM, Part.valueOf("LEFT_ARM"));
        assertEquals(Part.RIGHT_LEG, Part.valueOf("RIGHT_LEG"));
    }


    @Test
    public void testInvalidValueOf()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> Part.valueOf("WING"));
    }
}
