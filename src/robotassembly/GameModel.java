package robotassembly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Standalone game rules. No Scanner, System.out, or view dependencies. */
public class GameModel {
    public static final int CORRECT_POINTS = 10;
    public static final int DISCARD_POINTS = 5;
    public static final int WRONG_PENALTY = 5;

    private final RobotBlueprint blueprint;
    private final Robot robot;
    private final LinkedQueue<Part> queue = new LinkedQueue<>();
    private int score;

    /** Uses all five unique parts, including parts that must be discarded. */
    public GameModel(RobotBlueprint blueprint, List<Part> arrivalOrder) {
        if (blueprint == null || arrivalOrder == null || arrivalOrder.size() != Part.values().length)
            throw new IllegalArgumentException("Provide a blueprint and all five parts");
        for (Part part : Part.values()) {
            if (Collections.frequency(arrivalOrder, part) != 1)
                throw new IllegalArgumentException("Every part must appear exactly once");
        }
        this.blueprint = blueprint;
        this.robot = new Robot(blueprint);
        for (Part part : arrivalOrder) queue.enqueue(part);
    }
    public static GameModel randomGame(Random random) {
        if (random == null) throw new IllegalArgumentException("Random required");
        RobotBlueprint selected = Blueprints.random(random);
        List<Part> parts = new ArrayList<>();
        Collections.addAll(parts, Part.values());
        Collections.shuffle(parts, random);
        return new GameModel(selected, parts);
    }
    public String getClue() { return blueprint.getClue(); }
    public String getRobotName() { return blueprint.getName(); }
    public int getScore() { return score; }
    public int remainingParts() { return queue.size(); }
    public Part getCurrentPart() { return queue.isEmpty() ? null : queue.peek(); }
    public Part placedAt(int position) { return robot.placedAt(position); }
    public boolean isFinished() { return queue.isEmpty() && robot.isComplete(); }

    public PlacementResult placePart(int position) {
        if (queue.isEmpty()) return PlacementResult.GAME_OVER;
        if (!robot.validPosition(position)) return PlacementResult.INVALID_SPOT;
        if (robot.isOccupied(position)) return PlacementResult.OCCUPIED;
        Part current = queue.peek();
        if (!robot.fits(current, position)) {
            score -= WRONG_PENALTY;
            return PlacementResult.WRONG;
        }
        robot.place(current, position);
        queue.dequeue();
        score += CORRECT_POINTS;
        return PlacementResult.CORRECT;
    }
    public PlacementResult discardPart() {
        if (queue.isEmpty()) return PlacementResult.GAME_OVER;
        if (blueprint.needs(queue.peek())) {
            score -= WRONG_PENALTY;
            return PlacementResult.WRONG_DISCARD;
        }
        queue.dequeue();
        score += DISCARD_POINTS;
        return PlacementResult.CORRECT_DISCARD;
    }
}
