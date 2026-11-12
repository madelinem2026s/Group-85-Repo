
package robotassembly;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

public class BlueprintsTest {

    @Test
    public void testFiveBlueprintsExist() {
        assertEquals(5, Blueprints.all().size());
    }

    @Test
    public void testAllBlueprintsHaveNames() {
        for (RobotBlueprint blueprint : Blueprints.all()) {
            assertNotNull(blueprint.getName());
            assertFalse(blueprint.getName().isEmpty());
        }
    }

    @Test
    public void testAllBlueprintsHaveClues() {
        for (RobotBlueprint blueprint : Blueprints.all()) {
            assertNotNull(blueprint.getClue());
            assertFalse(blueprint.getClue().isEmpty());
        }
    }

    @Test
    public void testExpectedRobotNames() {
        List<RobotBlueprint> blueprints = Blueprints.all();

        assertEquals("Headless", blueprints.get(0).getName());
        assertEquals("Armless", blueprints.get(1).getName());
        assertEquals("Legless", blueprints.get(2).getName());
        assertEquals("Swapped", blueprints.get(3).getName());
        assertEquals("Mixed", blueprints.get(4).getName());
    }

    @Test
    public void testRandomBlueprintIsValid() {
        Random random = new Random(42);

        for (int i = 0; i < 20; i++) {
            RobotBlueprint selected = Blueprints.random(random);

            assertNotNull(selected);

            boolean found = false;

            for (RobotBlueprint blueprint : Blueprints.all()) {
                if (blueprint.getName().equals(selected.getName())) {
                    found = true;
                }
            }

            assertTrue(found);
        }
    }

    @Test
    public void testRandomRejectsNull() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Blueprints.random(null)
        );
    }
}
