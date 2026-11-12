package robotassembly;

import java.util.Random;
import java.util.Scanner;

/** Play the console game. */
public final class RobotAssemblyMain {
    private RobotAssemblyMain() { }
    public static void main(String[] args) {
        GameModel model = GameModel.randomGame(new Random());
        new GameController(model, new ConsoleView(), new Scanner(System.in)).run();
    }
}
