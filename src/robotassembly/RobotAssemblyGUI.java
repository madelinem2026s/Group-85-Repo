
package robotassembly;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/** Pastel companion-robot interface. Game rules remain in GameModel. */
public class RobotAssemblyGUI
    extends JFrame
{
    private static final long serialVersionUID = 1L;
    private static final Color BACKGROUND = new Color(20, 26, 49);
    private static final Color PANEL = new Color(38, 45, 78);
    private static final Color LAVENDER = new Color(194, 170, 255);
    private static final Color MINT = new Color(153, 238, 220);
    private static final Color WHITE = new Color(244, 242, 255);
    private static final Color MUTED = new Color(174, 181, 213);

    private GameModel model;
    private GameController controller;
    private final Random random = new Random();
    private final ArrayList<String> history = new ArrayList<>();
    private String difficulty = "Easy";
    private boolean roundRecorded;

    private final JLabel modeLabel = new JLabel();
    private final JLabel clueLabel = new JLabel();
    private final JLabel scoreLabel = new JLabel();
    private final JLabel partLabel = new JLabel();
    private final JLabel remainingLabel = new JLabel();
    private final JLabel feedbackLabel = new JLabel();

    private final JButton[] positionButtons = new JButton[5];
    private final JButton discardButton = new SoftButton("Discard Part", true);
    private final RobotPanel robotPanel = new RobotPanel();

    public RobotAssemblyGUI()
    {
        setTitle("Robot Assembly Factory");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1120, 790);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(14, 14));
        root.setBackground(BACKGROUND);
        root.setBorder(new EmptyBorder(18, 20, 18, 20));
        setContentPane(root);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("✦ Robot Assembly Factory ✦");
        title.setFont(new Font("Serif", Font.BOLD, 32));
        title.setForeground(WHITE);
        header.add(title, BorderLayout.NORTH);

        JLabel subtitle = new JLabel("Build  •  Assemble  •  Complete");
        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        header.add(subtitle, BorderLayout.SOUTH);
        root.add(header, BorderLayout.NORTH);

        JPanel middle = new JPanel(new BorderLayout(14, 14));
        middle.setOpaque(false);
        root.add(middle, BorderLayout.CENTER);

        RoundedPanel left = new RoundedPanel();
        left.setPreferredSize(new Dimension(265, 450));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(new EmptyBorder(20, 18, 20, 18));

        JLabel modeTitle = new JLabel("✧  DIFFICULTY");
        styleLabel(modeTitle, 14, LAVENDER, true);
        left.add(modeTitle);
        left.add(Box.createVerticalStrut(10));

        styleLabel(modeLabel, 22, WHITE, true);
        left.add(modeLabel);
        left.add(Box.createVerticalStrut(28));

        JLabel scoreTitle = new JLabel("✧  YOUR SCORE");
        styleLabel(scoreTitle, 14, LAVENDER, true);
        left.add(scoreTitle);
        left.add(Box.createVerticalStrut(10));

        styleLabel(scoreLabel, 38, MINT, true);
        left.add(scoreLabel);
        left.add(Box.createVerticalStrut(28));

        JLabel remainingTitle = new JLabel("✧  PARTS LEFT");
        styleLabel(remainingTitle, 14, LAVENDER, true);
        left.add(remainingTitle);
        left.add(Box.createVerticalStrut(10));

        styleLabel(remainingLabel, 27, WHITE, true);
        left.add(remainingLabel);
        left.add(Box.createVerticalStrut(28));

        JLabel hint = new JLabel(
            "<html>Choose a numbered position<br>"
                + "or discard an unneeded part.</html>");
        styleLabel(hint, 13, MUTED, false);
        left.add(hint);

        middle.add(left, BorderLayout.WEST);

        JPanel robotArea = new JPanel(new BorderLayout());
        robotArea.setOpaque(false);
        robotArea.add(robotPanel, BorderLayout.CENTER);
        middle.add(robotArea, BorderLayout.CENTER);

        RoundedPanel right = new RoundedPanel();
        right.setPreferredSize(new Dimension(330, 450));
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(new EmptyBorder(20, 18, 20, 18));

        JLabel riddleTitle = new JLabel("✦  ROBOT RIDDLE");
        styleLabel(riddleTitle, 16, LAVENDER, true);
        right.add(riddleTitle);
        right.add(Box.createVerticalStrut(14));

        styleLabel(clueLabel, 15, WHITE, false);
        clueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        clueLabel.setVerticalAlignment(SwingConstants.TOP);

        JPanel cluePanel = new JPanel(new BorderLayout());
        cluePanel.setOpaque(false);
        cluePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cluePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        cluePanel.setPreferredSize(new Dimension(280, 180));
        cluePanel.add(clueLabel, BorderLayout.CENTER);

        right.add(cluePanel);
        right.add(Box.createVerticalStrut(12));

        JLabel nextTitle = new JLabel("✦  NEXT PART IN QUEUE");
        styleLabel(nextTitle, 15, LAVENDER, true);
        right.add(nextTitle);
        right.add(Box.createVerticalStrut(12));

        styleLabel(partLabel, 22, MINT, true);
        right.add(partLabel);
        right.add(Box.createVerticalStrut(24));

        JLabel queueHint = new JLabel(
            "<html>FIFO: Only the front part can<br>"
                + "be placed or discarded.</html>");
        styleLabel(queueHint, 13, MUTED, false);
        right.add(queueHint);

        middle.add(right, BorderLayout.EAST);

        RoundedPanel bottom = new RoundedPanel();
        bottom.setLayout(new BorderLayout(12, 12));
        bottom.setBorder(new EmptyBorder(14, 18, 14, 18));

        JLabel instruction =
            new JLabel("Place the current part in a position:");
        styleLabel(instruction, 15, WHITE, true);
        bottom.add(instruction, BorderLayout.NORTH);

        JPanel controls = new JPanel(new GridLayout(1, 6, 10, 0));
        controls.setOpaque(false);

        for (int i = 0; i < 5; i++)
        {
            final int position = i + 1;
            JButton button = new SoftButton("" + position, false);
            positionButtons[i] = button;

            button.addActionListener(
                e -> handleResult(controller.placePart(position)));

            controls.add(button);
        }

        discardButton
            .addActionListener(e -> handleResult(controller.discardPart()));
        controls.add(discardButton);

        bottom.add(controls, BorderLayout.CENTER);

        JPanel bottomFooter = new JPanel(new BorderLayout(10, 10));
        bottomFooter.setOpaque(false);

        styleLabel(feedbackLabel, 14, MINT, true);
        bottomFooter.add(feedbackLabel, BorderLayout.NORTH);

        JPanel menu = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        menu.setOpaque(false);

        JButton replay = new SoftButton("↻ Replay / New Robot", false);
        JButton historyButton = new SoftButton("Game History", false);

        replay.addActionListener(e -> chooseDifficulty());
        historyButton.addActionListener(e -> showHistory());

        menu.add(replay);
        menu.add(historyButton);
        bottomFooter.add(menu, BorderLayout.SOUTH);

        bottom.add(bottomFooter, BorderLayout.SOUTH);
        root.add(bottom, BorderLayout.SOUTH);

        modeLabel.setText(difficulty + " Mode");
        chooseDifficulty();
    }


    private void styleLabel(JLabel label, int size, Color color, boolean bold)
    {
        label.setFont(
            new Font("SansSerif", bold ? Font.BOLD : Font.PLAIN, size));
        label.setForeground(color);
    }


    private void chooseDifficulty()
    {
        String[] options = { "Easy", "Medium", "Hard" };

        String choice = (String)JOptionPane.showInputDialog(
            this,
            "Choose your difficulty:",
            "Robot Assembly - Difficulty",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            difficulty);

        if (choice != null)
        {
            difficulty = choice;
        }

        newGame();
    }


    private void newGame()
    {
        model = GameModel.randomGame(random);
        controller = new GameController(model);
        roundRecorded = false;
        feedbackLabel.setText("✧ Read the riddle and assemble your robot!");
        updateDisplay();
    }


    private void handleResult(PlacementResult result)
    {
        switch (result)
        {
            case CORRECT:
                feedbackLabel.setText("✓ Correct placement! +10 points");
                break;
            case CORRECT_DISCARD:
                feedbackLabel.setText("✓ Correct discard! +5 points");
                break;
            case WRONG:
                feedbackLabel.setText("✗ Wrong position! -5 points");
                break;
            case WRONG_DISCARD:
                feedbackLabel.setText("✗ This part is needed! -5 points");
                break;
            case OCCUPIED:
                feedbackLabel.setText("That position is already occupied!");
                break;
            case INVALID_SPOT:
                feedbackLabel.setText("Invalid position!");
                break;
            case GAME_OVER:
                feedbackLabel.setText("The game is already finished!");
                break;
            default:
                feedbackLabel.setText("Try again!");
                break;
        }

        if (model.isFinished() && !roundRecorded)
        {
            roundRecorded = true;
            history.add(
                "Round " + (history.size() + 1) + " | Robot: "
                    + model.getRobotName() + " | Difficulty: " + difficulty
                    + " | Score: " + model.getScore());

            feedbackLabel.setText(
                "✦ Robot complete! Final score: " + model.getScore()
                    + ". Click Replay to play again!");
        }

        updateDisplay();
    }


    private void updateDisplay()
    {
        modeLabel.setText(difficulty + " Mode");
        clueLabel.setText(
            "<html><div style='width:200px'>"
            + getDifficultyClue()
            + "</div></html>"
        );

        scoreLabel.setText("" + model.getScore());
        remainingLabel.setText("" + model.remainingParts());

        Part current = model.getCurrentPart();

        if (current == null)
        {
            partLabel.setText("None");
        }
        else
        {
            partLabel.setText(current.toString());
        }

        for (JButton button : positionButtons)
        {
            button.setEnabled(!model.isFinished());
        }

        discardButton.setEnabled(!model.isFinished());
        robotPanel.repaint();
    }


    private String getDifficultyClue()
    {
        String robot = model.getRobotName();

        if ("Headless".equalsIgnoreCase(robot))
        {
            switch (difficulty)
            {
                case "Medium":
                    return "I can walk and reach, but nothing sits "
                        + "above my shoulders. My limbs remain "
                        + "where you expect them.";
                case "Hard":
                    return "Four limbs serve my frame, yet no crown "
                        + "rests above. My paths and grasp remain unchanged.";
                default:
                    return "This robot has no head. Discard the head. "
                        + "All other parts go in their normal positions.";
            }
        }

        if ("Armless".equalsIgnoreCase(robot))
        {
            switch (difficulty)
            {
                case "Medium":
                    return "I can see and travel, but I have nothing "
                        + "with which to reach or grasp.";
                case "Hard":
                    return "A mind crowns my frame, and two travelers "
                        + "carry me. Yet no branches extend to touch "
                        + "the world.";
                default:
                    return "This robot has a head and both legs, "
                        + "but no arms. Discard both arms.";
            }
        }

        if ("Legless".equalsIgnoreCase(robot))
        {
            switch (difficulty)
            {
                case "Medium":
                    return "I can observe and reach, but I have no "
                        + "limbs to carry me across the ground.";
                case "Hard":
                    return "My crown remains and my branches still "
                        + "grasp. Yet beneath my frame, no travelers await.";
                default:
                    return "This robot has a head and both arms, "
                        + "but no legs. Discard both legs.";
            }
        }

        if ("Swapped".equalsIgnoreCase(robot))
        {
            switch (difficulty)
            {
                case "Medium":
                    return "My head rests where my left foot belongs, "
                        + "and my left foot rises to the highest place. "
                        + "Everything else stays unchanged.";
                case "Hard":
                    return "The thinker descends to the lower left, "
                        + "while its traveler claims the throne above. "
                        + "The remaining three keep their homes.";
                default:
                    return "Swap the head and left leg positions. "
                        + "Put the left leg in slot 1 and head in slot 4. "
                        + "All other parts stay normal.";
            }
        }

        if ("Mixed".equalsIgnoreCase(robot))
        {
            switch (difficulty)
            {
                case "Medium":
                    return "One reaching limb has climbed to where "
                        + "thought usually lives. Its old place is empty. "
                        + "The other limbs remain unchanged.";
                case "Hard":
                    return "A branch ascends to wear the crown, "
                        + "leaving its eastern birthplace bare. "
                        + "The true crown is cast away, while the "
                        + "western branch and both travelers remain.";
                default:
                    return "Put the right arm in slot 1 instead of "
                        + "the head. Keep the left arm and both legs "
                        + "normal. Discard the head.";
            }
        }

        return model.getClue();
    }


    private void showHistory()
    {
        if (history.isEmpty())
        {
            JOptionPane.showMessageDialog(
                this,
                "No completed games yet!",
                "Game History",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder text = new StringBuilder();

        for (String record : history)
        {
            text.append(record).append("\n");
        }

        JTextArea area = new JTextArea(text.toString(), 12, 45);
        area.setEditable(false);

        JOptionPane.showMessageDialog(
            this,
            new JScrollPane(area),
            "Game History",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private static class RoundedPanel
        extends JPanel
    {
        private static final long serialVersionUID = 1L;

        RoundedPanel()
        {
            setOpaque(false);
        }


        @Override
        protected void paintComponent(Graphics graphics)
        {
            Graphics2D g = (Graphics2D)graphics.create();
            g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(PANEL);
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

            g.setColor(new Color(119, 112, 174));
            g.setStroke(new BasicStroke(1.5f));
            g.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 30, 30);

            g.dispose();
            super.paintComponent(graphics);
        }
    }


    private static class SoftButton
        extends JButton
    {
        private static final long serialVersionUID = 1L;
        private final boolean pink;

        SoftButton(String text, boolean pink)
        {
            super(text);
            this.pink = pink;
            setForeground(WHITE);
            setFont(new Font("SansSerif", Font.BOLD, 15));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(12, 20, 12, 20));
            setMargin(new Insets(12, 20, 12, 20));
        }


        @Override
        protected void paintComponent(Graphics graphics)
        {
            Graphics2D g = (Graphics2D)graphics.create();
            g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

            Color top =
                pink ? new Color(185, 105, 165) : new Color(118, 111, 195);
            Color bottom =
                pink ? new Color(133, 72, 133) : new Color(75, 84, 145);

            if (!isEnabled())
            {
                top = new Color(73, 74, 95);
                bottom = new Color(57, 60, 78);
            }

            g.setPaint(new GradientPaint(0, 0, top, 0, getHeight(), bottom));
            g.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 24, 24);

            g.setColor(new Color(214, 201, 255));
            g.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 24, 24);

            g.dispose();
            super.paintComponent(graphics);
        }
    }


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

            // Glowing assembly platform
            g.setColor(new Color(100, 91, 180, 90));
            g.fillOval(45, 455, 360, 55);
            g.setColor(LAVENDER);
            g.setStroke(new BasicStroke(3));
            g.drawOval(65, 460, 320, 35);

            // Rounded torso
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

            // Glowing heart
            drawHeart(g, 225, 270);

            // Position 1: head
            drawSlot(g, 1, 225, 122);

            // Position 2: left arm
            drawSlot(g, 2, 115, 255);

            // Position 3: right arm
            drawSlot(g, 3, 335, 255);

            // Position 4: left leg
            drawSlot(g, 4, 185, 410);

            // Position 5: right leg
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

            // Ear pieces
            g.setColor(new Color(151, 131, 225));
            g.fillOval(x - 77, y - 23, 27, 49);
            g.fillOval(x + 50, y - 23, 27, 49);

            // Rounded helmet
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

            // Happy glowing eyes
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

            // Joint and hand
            g.setColor(LAVENDER);
            g.fillOval(x - 19, y - 54, 38, 38);

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

            // Knee joint
            g.setColor(LAVENDER);
            g.fillOval(x - 15, y - 2, 30, 30);

            // Rounded foot
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

    public static void main(String[] args)
    {
        SwingUtilities
            .invokeLater(() -> new RobotAssemblyGUI().setVisible(true));
    }
}
