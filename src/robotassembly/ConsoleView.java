package robotassembly;

/** Thin output-only view: no game rules or direct queue access. */
public class ConsoleView {
    public void showWelcome(GameModel model) {
        System.out.println("=== ROBOT ASSEMBLY ===");
        System.out.println("Clue: " + model.getClue());
        System.out.println("Positions: 1 Head | 2 Left arm | 3 Right arm | 4 Left leg | 5 Right leg");
        System.out.println("Type 1-5 to place the current part, or 'discard' if unwanted.");
        System.out.println("Correct +10 | correct discard +5 | wrong -5");
    }
    public void showTurn(GameModel model) {
        System.out.println("\nPart: " + model.getCurrentPart() + " | Remaining: "
            + model.remainingParts() + " | Score: " + model.getScore());
        for (int position = 1; position <= 5; position++) {
            Part placed = model.placedAt(position);
            System.out.println(position + ": " + (placed == null ? "[empty]" : placed));
        }
        System.out.print("Your action: ");
    }
    public void showResult(PlacementResult result) {
        switch (result) {
            case CORRECT: System.out.println("Correct placement! +10"); break;
            case CORRECT_DISCARD: System.out.println("Correct discard! +5"); break;
            case WRONG: System.out.println("Wrong position. -5; try this part again."); break;
            case WRONG_DISCARD: System.out.println("This part is needed! -5; try again."); break;
            case OCCUPIED: System.out.println("That position is already occupied. Try again."); break;
            case INVALID_SPOT: System.out.println("Enter a position from 1 to 5."); break;
            case GAME_OVER: System.out.println("No parts remain."); break;
            default: throw new IllegalStateException("Unexpected result");
        }
    }
    public void showInvalidInput() { System.out.println("Enter 1-5 or 'discard'."); }
    public void showFinished(GameModel model) {
        System.out.println("\nRobot completed! Final score: " + model.getScore());
    }
}
