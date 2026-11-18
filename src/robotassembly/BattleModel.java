
package robotassembly;

/**
 * Stores the state and rules for a two-player robot battle. Both players
 * complete their turn before knockout is checked.
 */
public class BattleModel
{

    public static final int MAX_HEALTH = 1000;

    private static final String[] DIFFICULTIES = { "Easy", "Medium", "Hard" };

    private final int[] health = { MAX_HEALTH, MAX_HEALTH };

    private int roundIndex;
    private int currentPlayer;
    private int lastDamage;
    private boolean finished;
    private String winner;

    public BattleModel()
    {
        roundIndex = 0;
        currentPlayer = 1;
        lastDamage = 0;
        finished = false;
        winner = "";
    }


    public int getHealth(int player)
    {
        validatePlayer(player);
        return health[player - 1];
    }


    public int getCurrentPlayer()
    {
        return currentPlayer;
    }


    public int getRound()
    {
        return roundIndex + 1;
    }


    public String getDifficulty()
    {
        return DIFFICULTIES[roundIndex];
    }


    public int getLastDamage()
    {
        return lastDamage;
    }


    public boolean isFinished()
    {
        return finished;
    }


    public String getWinner()
    {
        return winner;
    }


    /**
     * Completes the current player's turn.
     *
     * @param player
     *            player 1 or 2
     * @param score
     *            assembly score
     * @param multiplier
     *            final time multiplier
     * @return damage dealt
     */
    public int completeTurn(int player, int score, double multiplier)
    {

        if (finished)
        {
            throw new IllegalStateException("The battle is already finished.");
        }

        validatePlayer(player);

        if (player != currentPlayer)
        {
            throw new IllegalArgumentException("It is not this player's turn.");
        }

        if (!Double.isFinite(multiplier) || multiplier < 0)
        {
            throw new IllegalArgumentException(
                "Multiplier must be nonnegative and finite.");
        }

        double rawDamage = Math.max(0.0, score * multiplier);

        lastDamage = rawDamage >= Integer.MAX_VALUE
            ? Integer.MAX_VALUE
            : (int)Math.round(rawDamage);

        int opponent = player == 1 ? 2 : 1;

        health[opponent - 1] =
            (int)Math.max(0L, (long)health[opponent - 1] - lastDamage);

        if (currentPlayer == 1)
        {
            // Player 2 always gets their turn.
            currentPlayer = 2;
        }
        else
        {
            // Both players have completed this round.
            evaluateRound();
        }

        return lastDamage;
    }


    private void evaluateRound()
    {

        if (health[0] == 0 && health[1] == 0)
        {
            endBattle("It's a tie! Both players were knocked out!");
        }
        else if (health[0] == 0)
        {
            endBattle("Player 2 wins by knockout!");
        }
        else if (health[1] == 0)
        {
            endBattle("Player 1 wins by knockout!");
        }
        else if (roundIndex == 2)
        {

            if (health[0] == health[1])
            {
                endBattle("It's a tie!");
            }
            else if (health[0] > health[1])
            {
                endBattle("Player 1 wins!");
            }
            else
            {
                endBattle("Player 2 wins!");
            }
        }
        else
        {
            roundIndex++;
            currentPlayer = 1;
        }
    }


    private void endBattle(String result)
    {
        finished = true;
        winner = result;
    }


    private void validatePlayer(int player)
    {
        if (player != 1 && player != 2)
        {
            throw new IllegalArgumentException("Player must be 1 or 2.");
        }
    }
}
