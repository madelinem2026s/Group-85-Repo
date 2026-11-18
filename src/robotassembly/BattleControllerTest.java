
package robotassembly;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class BattleControllerTest
{

    @Test
    public void testCompleteTurnUpdatesModel()
    {
        BattleModel model = new BattleModel();
        BattleController controller = new BattleController(model);

        int damage = controller.completeTurn(1, 25, 4.0);

        assertEquals(100, damage);
        assertEquals(900, model.getHealth(2));
        assertEquals(2, model.getCurrentPlayer());
    }


    @Test
    public void testControllerUsesSameModel()
    {
        BattleModel model = new BattleModel();
        BattleController controller = new BattleController(model);

        assertSame(model, controller.getModel());
    }


    @Test
    public void testPlayerTwoTurn()
    {
        BattleModel model = new BattleModel();
        BattleController controller = new BattleController(model);

        controller.completeTurn(1, 20, 2.0);
        controller.completeTurn(2, 30, 2.0);

        assertEquals(960, model.getHealth(2));
        assertEquals(940, model.getHealth(1));
        assertEquals(2, model.getRound());
        assertEquals(1, model.getCurrentPlayer());
    }


    @Test
    public void testNullModelRejected()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> new BattleController(null));
    }


    @Test
    public void testInvalidTurnRejected()
    {
        BattleModel model = new BattleModel();
        BattleController controller = new BattleController(model);

        assertThrows(
            IllegalArgumentException.class,
            () -> controller.completeTurn(2, 25, 4.0));
    }


    @Test
    public void testInvalidMultiplierRejected()
    {
        BattleModel model = new BattleModel();
        BattleController controller = new BattleController(model);

        assertThrows(
            IllegalArgumentException.class,
            () -> controller.completeTurn(1, 25, -1.0));
    }


    @Test
    public void testControllerCannotContinueFinishedBattle()
    {
        BattleModel model = new BattleModel();
        BattleController controller = new BattleController(model);

        controller.completeTurn(1, 250, 4.0);
        controller.completeTurn(2, 250, 4.0);

        assertThrows(
            IllegalStateException.class,
            () -> controller.completeTurn(1, 10, 4.0));
    }
}
