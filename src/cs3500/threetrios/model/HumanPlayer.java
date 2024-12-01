package cs3500.threetrios.model;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a human player in a game of ThreeTrios.
 */
public class HumanPlayer implements AnyPlayer {
  private final TriosModel model;
  private final Player player;

  /**
   * Constructs this human player.
   *
   * @param model the model to initialize with
   * @param player the player that is playing
   */
  public HumanPlayer(TriosModel model, Player player) {
    this.model = model;
    this.player = player;
  }

  @Override
  public void makeMove(int row, int col, Card card) throws IOException {
    model.placeCard(row, col, card);
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
