package cs3500.threetrios.model;

import java.io.IOException;

/**
 * Handles actions triggered by a player.
 */
public interface AnyPlayer {
  /**
   * Allows the player to make a move.
   *
   * @param row the row position to make a move to
   * @param col the column position to make a move to
   * @param card the card to place at the specified position
   */
  void makeMove(int row, int col, Card card) throws IOException;

  /**
   * Returns this player.
   *
   * @return the player
   */
  Player getPlayer();
}

