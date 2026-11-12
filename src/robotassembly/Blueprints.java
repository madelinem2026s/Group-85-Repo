package robotassembly;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/** Easy-to-read clues for the Deliverable 3 prototype. */
public final class Blueprints {
    private Blueprints() { }
    public static List<RobotBlueprint> all() {
        return Arrays.asList(
            new RobotBlueprint("Headless", "I have no head. All my arms and legs are normal.",
                new Part[] {null, Part.LEFT_ARM, Part.RIGHT_ARM, Part.LEFT_LEG, Part.RIGHT_LEG}),
            new RobotBlueprint("Armless", "I have a head and legs, but no arms.",
                new Part[] {Part.HEAD, null, null, Part.LEFT_LEG, Part.RIGHT_LEG}),
            new RobotBlueprint("Legless", "I have a head and arms, but no legs.",
                new Part[] {Part.HEAD, Part.LEFT_ARM, Part.RIGHT_ARM, null, null}),
            new RobotBlueprint("Swapped", "My head belongs at my left leg position; my left leg belongs at my head position.",
                new Part[] {Part.LEFT_LEG, Part.LEFT_ARM, Part.RIGHT_ARM, Part.HEAD, Part.RIGHT_LEG}),
            new RobotBlueprint("Mixed", "My right arm belongs where my head normally goes. I have no head; my right-arm slot stays empty.",
                new Part[] {Part.RIGHT_ARM, Part.LEFT_ARM, null, Part.LEFT_LEG, Part.RIGHT_LEG})
        );
    }
    public static RobotBlueprint random(Random random) {
        if (random == null) throw new IllegalArgumentException("Random cannot be null");
        List<RobotBlueprint> options = all();
        return options.get(random.nextInt(options.size()));
    }
}
