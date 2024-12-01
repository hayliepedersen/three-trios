package cs3500.threetrios.model;

import java.io.IOException;

/**
 * Representation of the possible colors for a player in Three Trios.
 *
 * <p>Implements an asString method to be used for textual view of gameplay</p>
 */
public enum Player implements AnyPlayer {
  RED("RED"),
  BLUE("BLUE");

  public final String asString;

  Player(String player) {
    this.asString = player;
  }

  /**
   * Returns the player as a string.
   *
   * @return the player as a string
   */
  public String getAsString() {
    return asString;
  }

  @Override
  public void makeMove(int row, int col, Card card) throws IOException {
    // does not do anyhting in this class
  }

  @Override
  public Player getPlayer() {
    return this;
  }
}
