package cs3500.threetrios.controller;


import java.io.IOException;

import cs3500.threetrios.model.Card;

/**
 * Defines the actions the controller listens for from the view,
 * any interaction initiated by the player.
 */
public interface ViewFeatures {
  /**
   * Called when a player selects a card from their hand.
   */
  void cardSelected(Card card);

  /**
   * Retrieves the selected card from player.
   *
   * @return card the card selected by the player
   */
  Card selectedCard();

  /**
   * Called when a player attempts to place a card on the grid.
   *
   * @param row the row index where the card is to be placed
   * @param col the column index where the card is to be placed
   */
  void cardPlaced(int row, int col, Card card) throws IOException;

  /**
   * Updates the view at the end of a turn.
   */
  void endTurn() throws IOException;

  /**
   * Prints to the console with a message depending on what was passed in from the view.
   *
   * @param rows the row clicked on
   * @param cols the col clicked on
   * @param index the index clicked on
   * @param player which players hand was clicked on
   */
  void panelPrint(int rows, int cols, int index, String player);

  /**
   * If either or both players are machines, delegates to makeMove() in the AnyPlayer interface.
   * Allows for the machine player to make their turn.
   */
  void handleMachineTurnPlay() throws IOException;
}

