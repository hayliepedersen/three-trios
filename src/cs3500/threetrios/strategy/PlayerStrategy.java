package cs3500.threetrios.strategy;

import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.TriosModel;

/**
 * A Strategy interface for choosing where to play next for the given player.
 */
public interface PlayerStrategy {

  /**
   * Chooses the best move.
   *
   * @param model the model
   * @param player the player that is playing
   * @return the coordinates to play to
   */
  BestMove chooseMove(TriosModel model, Player player);

  /**
   * Default case, get the first card in hand at uppermost left corner.
   *
   * @param model the model to play with
   * @param player the player that is playing
   * @return the best move
   */
  BestMove defaultMove(TriosModel model, Player player);
}
