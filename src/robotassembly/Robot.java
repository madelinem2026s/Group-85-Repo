package robotassembly;

/** Tracks filled positions without any console or view code. */
public class Robot {
    private final RobotBlueprint blueprint;
    private final Part[] placed = new Part[5];
    public Robot(RobotBlueprint blueprint) {
        if (blueprint == null) throw new IllegalArgumentException("Blueprint required");
        this.blueprint = blueprint;
    }
    public boolean validPosition(int position) { return position >= 1 && position <= 5; }
    public boolean isOccupied(int position) {
        if (!validPosition(position)) throw new IllegalArgumentException("Position must be 1-5");
        return placed[position - 1] != null;
    }
    public boolean fits(Part part, int position) {
        return validPosition(position) && blueprint.requiredAt(position) == part;
    }
    public void place(Part part, int position) {
        if (!fits(part, position) || isOccupied(position))
            throw new IllegalArgumentException("Cannot place part in this position");
        placed[position - 1] = part;
    }
    public Part placedAt(int position) {
        if (!validPosition(position)) throw new IllegalArgumentException("Position must be 1-5");
        return placed[position - 1];
    }
    public boolean isComplete() {
        for (int i = 1; i <= 5; i++) {
            if (blueprint.requiredAt(i) != placedAt(i)) return false;
        }
        return true;
    }
}
