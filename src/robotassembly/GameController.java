package robotassembly;

import java.util.Scanner;

/** Translates console commands into model operations. */
public class GameController {
    private final GameModel model;
    private final ConsoleView view;
    private final Scanner input;
    public GameController(GameModel model, ConsoleView view, Scanner input) {
        if (model == null || view == null || input == null)
            throw new IllegalArgumentException("Model, view and input are required");
        this.model = model;
        this.view = view;
        this.input = input;
    }
    public void run() {
        view.showWelcome(model);
        while (!model.isFinished() && model.remainingParts() > 0) {
            view.showTurn(model);
            if (!input.hasNextLine()) {
                System.out.println("Input ended. Game stopped safely.");
                return;
            }
            String command = input.nextLine().trim();
            if (command.equalsIgnoreCase("discard")) {
                view.showResult(model.discardPart());
            }
            else {
                try { view.showResult(model.placePart(Integer.parseInt(command))); }
                catch (NumberFormatException exception) { view.showInvalidInput(); }
            }
        }
        if (model.isFinished()) view.showFinished(model);
    }
    public GameController(GameModel model) {
        if (model == null) {
            throw new IllegalArgumentException("Model is required");
        }

        this.model = model;
        this.view = null;
        this.input = null;
    }
    public PlacementResult placePart(int position) {
        return model.placePart(position);
    }

    public PlacementResult discardPart() {
        return model.discardPart();
    }
}
