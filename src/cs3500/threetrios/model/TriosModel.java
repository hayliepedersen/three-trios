package cs3500.threetrios.model;

import java.io.IOException;

import cs3500.threetrios.controller.ModelObservers;

/**
 * Behaviors for a game of Three Trios.
 */
public interface TriosModel extends ReadOnlyTriosModel {

  /**
   * Handles the logic for placing a card on the grid.
   *
   * <p>A play is legal for player A if the cell the card is being played to is not a hole
   * and not occupied by another card. The result of a legal move is proceeding into
   * the battle phase. Naturally, the card is also removed from the player A’s hand.</p>
   *
   * @param row the row coordinate for the card to be placed to
   * @param col the column coordinate for the card to be placed to
   * @param card the card to be placed on the grid
   * @throws IllegalArgumentException if trying to place card to a hole, an occupied cell,
   *     or if player does not have this card
   */
  void placeCard(int row, int col, Card card) throws IOException;

  /**
   * Designates the ThreeTriosModel as started.
   */
  void startGame();

  /**
   * Registers a listener for model status changes.
   *
   * @param modelObservers the listener to be notified of model events
   */
  void addObservers(ModelObservers modelObservers);

  /**
   * Ends the turn of the current player.
   */
  void endTurn();
}