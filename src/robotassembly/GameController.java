
package robotassembly;

import java.util.Scanner;

/** Translates console commands into model operations. */
public class GameController
{

    private final GameModel model;
    private final ConsoleView view;
    private final Scanner input;

    public GameController(GameModel model, ConsoleView view, Scanner input)
    {

        if (model == null || view == null || input == null)
        {
            throw new IllegalArgumentException(
                "Model, view and input are required");
        }

        this.model = model;
        this.view = view;
        this.input = input;
    }


    public void run()
    {
        String difficulty = chooseDifficulty();

        if (difficulty == null)
        {
            return;
        }

        view.showWelcome(model, difficulty);

        while (!model.isFinished() && model.remainingParts() > 0)
        {
            view.showTurn(model);

            if (!input.hasNextLine())
            {
                System.out.println("Input ended. Game stopped safely.");
                return;
            }

            String command = input.nextLine().trim();

            if (command.equalsIgnoreCase("discard"))
            {
                view.showResult(model.discardPart());
            }
            else
            {
                try
                {
                    int position = Integer.parseInt(command);
                    view.showResult(model.placePart(position));
                }
                catch (NumberFormatException exception)
                {
                    view.showInvalidInput();
                }
            }
        }

        if (model.isFinished())
        {
            view.showFinished(model);
        }
    }


    /**
     * Lets the console player select a difficulty.
     */
    private String chooseDifficulty()
    {
        while (true)
        {
            view.showDifficultyMenu();

            if (!input.hasNextLine())
            {
                System.out.println("Input ended. Game stopped safely.");
                return null;
            }

            String choice = input.nextLine().trim();

            switch (choice)
            {
                case "1":
                    return "Easy";

                case "2":
                    return "Medium";

                case "3":
                    return "Hard";

                default:
                    view.showInvalidDifficulty();
            }
        }
    }


    /**
     * Constructor used by the GUI.
     */
    public GameController(GameModel model)
    {
        if (model == null)
        {
            throw new IllegalArgumentException("Model is required");
        }

        this.model = model;
        this.view = null;
        this.input = null;
    }


    /**
     * Places the current part into a robot position.
     */
    public PlacementResult placePart(int position)
    {
        return model.placePart(position);
    }


    /**
     * Discards the current part.
     */
    public PlacementResult discardPart()
    {
        return model.discardPart();
    }
}
