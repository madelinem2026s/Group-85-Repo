
package robotassembly;

/**
 * Provides difficulty-based clues for each robot type.
 */
public class DifficultyClues {

    public static String getClue(
        String robot, String difficulty, String fallback) {

        if ("Headless".equalsIgnoreCase(robot)) {
            switch (difficulty) {
                case "Medium":
                    return "I can walk and reach, but nothing sits "
                        + "above my shoulders. My limbs remain "
                        + "where you expect them.";

                case "Hard":
                    return "Four limbs serve my frame, yet no crown "
                        + "rests above. My paths and grasp remain unchanged.";

                default:
                    return "This robot has no head. Discard the head. "
                        + "All other parts go in their normal positions.";
            }
        }

        if ("Armless".equalsIgnoreCase(robot)) {
            switch (difficulty) {
                case "Medium":
                    return "I can see and travel, but I have nothing "
                        + "with which to reach or grasp.";

                case "Hard":
                    return "A mind crowns my frame, and two travelers "
                        + "carry me. Yet no branches extend to touch "
                        + "the world.";

                default:
                    return "This robot has a head and both legs, "
                        + "but no arms. Discard both arms.";
            }
        }

        if ("Legless".equalsIgnoreCase(robot)) {
            switch (difficulty) {
                case "Medium":
                    return "I can observe and reach, but I have no "
                        + "limbs to carry me across the ground.";

                case "Hard":
                    return "My crown remains and my branches still "
                        + "grasp. Yet beneath my frame, no travelers await.";

                default:
                    return "This robot has a head and both arms, "
                        + "but no legs. Discard both legs.";
            }
        }

        if ("Swapped".equalsIgnoreCase(robot)) {
            switch (difficulty) {
                case "Medium":
                    return "My head rests where my left foot belongs, "
                        + "and my left foot rises to the highest place. "
                        + "Everything else stays unchanged.";

                case "Hard":
                    return "The thinker descends to the lower left, "
                        + "while its traveler claims the throne above. "
                        + "The remaining three keep their homes.";

                default:
                    return "Swap the head and left leg positions. "
                        + "Put the left leg in slot 1 and head in slot 4. "
                        + "All other parts stay normal.";
            }
        }

        if ("Mixed".equalsIgnoreCase(robot)) {
            switch (difficulty) {
                case "Medium":
                    return "One reaching limb has climbed to where "
                        + "thought usually lives. Its old place is empty. "
                        + "The other limbs remain unchanged.";

                case "Hard":
                    return "A branch ascends to wear the crown, "
                        + "leaving its eastern birthplace bare. "
                        + "The true crown is cast away, while the "
                        + "western branch and both travelers remain.";

                default:
                    return "Put the right arm in slot 1 instead of "
                        + "the head. Keep the left arm and both legs "
                        + "normal. Discard the head.";
            }
        }

        return fallback;
    }
}
