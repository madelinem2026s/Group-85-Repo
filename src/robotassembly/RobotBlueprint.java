package robotassembly;

import java.util.Arrays;

/** Immutable five-position target. A null target is an empty slot. */
public class RobotBlueprint {
    private final String name;
    private final String clue;
    private final Part[] required;

    public RobotBlueprint(String name, String clue, Part[] required) {
        if (name == null || clue == null || required == null || required.length != 5)
            throw new IllegalArgumentException("A blueprint needs a name, clue and five slots");
        this.name = name;
        this.clue = clue;
        this.required = Arrays.copyOf(required, required.length);
        // Each physical part appears at most once in a single round.
        for (int i = 0; i < 5; i++) {
            for (int j = i + 1; j < 5; j++) {
                if (required[i] != null && required[i] == required[j])
                    throw new IllegalArgumentException("Duplicate part in blueprint");
            }
        }
    }
    public String getName() { return name; }
    public String getClue() { return clue; }
    public Part requiredAt(int position) {
        if (position < 1 || position > 5) throw new IllegalArgumentException("Position must be 1-5");
        return required[position - 1];
    }
    public boolean needs(Part part) {
        for (Part requiredPart : required) if (requiredPart == part) return true;
        return false;
    }
    public int requiredCount() {
        int count = 0;
        for (Part part : required) if (part != null) count++;
        return count;
    }
}
