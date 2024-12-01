package cs3500.threetrios.model;

import java.io.IOException;
import java.util.Objects;

import cs3500.threetrios.strategy.BestMove;
import cs3500.threetrios.strategy.Strategy;

/**
 * Represents a machine player in the three trios game.
 */
public class MachinePlayer implements AnyPlayer {
  private final TriosModel model;
  private final Player player;
  private final Strategy strategy;

  /**
   * Constructs a machine player.
   *
   * @param model the model to initialize the strategy with
   * @param player the player to play for
   * @param strategy the strategy to execute
   */
  public MachinePlayer(TriosModel model, Player player, Strategy strategy) {
    this.model = model;
    this.player = player;
    this.strategy = strategy;
  }

  @Override
  public void makeMove(int row, int col, Card card) throws IOException {
    BestMove move = strategy.chooseMove(model, player);

    Card chosenCard = move.getCard();
    int[] chosenCell =  new int[] { move.getRow(), move.getCol() };

    model.placeCard(chosenCell[0], chosenCell[1], chosenCard);
  }

  @Override
  public Player getPlayer() {
    return player;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof AnyPlayer)) {
      return false;
    }

    AnyPlayer that = (AnyPlayer) obj;

    return this.getPlayer().equals(that.getPlayer());
  }

  @Override
  public int hashCode() {
    return Objects.hash(model, player);
  }
}
