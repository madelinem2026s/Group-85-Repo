
package robotassembly;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Two-player Robot Assembly Battle GUI. The BattleModel handles health, damage,
 * rounds, turns, knockouts, and winner determination. The GUI handles display
 * and user interaction.
 */
public class RobotBattleGUI
    extends JFrame
{

    private static final long serialVersionUID = 1L;

    private static final Color BACKGROUND = new Color(20, 26, 49);

    private static final Color PANEL = new Color(38, 45, 78);

    private static final Color LAVENDER = new Color(194, 170, 255);

    private static final Color MINT = new Color(153, 238, 220);

    private static final Color WHITE = new Color(244, 242, 255);

    private static final int MAX_HEALTH = BattleModel.MAX_HEALTH;

    private static final Path HISTORY_FILE =
        Paths.get(System.getProperty("user.home"), "robot_battle_history.txt");

    private final Random random = new Random();

    // Assembly model and controller
    private GameModel model;
    private GameController controller;

    // Battle model and controller
    private BattleModel battleModel = new BattleModel();

    private BattleController battleController =
        new BattleController(battleModel);

    // Timer
    private GameTimer gameTimer;
    private Timer swingTimer;

    private int score = 0;
    private boolean turnActive = false;

    private String previousRobotName = null;

    private final StringBuilder currentBattleHistory = new StringBuilder();

    // Stats
    private final JLabel roundLabel = new JLabel();
    private final JLabel turnLabel = new JLabel();
    private final JLabel timerLabel = new JLabel();
    private final JLabel multiplierLabel = new JLabel();
    private final JLabel scoreLabel = new JLabel();
    private final JLabel damageLabel = new JLabel();

    // Riddle and queue
    private final JLabel clueLabel = new JLabel();
    private final JLabel partLabel = new JLabel();
    private final JLabel remainingLabel = new JLabel();
    private final JLabel feedbackLabel = new JLabel();

    // Health displays
    private final JLabel[] hpLabels = { new JLabel(), new JLabel() };

    private final JProgressBar[] hpBars =
        { new JProgressBar(0, MAX_HEALTH), new JProgressBar(0, MAX_HEALTH) };

    // Buttons
    private final JButton[] positionButtons = new JButton[5];

    private final JButton discardButton = new JButton("Discard Part");

    private final JButton nextButton = new JButton("Start Player 1 Turn");

    private final JButton historyButton = new JButton("Game History");

    private final JButton replayButton = new JButton("Play Again");

    private final RobotPanel robotPanel = new RobotPanel();

    // ==================================================
    // CONSTRUCTOR
    // ==================================================

    public RobotBattleGUI()
    {

        setTitle("Robot Assembly Battle");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1240, 860);

        setMinimumSize(new Dimension(980, 710));

        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(14, 14));

        root.setBackground(BACKGROUND);

        root.setBorder(new EmptyBorder(16, 18, 16, 18));

        setContentPane(root);

        // ---------------- HEADER ----------------

        JPanel top = new JPanel(new BorderLayout(10, 10));

        top.setOpaque(false);

        JLabel title =
            new JLabel("✦ Robot Assembly Battle ✦", SwingConstants.CENTER);

        styleLabel(title, 30, WHITE);

        top.add(title, BorderLayout.NORTH);

        // ---------------- HEALTH BARS ----------------

        JPanel hpRow = new JPanel(new GridLayout(1, 2, 14, 0));

        hpRow.setOpaque(false);

        for (int i = 0; i < 2; i++)
        {

            JPanel hpPanel = new JPanel(new BorderLayout(4, 5));

            hpPanel.setBackground(PANEL);

            hpPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

            styleLabel(hpLabels[i], 16, WHITE);

            hpBars[i].setValue(MAX_HEALTH);

            hpBars[i].setForeground(i == 0 ? MINT : LAVENDER);

            hpBars[i].setBackground(BACKGROUND);

            hpBars[i].setStringPainted(false);

            hpPanel.add(hpLabels[i], BorderLayout.NORTH);

            hpPanel.add(hpBars[i], BorderLayout.CENTER);

            hpRow.add(hpPanel);
        }

        top.add(hpRow, BorderLayout.CENTER);

        root.add(top, BorderLayout.NORTH);

        // ---------------- MAIN AREA ----------------

        JPanel middle = new JPanel(new BorderLayout(15, 0));

        middle.setOpaque(false);

        robotPanel.setPreferredSize(new Dimension(540, 520));

        middle.add(robotPanel, BorderLayout.CENTER);

        JPanel right = new JPanel(new BorderLayout(0, 12));

        right.setOpaque(false);

        right.setPreferredSize(new Dimension(405, 520));

        // ---------------- STAT PANELS ----------------

        JPanel stats = new JPanel(new GridLayout(3, 2, 8, 8));

        stats.setOpaque(false);

        stats.setPreferredSize(new Dimension(405, 206));

        stats.add(stat("ROUND", roundLabel));

        stats.add(stat("CURRENT PLAYER", turnLabel));

        stats.add(stat("TIME", timerLabel));

        stats.add(stat("MULTIPLIER", multiplierLabel));

        stats.add(stat("SCORE", scoreLabel));

        stats.add(stat("DAMAGE DEALT", damageLabel));

        right.add(stats, BorderLayout.NORTH);

        // ---------------- RIDDLE PANEL ----------------

        JPanel riddleBox = new JPanel(new BorderLayout(10, 14));

        riddleBox.setBackground(PANEL);

        riddleBox.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel riddleTitle = new JLabel("✦ ROBOT RIDDLE");

        styleLabel(riddleTitle, 18, LAVENDER);

        riddleBox.add(riddleTitle, BorderLayout.NORTH);

        styleLabel(clueLabel, 15, WHITE);

        clueLabel.setVerticalAlignment(SwingConstants.TOP);

        riddleBox.add(clueLabel, BorderLayout.CENTER);

        JPanel queuePanel = new JPanel(new GridLayout(2, 1, 0, 9));

        queuePanel.setOpaque(false);

        styleLabel(partLabel, 16, MINT);

        styleLabel(remainingLabel, 14, LAVENDER);

        queuePanel.add(partLabel);

        queuePanel.add(remainingLabel);

        riddleBox.add(queuePanel, BorderLayout.SOUTH);

        right.add(riddleBox, BorderLayout.CENTER);

        middle.add(right, BorderLayout.EAST);

        root.add(middle, BorderLayout.CENTER);

        // ---------------- BOTTOM CONTROLS ----------------

        JPanel bottom = new JPanel(new BorderLayout(10, 10));

        bottom.setOpaque(false);

        styleLabel(feedbackLabel, 15, MINT);

        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);

        bottom.add(feedbackLabel, BorderLayout.NORTH);

        JPanel placementButtons = new JPanel(new GridLayout(1, 6, 9, 0));

        placementButtons.setOpaque(false);

        for (int i = 0; i < 5; i++)
        {

            final int position = i + 1;

            JButton button = new JButton("" + position);

            positionButtons[i] = button;

            button.addActionListener(e -> handlePlacement(position));

            placementButtons.add(button);
        }

        discardButton.addActionListener(e -> handleDiscard());

        placementButtons.add(discardButton);

        JPanel controlRows = new JPanel(new GridLayout(2, 1, 0, 10));

        controlRows.setOpaque(false);

        controlRows.add(placementButtons);

        JPanel extraButtons = new JPanel(new GridLayout(1, 3, 10, 0));

        extraButtons.setOpaque(false);

        nextButton.addActionListener(e -> startTurn());

        historyButton.addActionListener(e -> showGameHistory());

        replayButton.addActionListener(e -> replayGame());

        extraButtons.add(nextButton);

        extraButtons.add(historyButton);

        extraButtons.add(replayButton);

        controlRows.add(extraButtons);

        bottom.add(controlRows, BorderLayout.CENTER);

        JLabel note = new JLabel(
            "Place the next part in slots 1–5, "
                + "or discard it if it is not needed.",
            SwingConstants.CENTER);

        note.setForeground(LAVENDER);

        bottom.add(note, BorderLayout.SOUTH);

        root.add(bottom, BorderLayout.SOUTH);

        initializeDisplay();
    }

    // ==================================================
    // STYLING
    // ==================================================


    private void styleLabel(JLabel label, int size, Color color)
    {

        label.setFont(new Font("SansSerif", Font.BOLD, size));

        label.setForeground(color);
    }


    private JPanel stat(String title, JLabel value)
    {

        JPanel panel = new JPanel(new BorderLayout(2, 5));

        panel.setBackground(PANEL);

        panel.setBorder(new EmptyBorder(9, 8, 9, 8));

        JLabel heading = new JLabel(title, SwingConstants.CENTER);

        styleLabel(heading, 12, LAVENDER);

        styleLabel(value, 22, WHITE);

        value.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(heading, BorderLayout.NORTH);

        panel.add(value, BorderLayout.CENTER);

        return panel;
    }

    // ==================================================
    // INITIAL DISPLAY
    // ==================================================


    private void initializeDisplay()
    {

        refreshHealth();

        updateRoundLabels();

        timerLabel.setText("60.0 s");

        multiplierLabel.setText("4.0x");

        scoreLabel.setText("0");

        damageLabel.setText("0");

        clueLabel.setText(
            "<html><div style='width:270px'>" + "Press Start Player 1 Turn "
                + "to see your first riddle." + "</div></html>");

        partLabel.setText("NEXT PART: —");

        remainingLabel.setText("PARTS REMAINING: —");

        feedbackLabel.setText(
            "Player 1 begins. " + "Each player takes one turn per difficulty.");

        setPlacementEnabled(false);
    }

    // ==================================================
    // MODEL-DRIVEN DISPLAYS
    // ==================================================


    private void updateRoundLabels()
    {

        roundLabel.setText(
            battleModel.getRound() + " / 3 — " + battleModel.getDifficulty());

        turnLabel.setText("Player " + battleModel.getCurrentPlayer());
    }


    private void refreshHealth()
    {

        for (int i = 0; i < 2; i++)
        {

            int hp = battleModel.getHealth(i + 1);

            hpLabels[i].setText(
                "PLAYER " + (i + 1) + " — " + hp + " / " + MAX_HEALTH + " HP");

            hpBars[i].setValue(hp);
        }
    }


    private void setPlacementEnabled(boolean enabled)
    {

        for (JButton button : positionButtons)
        {
            button.setEnabled(enabled);
        }

        discardButton.setEnabled(enabled);
    }

    // ==================================================
    // START PLAYER TURN
    // ==================================================


    private void startTurn()
    {

        if (turnActive || battleModel.isFinished())
        {

            return;
        }

        if (swingTimer != null)
        {
            swingTimer.stop();
        }

        int player = battleModel.getCurrentPlayer();

        // Create a new assembly model.
        model = GameModel.randomGame(random);

        // Player 1 always goes first.
        // Player 2 receives a different robot
        // from Player 1 in the same round.

        if (player == 1)
        {

            previousRobotName = model.getRobotName();

        }
        else
        {

            int attempts = 0;

            while (model.getRobotName().equals(previousRobotName)
                && attempts < 100)
            {

                model = GameModel.randomGame(random);

                attempts++;
            }
        }

        controller = new GameController(model);

        gameTimer = new GameTimer(battleModel.getDifficulty());

        score = 0;

        turnActive = true;

        scoreLabel.setText("0");

        damageLabel.setText("0");

        nextButton.setEnabled(false);

        replayButton.setEnabled(false);

        setPlacementEnabled(true);

        updateRoundLabels();

        updateGameDisplay();

        updateTimerDisplay();

        feedbackLabel.setText("Player " + player + ": assemble your robot!");

        swingTimer = new Timer(100, e -> {

            gameTimer.tick();

            updateTimerDisplay();

            if (gameTimer.isFinished())
            {
                finishTurn();
            }
        });

        swingTimer.start();
    }

    // ==================================================
    // TIMER DISPLAY
    // ==================================================


    private void updateTimerDisplay()
    {

        double seconds = gameTimer.getTimeRemaining();

        timerLabel.setText(
            seconds > 0
                ? String.format("%.1f s", seconds)
                : String.valueOf((int)Math.ceil(seconds)));

        multiplierLabel
            .setText(String.format("%.1fx", gameTimer.getMultiplier()));
    }

    // ==================================================
    // ASSEMBLY DISPLAY
    // ==================================================


    private void updateGameDisplay()
    {

        if (model == null)
        {
            return;
        }

        score = model.getScore();

        scoreLabel.setText("" + score);

        clueLabel.setText(
            "<html><div style='width:270px'>" + getDifficultyClue()
                + "</div></html>");

        Part current = model.getCurrentPart();

        partLabel.setText(
            "NEXT PART: " + (current == null ? "None" : current.toString()));

        remainingLabel.setText("PARTS REMAINING: " + model.remainingParts());

        robotPanel.repaint();
    }

    // ==================================================
    // PLACEMENT ACTIONS
    // ==================================================


    private void handlePlacement(int position)
    {

        if (!turnActive)
        {
            return;
        }

        PlacementResult result = controller.placePart(position);

        showFeedback(result);

        updateGameDisplay();

        if (model.isFinished())
        {
            finishTurn();
        }
    }


    private void handleDiscard()
    {

        if (!turnActive)
        {
            return;
        }

        PlacementResult result = controller.discardPart();

        showFeedback(result);

        updateGameDisplay();

        if (model.isFinished())
        {
            finishTurn();
        }
    }


    private void showFeedback(PlacementResult result)
    {

        switch (result)
        {

            case CORRECT:
                feedbackLabel.setText("Correct placement! +10 points");
                break;

            case CORRECT_DISCARD:
                feedbackLabel.setText("Correct discard! +10 points");
                break;

            case WRONG:
                feedbackLabel.setText("Wrong position! -5 points");
                break;

            case WRONG_DISCARD:
                feedbackLabel.setText("That part is needed! -5 points");
                break;

            case OCCUPIED:
                feedbackLabel.setText("That slot is occupied!");
                break;

            case INVALID_SPOT:
                feedbackLabel.setText("Invalid slot!");
                break;

            case GAME_OVER:
                feedbackLabel.setText("Robot already finished!");
                break;

            default:
                feedbackLabel.setText("Try again!");
        }
    }

    // ==================================================
    // FINISH PLAYER TURN
    // ==================================================


    private void finishTurn()
    {

        if (!turnActive)
        {
            return;
        }

        swingTimer.stop();

        turnActive = false;

        setPlacementEnabled(false);

        int finishedPlayer = battleModel.getCurrentPlayer();

        int finishedRound = battleModel.getRound();

        String finishedDifficulty = battleModel.getDifficulty();

        String robotName = model.getRobotName();

        double multiplier = gameTimer.getMultiplier();

        // Damage, health, turns, and winner
        // are handled by the battle controller/model.
        int damage =
            battleController.completeTurn(finishedPlayer, score, multiplier);

        damageLabel.setText("" + damage);

        refreshHealth();

        // Record the completed turn.
        currentBattleHistory.append(
            "Round " + finishedRound + " (" + finishedDifficulty + ")\n");

        currentBattleHistory.append("Player: " + finishedPlayer + "\n");

        currentBattleHistory.append("Robot: " + robotName + "\n");

        currentBattleHistory.append("Score: " + score + "\n");

        currentBattleHistory
            .append("Multiplier: " + String.format("%.1fx", multiplier) + "\n");

        currentBattleHistory.append("Damage dealt: " + damage + "\n");

        currentBattleHistory
            .append("Player 1 HP: " + battleModel.getHealth(1) + "\n");

        currentBattleHistory
            .append("Player 2 HP: " + battleModel.getHealth(2) + "\n\n");

        // The model checks for knockouts
        // only after both players finish.
        if (battleModel.isFinished())
        {

            finishBattle(battleModel.getWinner());

            return;
        }

        updateRoundLabels();

        int nextPlayer = battleModel.getCurrentPlayer();

        nextButton.setText("Start Player " + nextPlayer + " Turn");

        nextButton.setEnabled(true);

        replayButton.setEnabled(true);

        feedbackLabel.setText(
            "Player " + finishedPlayer + " dealt " + damage
                + " damage. Pass the computer to Player " + nextPlayer + ".");

        clueLabel.setText(
            "<html><div style='width:270px'>"
                + "Next player's riddle will appear "
                + "when their turn starts." + "</div></html>");

        partLabel.setText("NEXT PART: —");

        remainingLabel.setText("PARTS REMAINING: —");

        model = null;

        robotPanel.repaint();
    }

    // ==================================================
    // FINISH BATTLE
    // ==================================================


    private void finishBattle(String message)
    {

        nextButton.setText("Battle Finished");

        nextButton.setEnabled(false);

        replayButton.setEnabled(true);

        feedbackLabel.setText(message);

        currentBattleHistory.append("FINAL RESULT: " + message + "\n");

        currentBattleHistory
            .append("Final Player 1 HP: " + battleModel.getHealth(1) + "\n");

        currentBattleHistory
            .append("Final Player 2 HP: " + battleModel.getHealth(2) + "\n");

        saveBattleHistory();

        JOptionPane.showMessageDialog(
            this,
            message + "\nPlayer 1: " + battleModel.getHealth(1) + " HP"
                + "\nPlayer 2: " + battleModel.getHealth(2) + " HP",
            "Battle Complete",
            JOptionPane.INFORMATION_MESSAGE);
    }

    // ==================================================
    // SAVE GAME HISTORY
    // ==================================================


    private void saveBattleHistory()
    {

        String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("MMM d, yyyy - h:mm a"));

        String entry = "====================================\n"
            + "ROBOT ASSEMBLY BATTLE\n" + "Played: " + timestamp + "\n"
            + "====================================\n\n"
            + currentBattleHistory.toString() + "\n\n";

        try
        {

            Files.write(
                HISTORY_FILE,
                entry.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);

        }
        catch (IOException e)
        {

            JOptionPane.showMessageDialog(
                this,
                "Could not save game history:\n" + e.getMessage(),
                "History Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==================================================
    // VIEW GAME HISTORY
    // ==================================================


    private void showGameHistory()
    {

        String savedHistory = "";

        try
        {

            if (Files.exists(HISTORY_FILE))
            {

                savedHistory = new String(
                    Files.readAllBytes(HISTORY_FILE),
                    StandardCharsets.UTF_8);
            }

        }
        catch (IOException e)
        {

            JOptionPane.showMessageDialog(
                this,
                "Could not load game history:\n" + e.getMessage(),
                "History Error",
                JOptionPane.ERROR_MESSAGE);

            return;
        }

        StringBuilder display = new StringBuilder();

        display.append("========== PREVIOUS BATTLES ==========\n\n");

        if (savedHistory.trim().isEmpty())
        {

            display.append("No completed battles saved yet.\n\n");

        }
        else
        {

            display.append(savedHistory);
        }

        if (currentBattleHistory.length() > 0 && !battleModel.isFinished())
        {

            display.append("\n========== CURRENT BATTLE ==========\n\n");

            display.append(currentBattleHistory.toString());
        }

        JTextArea historyArea = new JTextArea(display.toString());

        historyArea.setEditable(false);

        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        historyArea.setBackground(BACKGROUND);

        historyArea.setForeground(WHITE);

        historyArea.setBorder(new EmptyBorder(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(historyArea);

        scrollPane.setPreferredSize(new Dimension(650, 520));

        JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "Game History",
            JOptionPane.PLAIN_MESSAGE);
    }

    // ==================================================
    // PLAY AGAIN
    // ==================================================


    private void replayGame()
    {

        if (turnActive)
        {
            return;
        }

        if (!battleModel.isFinished() && currentBattleHistory.length() > 0)
        {

            int choice = JOptionPane.showConfirmDialog(
                this,
                "Restart the battle?\n" + "The current unfinished battle "
                    + "will be cleared.",
                "Play Again",
                JOptionPane.YES_NO_OPTION);

            if (choice != JOptionPane.YES_OPTION)
            {
                return;
            }
        }

        if (swingTimer != null)
        {
            swingTimer.stop();
        }

        // Create a completely fresh battle model.
        battleModel = new BattleModel();

        battleController = new BattleController(battleModel);

        score = 0;

        turnActive = false;

        previousRobotName = null;

        currentBattleHistory.setLength(0);

        model = null;

        controller = null;

        gameTimer = null;

        nextButton.setText("Start Player 1 Turn");

        nextButton.setEnabled(true);

        replayButton.setEnabled(true);

        initializeDisplay();

        robotPanel.repaint();
    }

    // ==================================================
    // DIFFICULTY-SPECIFIC RIDDLES
    // ==================================================


    private String getDifficultyClue()
    {

        String robot = model.getRobotName();

        String difficulty = battleModel.getDifficulty();

        if ("Headless".equalsIgnoreCase(robot))
        {

            switch (difficulty)
            {

                case "Medium":
                    return "I can walk and reach, but "
                        + "nothing sits above my shoulders. "
                        + "My limbs remain where you expect them.";

                case "Hard":
                    return "Four limbs serve my frame, "
                        + "yet no crown rests above. "
                        + "My paths and grasp remain unchanged.";

                default:
                    return "This robot has no head. "
                        + "Discard the head. All other parts "
                        + "go in their normal positions.";
            }
        }

        if ("Armless".equalsIgnoreCase(robot))
        {

            switch (difficulty)
            {

                case "Medium":
                    return "I can see and travel, but "
                        + "I have nothing with which " + "to reach or grasp.";

                case "Hard":
                    return "A mind crowns my frame, "
                        + "and two travelers carry me. "
                        + "Yet no branches extend to touch the world.";

                default:
                    return "This robot has a head and "
                        + "both legs, but no arms. " + "Discard both arms.";
            }
        }

        if ("Legless".equalsIgnoreCase(robot))
        {

            switch (difficulty)
            {

                case "Medium":
                    return "I can observe and reach, "
                        + "but I have no limbs to carry "
                        + "me across the ground.";

                case "Hard":
                    return "My crown remains and my "
                        + "branches still grasp. Yet beneath "
                        + "my frame, no travelers await.";

                default:
                    return "This robot has a head and "
                        + "both arms, but no legs. " + "Discard both legs.";
            }
        }

        if ("Swapped".equalsIgnoreCase(robot))
        {

            switch (difficulty)
            {

                case "Medium":
                    return "My head rests where my left "
                        + "foot belongs, and my left foot "
                        + "rises to the highest place. "
                        + "Everything else stays unchanged.";

                case "Hard":
                    return "The thinker descends to the "
                        + "lower left, while its traveler "
                        + "claims the throne above. "
                        + "The remaining three keep their homes.";

                default:
                    return "Swap the head and left leg positions. "
                        + "Put the left leg in slot 1 and " + "head in slot 4. "
                        + "All other parts stay normal.";
            }
        }

        if ("Mixed".equalsIgnoreCase(robot))
        {

            switch (difficulty)
            {

                case "Medium":
                    return "One reaching limb has climbed "
                        + "to where thought usually lives. "
                        + "Its old place is empty. "
                        + "The other limbs remain unchanged.";

                case "Hard":
                    return "A branch ascends to wear the crown, "
                        + "leaving its eastern birthplace bare. "
                        + "The true crown is cast away, "
                        + "while the western branch and "
                        + "both travelers remain.";

                default:
                    return "Put the right arm in slot 1 "
                        + "instead of the head. "
                        + "Keep the left arm and both legs normal. "
                        + "Discard the head.";
            }
        }

        return model.getClue();
    }

    // ==================================================
    // ROBOT DRAWING
    // ==================================================

    private class RobotPanel
        extends JPanel
    {

        private static final long serialVersionUID = 1L;

        RobotPanel()
        {
            setOpaque(false);
        }


        @Override
        protected void paintComponent(Graphics graphics)
        {

            super.paintComponent(graphics);

            if (model == null)
            {
                return;
            }

            Graphics2D g = (Graphics2D)graphics.create();

            g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

            double scale = Math.min(getWidth() / 450.0, getHeight() / 520.0);

            g.translate(
                (getWidth() - 450 * scale) / 2,
                (getHeight() - 520 * scale) / 2);

            g.scale(scale, scale);

            // Assembly platform
            g.setColor(new Color(100, 91, 180, 90));

            g.fillOval(45, 455, 360, 55);

            g.setColor(LAVENDER);

            g.setStroke(new BasicStroke(3));

            g.drawOval(65, 460, 320, 35);

            // Robot torso
            g.setPaint(
                new GradientPaint(
                    165,
                    190,
                    new Color(226, 220, 255),
                    290,
                    350,
                    new Color(112, 106, 189)));

            g.fillRoundRect(155, 190, 140, 170, 55, 55);

            g.setColor(new Color(96, 90, 165));

            g.setStroke(new BasicStroke(4));

            g.drawRoundRect(155, 190, 140, 170, 55, 55);

            drawHeart(g, 225, 270);

            drawSlot(g, 1, 225, 122);

            drawSlot(g, 2, 115, 255);

            drawSlot(g, 3, 335, 255);

            drawSlot(g, 4, 185, 410);

            drawSlot(g, 5, 265, 410);

            g.dispose();
        }


        private void drawSlot(Graphics2D g, int position, int x, int y)
        {

            Part placed = model.placedAt(position);

            if (placed == null)
            {

                drawEmptySlot(g, position, x, y);

                return;
            }

            switch (placed)
            {

                case HEAD:
                    drawHead(g, x, y);
                    break;

                case LEFT_ARM:
                case RIGHT_ARM:
                    drawArm(g, x, y, placed == Part.LEFT_ARM);
                    break;

                case LEFT_LEG:
                case RIGHT_LEG:
                    drawLeg(g, x, y);
                    break;

                default:
                    drawEmptySlot(g, position, x, y);
                    break;
            }
        }


        private void drawEmptySlot(Graphics2D g, int position, int x, int y)
        {

            g.setColor(new Color(174, 163, 235, 165));

            g.setStroke(
                new BasicStroke(
                    3,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND,
                    1,
                    new float[] { 7, 8 },
                    0));

            if (position == 1)
            {

                g.drawRoundRect(x - 58, y - 50, 116, 100, 45, 45);
            }
            else if (position == 2 || position == 3)
            {

                g.drawRoundRect(x - 30, y - 65, 60, 130, 35, 35);
            }
            else
            {

                g.drawRoundRect(x - 27, y - 55, 54, 110, 28, 28);
            }

            g.setFont(new Font("SansSerif", Font.BOLD, 16));

            centerText(g, "" + position, x, y + 5);
        }


        private void drawHead(Graphics2D g, int x, int y)
        {

            // Antenna
            g.setColor(LAVENDER);

            g.setStroke(new BasicStroke(6));

            g.drawLine(x, y - 53, x, y - 73);

            g.setColor(MINT);

            g.fillOval(x - 8, y - 85, 16, 16);

            // Ears
            g.setColor(new Color(151, 131, 225));

            g.fillOval(x - 77, y - 23, 27, 49);

            g.fillOval(x + 50, y - 23, 27, 49);

            // Head
            g.setPaint(
                new GradientPaint(
                    x - 55,
                    y - 50,
                    new Color(248, 241, 255),
                    x + 55,
                    y + 50,
                    new Color(159, 150, 226)));

            g.fillRoundRect(x - 60, y - 52, 120, 104, 55, 55);

            // Face screen
            g.setColor(new Color(31, 39, 75));

            g.fillRoundRect(x - 48, y - 36, 96, 72, 38, 38);

            // Eyes
            g.setColor(MINT);

            g.setStroke(
                new BasicStroke(
                    5,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND));

            g.drawArc(x - 32, y - 5, 25, 20, 15, 150);

            g.drawArc(x + 7, y - 5, 25, 20, 15, 150);

            // Blush
            g.setColor(new Color(241, 150, 214));

            g.fillOval(x - 35, y + 15, 13, 6);

            g.fillOval(x + 22, y + 15, 13, 6);
        }


        private void drawArm(Graphics2D g, int x, int y, boolean left)
        {

            g.setPaint(
                new GradientPaint(
                    x - 30,
                    y - 65,
                    new Color(232, 226, 255),
                    x + 30,
                    y + 65,
                    new Color(131, 123, 206)));

            g.fillRoundRect(x - 27, y - 60, 54, 120, 30, 30);

            g.setColor(new Color(111, 101, 181));

            g.setStroke(new BasicStroke(3));

            g.drawRoundRect(x - 27, y - 60, 54, 120, 30, 30);

            // Joint
            g.setColor(LAVENDER);

            g.fillOval(x - 19, y - 54, 38, 38);

            // Hand
            g.setColor(MINT);

            g.fillOval(x - 13, y + 43, 26, 26);

            g.setFont(new Font("SansSerif", Font.BOLD, 11));

            g.setColor(new Color(45, 49, 85));

            centerText(g, left ? "L" : "R", x, y + 5);
        }


        private void drawLeg(Graphics2D g, int x, int y)
        {

            g.setPaint(
                new GradientPaint(
                    x - 26,
                    y - 55,
                    new Color(230, 224, 255),
                    x + 26,
                    y + 60,
                    new Color(131, 124, 207)));

            g.fillRoundRect(x - 25, y - 55, 50, 112, 25, 25);

            g.setColor(new Color(106, 99, 175));

            g.setStroke(new BasicStroke(3));

            g.drawRoundRect(x - 25, y - 55, 50, 112, 25, 25);

            // Knee
            g.setColor(LAVENDER);

            g.fillOval(x - 15, y - 2, 30, 30);

            // Foot
            g.setColor(new Color(201, 189, 252));

            g.fillRoundRect(x - 31, y + 43, 62, 23, 15, 15);
        }


        private void drawHeart(Graphics2D g, int x, int y)
        {

            Path2D.Double heart = new Path2D.Double();

            heart.moveTo(x, y + 26);

            heart.curveTo(x - 55, y - 7, x - 19, y - 35, x, y - 11);

            heart.curveTo(x + 19, y - 35, x + 55, y - 7, x, y + 26);

            heart.closePath();

            g.setColor(new Color(134, 242, 228, 80));

            g.setStroke(new BasicStroke(10));

            g.draw(heart);

            g.setColor(MINT);

            g.fill(heart);
        }


        private void centerText(Graphics2D g, String text, int x, int y)
        {

            FontMetrics fm = g.getFontMetrics();

            g.drawString(text, x - fm.stringWidth(text) / 2, y);
        }
    }

    // ==================================================
    // MAIN
    // ==================================================

    public static void main(String[] args)
    {

        SwingUtilities.invokeLater(() -> new RobotBattleGUI().setVisible(true));
    }
}
