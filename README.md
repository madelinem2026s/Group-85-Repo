# Group 85

# Robot Assembly Factory

## Project Overview

Our project is a robot assembly game where players build different types of robots by placing parts into the correct positions. The parts are stored in a FIFO (First In, First Out) queue, meaning players must work with the part at the front of the queue before moving on to the next one.

There are five possible robot configurations: Headless, Armless, Legless, Swapped, and Mixed. Each robot has different requirements, and players must use a riddle to figure out where each part belongs. Players can choose between Easy, Medium, and Hard difficulty, which changes how obvious the riddles are.

Players earn 10 points for correct placements and 10 points for correctly discarding unnecessary parts. Incorrect attempts result in a 5-point deduction.

For Deliverable 4, we added a two-player robot battle system. Players take turns assembling robots, and their scores and time multipliers determine how much damage they deal to the opposing player. Both players begin with 1,000 health points, and the game has three rounds with increasing difficulty.

Player 1 always goes first, but both players are allowed to complete their turns before the game checks for a winner. This allows for ties if both players reach zero health in the same round. If neither player is knocked out after three rounds, the player with more remaining health wins.

The game also includes a timer, a graphical interface, game history, and an option to play again.

## How to Run the Model

The model contains the main logic for our robot assembly game, including the queue, robot configurations, placement validation, scoring, and multiplayer battle rules.

To test the model independently of the GUI:

1. Open the Robot Assembly project in Eclipse.
2. Locate the `robotassembly` package.
3. Right-click a JUnit test class.
4. Select **Run As → JUnit Test**.
5. Check the JUnit window to make sure all tests pass.

Our project includes the following JUnit test classes:

- `LinkedQueueTest.java` — Tests FIFO queue operations and edge cases.
- `GameModelTest.java` — Tests game logic, scoring, and placement results.
- `BlueprintsTest.java` — Tests the different robot configurations.
- `DifficultyCluesTest.java` — Tests difficulty-based clues.
- `ConsoleViewTest.java` — Tests console output and feedback.
- `GameControllerTest.java` — Tests input processing and difficulty selection.
- `PartTest.java` — Tests robot part types and labels.
- `RobotTest.java` — Tests placement validation, occupied positions, and robot completion.
- `RobotBlueprintTest.java` — Tests blueprint creation, required parts, and invalid configurations.
- `PlacementResultTest.java` — Tests the possible placement results.
- `RobotAssemblyMainTest.java` — Tests console application startup and end-of-input handling.
- `BattleModelTest.java` — Tests health, damage, round progression, knockouts, ties, and invalid inputs.
- `BattleControllerTest.java` — Tests controller interactions, player turns, and invalid battle actions.

For Deliverable 4, we added 28 JUnit 5 tests for the new battle model and controller. All 28 tests passed successfully in Eclipse.

These tests allow us to verify that the multiplayer battle rules work independently of the graphical interface.

## How to Run the View

### Single-Player Robot Assembly

1. Open the Robot Assembly project in Eclipse.
2. Open the `robotassembly` package.
3. Right-click `RobotAssemblyGUI.java`.
4. Select **Run As → Java Application**.
5. Select Easy, Medium, or Hard difficulty.
6. Read the robot riddle and determine where each part belongs.
7. Use the numbered buttons to place parts or the Discard button to remove unnecessary parts.
8. Continue until the queue is empty and the robot is complete.

The single-player GUI also includes Replay and Game History buttons.

### Two-Player Robot Battle

1. Open the Robot Assembly project in Eclipse.
2. Open the `robotassembly` package.
3. Right-click `RobotBattleGUI.java`.
4. Select **Run As → Java Application**.
5. Click **Start Player 1 Turn**.
6. Read the riddle and place or discard parts to assemble the robot.
7. Complete the robot before the timer runs out to earn a higher damage multiplier.
8. Once Player 1 finishes, the game calculates damage and updates Player 2's health.
9. Click **Start Player 2 Turn** and complete the second player's assembly.
10. Continue through Easy, Medium, and Hard rounds until a winner is determined.

The GUI displays each player's health, the current round, remaining time, multiplier, score, and damage dealt.

Players can also view Game History and use Play Again to start a new battle.

The assembly model handles scoring, queue processing, and placement validation. The battle model handles health, damage, player turns, rounds, and winner determination. The GUI displays this information and collects player input through the controllers.

## Reflection

### What Changed From Our Original Specification?

Our overall idea has stayed the same since our original specification. We are still creating a game where players assemble robots using parts from a queue, and they gain or lose points depending on whether they place the parts correctly.

However, we made several changes as we started implementing the project.

One of the main changes was adding multiple robot configurations instead of having just one standard robot. We created five different robot types, each with different requirements for which parts are needed and where they belong. We decided to do this because we wanted the game to have more variety and be more interesting to replay.

We also added three difficulty levels. Easy mode gives more direct instructions, while Medium and Hard modes use less obvious riddles. This was added to make the game more challenging without needing to change the underlying placement rules.

Another change was creating a graphical interface rather than relying only on a simple text-based display. The GUI allows players to see the robot being assembled and select positions using buttons. We wanted players to be able to visualize their progress instead of only reading text.

We also added a Discard button because certain robot configurations do not require every part. This required us to handle both correct and incorrect discards in addition to normal placements.

For validation, we included checks for invalid positions, already occupied positions, incorrect placements, and attempts to discard parts that are actually needed. These checks were important because we wanted players to be able to make mistakes without crashing the game.

### Changes Made for Deliverable 4

For Deliverable 4, we implemented our planned two-player robot battle feature. Players now take turns assembling robots, and their assembly scores are used to calculate damage against the opposing player. We also added a timer and three difficulty rounds, with different time limits and damage multipliers.

One major change from our original plan was how the battle system works. We originally planned to have players unlock different attacks depending on their scores. Instead, we decided to use their scores and time multipliers to calculate damage directly. This made the battle system simpler while still rewarding players for performing well during assembly.

We added `BattleModel.java` to handle player health, damage, rounds, turn progression, and determining the winner. We also added `BattleController.java` to connect the battle model to the GUI.

Originally, we considered adding health and battle status directly to `Robot.java` and expanding `GameModel.java` to handle multiplayer. Instead, we decided to create a separate battle model so that the existing assembly logic would not need major changes.

We updated `RobotBattleGUI.java` to display player health, battle information, and the results of each turn. The GUI now gets battle information from the model instead of calculating the battle rules itself.

We also added `GameTimer.java` to manage the time limits and multipliers for each difficulty.

Audio was also added for background music, correct placements, and incorrect placements.

### Did the Model Work Independently?

Yes, the model can be tested independently of the graphical interface.

The `GameModel` class handles the game's scoring, queue processing, and placement rules, while `BattleModel` handles the multiplayer battle state.

We created JUnit tests that interact directly with these model classes, which allows us to check their behavior without opening the game window.

One important part of connecting the model and GUI was making sure that player actions updated the correct information. For example, after a player finishes assembling their robot, the GUI sends their score and time multiplier through `BattleController`. The battle model calculates the damage, updates the opposing player's health, and determines whose turn comes next.

The GUI then reads the updated values from the battle model and displays them.

This separation makes it easier to test the battle rules and change the appearance of the game without rewriting the core logic.

### What Happened to Our Unknowns?

One of our original unknowns was how players would know where to place each part. We decided to solve this by adding robot-specific riddles and numbered positions. This allows players to figure out the correct placements themselves instead of simply being told the answer every time.

Another unknown was how to handle parts that do not belong on certain robots. We solved this by adding a discard option that rewards correct discards and penalizes incorrect ones.

We also needed to decide how to handle incorrect attempts. We implemented a system where incorrect placements subtract points but do not remove the part from the queue. This allows players to try again until they find the correct position.

For Deliverable 4, another challenge was deciding how multiplayer turns and knockouts should work. We decided that Player 1 would always go first in each round, but Player 2 would still get their turn even if their health reached zero. This makes the battle fairer because both players have the opportunity to deal damage before a winner is determined.

We also decided to use a timer to reward players who finish assembling their robots more quickly.

### What Doesn't Work Yet?

Our current game supports robot assembly, difficulty selection, two-player battles, timers, damage calculations, game history, and replaying the game.

We have implemented the main multiplayer features we planned. However, we have not added selectable attacks or more advanced battle animations.

Instead of unlocking different attacks, players currently deal damage based on their assembly scores and time multipliers.

We plan to focus on testing and improving the features we already implemented rather than adding more complicated battle mechanics.

### What Would We Change in the Future?

One improvement we could make in the future would be to create a shared game-state interface that the GUI could use to display both assembly and battle information.

Currently, the GUI interacts with the assembly model and battle model separately. A shared interface could make it easier to add new game modes without having to change as much of the GUI.

We could also create a separate class for managing the overall flow between assembly turns and battle rounds. This would make it easier to add new features, such as different attack types or additional players.

## Next Steps

### Remaining Work

Our next priority is making sure the current game is fully tested and ready for the final demonstration.

1. **Finish testing the existing game.** Run all JUnit tests and check that the assembly and battle features work correctly together.
2. **Review the controller and GUI interaction.** Make sure input handling and game updates follow our intended MVC structure.
3. **Check multiplayer gameplay.** Test player turns, damage calculations, knockouts, ties, and round progression.
4. **Improve the GUI if necessary.** Fix any remaining display issues or small gameplay problems.
5. **Prepare the final demonstration.** Make sure we can show the new multiplayer feature and explain how it connects to the model.

### Completed Features

For Deliverable 4, we completed the following features from our previous Next Steps:

- Two-player multiplayer mode
- Turn-based robot battles
- Separate assembly turns for each player
- Health and damage calculations
- Three difficulty rounds
- Timer and damage multipliers
- Battle completion and winner determination
- JUnit tests for the new battle model and controller

### Stretch Goals

**Features we completed:**

- Two-player multiplayer mode
- Turn-based robot battles
- Damage based on assembly performance
- Additional testing for the battle system

**Features we changed:**

- Instead of unlocking different attacks, assembly scores and time multipliers directly determine damage.

**Features we are postponing:**

- Selectable attacks
- Additional robot configurations beyond the existing five
- More advanced battle animations

We want to focus on making the existing multiplayer system reliable before adding more features.

### Three-Minute Demo Plan

1. **0:00–0:30 — Introduction:** Open `RobotBattleGUI.java` and explain the robot assembly game, the FIFO queue, and the new multiplayer feature.
2. **0:30–1:15 — Player 1:** Start Player 1's turn and demonstrate placing parts, discarding unnecessary parts, and earning points.
3. **1:15–2:00 — Battle mechanics:** Explain how the assembly score and time multiplier determine damage. Show the updated health bars and begin Player 2's turn.
4. **2:00–2:40 — Round progression:** Demonstrate how players take turns and explain how knockouts and ties are determined.
5. **2:40–3:00 — Additional features:** Show Game History and Play Again.

Because a complete battle may take longer than three minutes, we will use a prepared demonstration or explain some of the later rounds rather than playing through the entire game.

### Work Ownership

| Remaining Task | Owner |
|---|---|
| Review queue and robot model behavior | Rushil |
| Review scoring and game logic | Randy |
| Review GUI for multiplayer and battles | Bryan and Madeline |
| Review multiplayer input and turn handling | Madeline |
| Review battle rules | Randy and Rushil |
| Run additional tests | All group members |
| Final integration and debugging | All group members |
| Prepare final demonstration | All group members |

These assignments are based on our previous plan and should be confirmed with the group.

## Work Attribution

The following table documents who worked on each file, how they contributed, and whether GenAI was used.

| File | Contributor(s) | Contribution |
|---|---|---|
| `Part.java` | Rushil | Implemented the Part model; GenAI-assisted |
| `LinkedQueue.java` | Rushil | Implemented the linked queue and its operations; GenAI-assisted |
| `Robot.java` | Rushil | Implemented robot state and placement logic; GenAI-assisted |
| `PlacementResult.java` | Bryan | Created placement result definitions; GenAI-assisted |
| `GameModel.java` | Randy | Implemented game logic and scoring; GenAI-assisted |
| `ConsoleView.java` | Bryan, Madeline | Bryan implemented the console view; Madeline revised difficulty-related output and tested the changes; GenAI-assisted |
| `GameController.java` | Madeline | Implemented and revised input processing, controller logic, and difficulty selection; GenAI-assisted |
| `RobotBlueprint.java` | Rushil | Implemented robot configuration representation; GenAI-assisted |
| `Blueprints.java` | Madeline | Implemented robot configurations and clues; GenAI-assisted |
| `DifficultyClues.java` | Madeline | Implemented and tested difficulty-based clues; GenAI-assisted |
| `RobotAssemblyGUI.java` | Madeline | Implemented, revised, and manually tested the single-player GUI; GenAI-assisted |
| `RobotBattleGUI.java` | Madeline | Implemented multiplayer GUI and connected it to the battle model and controller; GenAI-assisted |
| `GameTimer.java` | Rushil | Implemented time limits and damage multipliers; GenAI-assisted |
| `BattleModel.java` | Randy | Implemented player health, damage, turns, rounds, and winner determination; GenAI-assisted |
| `BattleController.java` | Bryan | Connected battle actions to the model; GenAI-assisted |
| `RobotAssemblyMain.java` | Rushil | Implemented console application startup; GenAI-assisted |
| `LinkedQueueTest.java` | Madeline | Created, executed, and reviewed JUnit tests; GenAI-assisted |
| `GameModelTest.java` | Madeline | Created, executed, and reviewed JUnit tests; GenAI-assisted |
| `BlueprintsTest.java` | Madeline | Created, executed, and reviewed JUnit tests; GenAI-assisted |
| `DifficultyCluesTest.java` | Madeline | Created, executed, and reviewed JUnit tests; GenAI-assisted |
| `ConsoleViewTest.java` | Bryan | Created, executed, and reviewed JUnit tests; GenAI-assisted |
| `GameControllerTest.java` | Madeline | Created, executed, and reviewed JUnit tests; GenAI-assisted |
| `PartTest.java` | Rushil | Created JUnit tests for part types and labels; GenAI-assisted |
| `RobotTest.java` | Madeline | Created JUnit tests for robot placement and completion; GenAI-assisted |
| `RobotBlueprintTest.java` | Madeline | Created JUnit tests for blueprint validation and required parts; GenAI-assisted |
| `PlacementResultTest.java` | Madeline | Created JUnit tests for placement result values; GenAI-assisted |
| `RobotAssemblyMainTest.java` | Madeline | Created JUnit tests for console startup and input handling; GenAI-assisted |
| `BattleModelTest.java` | Rushil, Bryan | Created and ran 21 JUnit 5 tests for battle logic; GenAI-assisted |
| `BattleControllerTest.java` | Madeline, Randy | Created and ran 7 JUnit 5 tests for controller behavior; GenAI-assisted |
| `SoundManager.java` | Madeline| Created sound manager to implement into GUI; GenAI-assisted |


## GenAI Note

We used ChatGPT as a tool to help with certain parts of the development process.

GenAI was used to help develop and revise the graphical interface, including the layout, visual robot, difficulty display, and user feedback. We also used it to help troubleshoot errors and improve how the interface displays information.

We used GenAI to help generate JUnit tests for our test classes. We ran the tests in Eclipse and revised them when necessary to match the actual implementation.

For Deliverable 4, we also used ChatGPT to help develop the multiplayer battle model, controller, and graphical interface. GenAI assisted with generating JUnit 5 tests for the new battle classes. We ran these tests in Eclipse, and all 28 passed successfully.

We also manually tested the multiplayer GUI to check that the battle features worked correctly.

GenAI was also used to help organize our README and check that our documentation addressed the project rubric.

We reviewed the AI-assisted work and tested the relevant functionality rather than assuming that the generated code was correct.