package cs3500.threetrios.controller;

import java.io.IOException;

/**
 * Handles updates about the model game state.
 */
public interface ModelObservers {
  /**
   * Called when the game ends.
   *
   * @param winner       the name of the winning player
   * @param winningScore the score of the winning player
   */
  void onGameOver(String winner, int winningScore) throws IOException;

  /**
   * Called when there is a tie in the game.
   */
  void onGameTie() throws IOException;

  /**
   * Called when a card is placed in the model.
   */
  void modelCardPlaced() throws IOException;

  /**
   * Called when the turn is changed.
   */
  void onTurnChanged() throws IOException;
}

