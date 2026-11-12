package robotassembly;

import java.util.Arrays;

/** Proves that the model runs with no view/controller. */
public final class ModelDriver {
    private ModelDriver() { }
    public static void main(String[] args) {
        GameModel model = new GameModel(Blueprints.all().get(0), Arrays.asList(
            Part.HEAD, Part.LEFT_ARM, Part.RIGHT_ARM, Part.LEFT_LEG, Part.RIGHT_LEG));
        assertResult(PlacementResult.CORRECT_DISCARD, model.discardPart());
        assertResult(PlacementResult.CORRECT, model.placePart(2));
        assertResult(PlacementResult.CORRECT, model.placePart(3));
        assertResult(PlacementResult.CORRECT, model.placePart(4));
        assertResult(PlacementResult.CORRECT, model.placePart(5));
        if (!model.isFinished() || model.getScore() != 45)
            throw new AssertionError("Model did not finish correctly");
        System.out.println("Standalone model PASS; final score = " + model.getScore());
    }
    private static void assertResult(PlacementResult expected, PlacementResult actual) {
        if (expected != actual) throw new AssertionError("Expected " + expected + " but got " + actual);
    }
}
