package robotassembly;

/** A physical part, identified by its type. */
public enum Part {
    HEAD("Head"), LEFT_ARM("Left arm"), RIGHT_ARM("Right arm"),
    LEFT_LEG("Left leg"), RIGHT_LEG("Right leg");

    private final String label;
    Part(String label) { this.label = label; }
    @Override public String toString() { return label; }
}
