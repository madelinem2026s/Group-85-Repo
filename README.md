
# Group 85
# Robot Assembly Factory

## Project Overview

Our project is a robot assembly game where players build different types of robots by placing parts into the correct positions. The parts are stored in a FIFO (First In, First Out) queue, meaning players must work with the part at the front of the queue before moving on to the next one.

There are five possible robot configurations: Headless, Armless, Legless, Swapped, and Mixed. Each robot has different requirements, and players must use a riddle to figure out where each part belongs. Players can choose between Easy, Medium, and Hard difficulty, which changes how obvious the riddles are.

Players earn 10 points for correct placements and 5 points for correctly discarding unnecessary parts. Incorrect attempts result in a 5-point deduction. Once all parts have been processed, the game ends and displays the final score.

## How to Run the Model

The model contains the main logic for our robot assembly game, including the queue, robot configurations, placement validation, and scoring.

To test the model independently of the GUI:

1. Open the Robot Assembly project in Eclipse.
2. Locate the `robotassembly` package.
3. Right-click `GameModelTest.java`.
4. Select **Run As → JUnit Test**.
5. The tests should run and display their results in the JUnit window.

The model can also be tested using `LinkedQueueTest.java` and `BlueprintsTest.java`.

These tests check the queue operations, game logic, and robot configurations without requiring the graphical interface.

## How to Run the View

1. Open the Robot Assembly project in Eclipse.
2. Open the `robotassembly` package.
3. Right-click `RobotAssemblyGUI.java`.
4. Select **Run As → Java Application**.
5. Select Easy, Medium, or Hard difficulty.
6. Read the robot riddle and determine where each part belongs.
7. Use the numbered buttons to place parts or the Discard button to remove unnecessary parts.
8. Continue until the queue is empty and the robot is complete.

The GUI also includes a Replay button for starting another round and a Game History button for viewing completed rounds.

The game logic is handled by the model, while the GUI displays the robot, score, current part, and feedback.

## Reflection

### What Changed From Our Original Specification

Our overall idea has stayed the same since our original specification. We are still creating a game where players assemble robots using parts from a queue, and they gain or lose points depending on whether they place the parts correctly.

However, we made several changes as we started implementing the project.

One of the main changes was adding multiple robot configurations instead of having just one standard robot. We created five different robot types, each with different requirements for which parts are needed and where they belong. We decided to do this because we wanted the game to have more variety and be more interesting to replay.

We also added three difficulty levels. Easy mode gives more direct instructions, while Medium and Hard modes use less obvious riddles. This was added to make the game more challenging without needing to change the underlying placement rules.

Another change was creating a graphical interface rather than relying only on a simple text-based display. The GUI allows players to see the robot being assembled and select positions using buttons. We wanted players to be able to visualize their progress instead of only reading text.

We also added a Discard button because certain robot configurations do not require every part. This required us to handle both correct and incorrect discards in addition to normal placements.

For validation, we included checks for invalid positions, already occupied positions, incorrect placements, and attempts to discard parts that are actually needed. These checks were important because we wanted players to be able to make mistakes without crashing the game.

Finally, we added JUnit tests for our queue, game model, and robot blueprints. These tests help us verify that the core logic works correctly without needing to run the GUI.

### Did the Model Work Independently?

Yes, the model can be tested independently of the graphical interface.

The `GameModel` class handles the game's scoring, queue processing, and placement rules, while the GUI is responsible for displaying the game and collecting user input.

We created JUnit tests that interact directly with the model classes, which allowed us to check their behavior without opening the game window.

This separation should make it easier to add new features or change the appearance of the game without rewriting the core logic.

### What Happened to Our Unknowns?

One of our original unknowns was how players would know where to place each part. We decided to solve this by adding robot-specific riddles and numbered positions. This allows players to figure out the correct placements themselves instead of simply being told the answer every time.

Another unknown was how to handle parts that do not belong on certain robots. We solved this by adding a discard option that rewards correct discards and penalizes incorrect ones.

We also needed to decide how to handle incorrect attempts. We implemented a system where incorrect placements subtract points but do not remove the part from the queue. This allows players to try again until they find the correct position.

One feature that is still undecided is whether to add a timer. We have discussed potentially tracking how long players take to assemble a robot and using that time to affect their final score, but we have not implemented this yet.

### What Doesn't Work Yet?

The current prototype supports assembling a robot, selecting difficulty, earning points, discarding parts, and replaying the game.

However, our planned multiplayer and battle features have not been implemented yet.

Currently, the game is designed around a single player completing one robot at a time. We still need to add a system that allows two players to compete against each other.

We also have not added a timer or a system that uses assembly performance to unlock different attacks.

These are features we plan to consider for future versions of the project.

## Next Steps

### Remaining Work

Our next priority is making sure the current prototype is fully tested and that the model, controller, and view are organized correctly.

After that, we plan to expand the game with additional features.

1. **Finish testing the existing game.** Review the model and GUI for errors and make sure all expected inputs are handled correctly. Add more tests if necessary.

2. **Review the controller and GUI interaction.** Make sure input handling and game updates follow our intended MVC structure.

3. **Add a second player.** Allow two players to assemble their own robots and record their scores.

4. **Implement a battle system.** After both players finish assembling their robots, they will enter a turn-based battle.

5. **Connect assembly scores to attacks.** Players who earn higher scores during assembly will unlock stronger or additional attacks during the battle.

6. **Test the multiplayer and battle features.** Make sure turns, scoring, and game completion work correctly.

### Planned Non-Trivial Change: Two-Player Robot Battles

One major feature we want to add is a multiplayer mode where two players compete against each other.

Each player would first assemble a robot using the existing queue system. Their assembly score would determine which attacks they could use in a battle afterward.

Once both players finish assembling their robots, they would take turns attacking until one robot wins.

This would make the assembly portion of the game more meaningful because players would have an incentive to earn higher scores.

### Classes and Methods That May Change

**GameModel.java**

- `getScore()` would be used to determine which attacks a player can unlock.
- `placePart(int)` and `discardPart()` should continue handling assembly actions.
- We may need to add methods for tracking player turns and transitioning between assembly and battle phases.

**Robot.java**

- We may need to add fields for robot health and battle status.
- New methods could include `takeDamage(int)` and `isDefeated()`.

**RobotAssemblyGUI.java**

- `updateDisplay()` would need to show the current player, battle status, and available attacks.
- `newGame()` would need to support starting a multiplayer game.
- We would need additional buttons for selecting attacks.

**New BattleController.java**

- A possible new controller class could manage player turns and battle actions.
- Methods could include `startBattle()`, `performAttack()`, and `nextTurn()`.

**Classes That Should Not Need to Change**

- `LinkedQueue.java` should remain unchanged because it already handles the FIFO behavior needed for assembly.
- `Part.java` should remain unchanged because the existing robot parts would still be used.
- `Blueprints.java` and `RobotBlueprint.java` should not require major changes because the existing robot configurations can still be used.

These method names are proposed additions rather than methods that are already implemented.

### Stretch Goals

**Features we plan to keep:**

- Two-player multiplayer mode
- Turn-based robot battles
- Attacks unlocked based on assembly score
- Additional testing for new gameplay features

**Features we may postpone or drop:**

- A timer that affects scoring
- Additional robot configurations beyond the existing five
- More advanced battle animations

We want to focus on making the multiplayer and battle systems work correctly before adding extra features.

### Additional Tests Needed

We plan to add tests for:

- Two players completing separate assembly rounds
- Scores being stored correctly for each player
- Attack availability based on assembly score
- Damage being applied correctly during battles
- Players alternating turns
- The battle ending when a robot is defeated
- Invalid battle actions being rejected without crashing the game

### Work Ownership

| Remaining Task | Owner |
|---|---|
| Review queue and robot model behavior | Rushil |
| Review scoring and game logic | Randy |
| Update GUI for multiplayer and battles | Bryan and Madeline |
| Add multiplayer input and turn handling | Madeline |
| Implement battle rules | Randy and Rushil |
| Write and run additional tests | All group members |
| Final integration and debugging | All group members |

These are proposed assignments and should be confirmed with the group.

## Work Attribution

The following table documents the files in our project and the work completed on them.

| File | Contributor(s) | Contribution |
|---|---|---|
| `Part.java` | Rushil | Model development |
| `LinkedQueue.java` | Rushil | Queue implementation |
| `Robot.java` | Rushil | Robot state and placement logic |
| `PlacementResult.java` | Bryan | Placement result definitions |
| `GameModel.java` | Randy | Game logic and scoring |
| `GameState.java` | Randy | Read-only game state interface |
| `GameListener.java` | Bryan | Model update notifications |
| `GameView.java` | Bryan | View interface |
| `ConsoleView.java` | Bryan | Console-based view |
| `GameController.java` | Madeline | Input processing and controller logic |
| `Main.java` | Madeline | Application startup |
| `RobotBlueprint.java` | Rushil | Robot configuration representation |
| `Blueprints.java` | Madeline | Robot configurations and clues |
| `RobotAssemblyGUI.java` | Madeline | GUI implementation, revisions, and testing; GenAI-assisted |
| `LinkedQueueTest.java` | Madeline | GenAI-assisted JUnit tests; executed and revised |
| `GameModelTest.java` | Madeline | GenAI-assisted JUnit tests; executed and reviewed |
| `BlueprintsTest.java` | Madeline | GenAI-assisted JUnit tests; executed and reviewed |

*This table is a draft based on our original division of work. We will update it to reflect who actually wrote, revised, reviewed, and tested each file before submission. We will also add any other files included in the final project.*

## GenAI Note

We used ChatGPT as a tool to help with certain parts of the development process.

GenAI was used to help develop and revise the graphical interface, including the layout, visual robot, difficulty display, and user feedback. We also used it to help troubleshoot errors and improve how the interface displays information.

We used GenAI to help generate JUnit tests for `LinkedQueue`, `GameModel`, and `Blueprints`. We ran the tests in Eclipse and revised them when necessary to match the actual implementation.

GenAI was also used to help organize our README and check that our documentation addressed the project rubric.

We reviewed the AI-assisted work and tested the relevant functionality rather than assuming that the generated code was correct.


