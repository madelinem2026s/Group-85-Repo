
package robotassembly;

/**
 * Controller for the two-player battle.
 */
public class BattleController
{

    private final BattleModel model;

    public BattleController(BattleModel model)
    {

        if (model == null)
        {
            throw new IllegalArgumentException("Battle model cannot be null.");
        }

        this.model = model;
    }


    public int completeTurn(int player, int score, double multiplier)
    {

        return model.completeTurn(player, score, multiplier);
    }


    public BattleModel getModel()
    {
        return model;
    }
}
