package cs3500.threetrios.model;

import java.io.IOException;
import java.util.List;

/**
 * Represents a read only trios model, can only be read from, not modified.
 */
public interface ReadOnlyTriosModel {

  /**
   * Checks if the game is over.
   *
   * <p>The game is over once all card cells are filled.</p>
   *
   * @return true if the game is over, false otherwise
   */
  boolean isGameOver();

  /**
   * Determines the winner of the game.
   *
   * <p>The winner is determined by counting the number of cards each player owns both
   * on the grid and in their hands. The player with the most owned cards wins.
   * If no such player exists, the game is a tie.</p>
   *
   * @return the winning player
   * @throws IllegalStateException if game is not over
   */
  String determineWinner() throws IOException;

  /**
   * Gets a copy of the grid from the model.
   *
   * @return the model's grid
   */
  Cell[][] getGridCopy();

  /**
   * Gets the hand for the specified player.
   *
   * @param player the player's hand to retrieve
   * @return a list of cards
   */
  public List<Card> getPlayerHand(AnyPlayer player);

  /**
   * Gets the blue player's hand from the model.
   *
   * @return the blue player's hand
   */
  List<Card> getBlueHand();

  /**
   * Gets the red player's hand from the model.
   *
   * @return the red player's hand
   */
  List<Card> getRedHand();

  /**
   * Gets the current player from the model.
   *
   * @return the model's current player
   */
  Player getCurrentPlayer();

  /**
   * Gets the number of rows from the grid.
   *
   * @return the number of rows
   */
  int getRows();

  /**
   * Gets the number of columns from the grid.
   *
   * @return the number of columns
   */
  int getCols();

  /**
   * Gets the cell at the specified position.
   *
   * @param row the row position
   * @param col the column position
   * @return the cell
   */
  Cell getCellAt(int row, int col);

  /**
   * Determines which player owns the card in a cell at a given coordinate.
   *
   * @param row the row position
   * @param col the column position
   * @return the player who owns the cell
   */
  Player getCellOwner(int row, int col);

  /**
   * Determines if it's legal for the current player to play at a given coordinate.
   *
   * @param row the row position
   * @param col the column position
   * @return if it is legal
   */
  boolean isLegalPlay(int row, int col);

  /**
   * Determines how many cards a player can flip by playing at a given coordinate.
   *
   * @param row the row position
   * @param col the column position
   * @param card the card to be placed
   * @return the number of cards that can be flipped
   */
  int numOfFlippableCards(int row, int col, Card card);

  /**
   * Determines a player’s score in the game.
   *
   * <p>A player’s score is simply the number of cards that player owns in their hand plus the
   * number of cards that player owns on the grid</p>
   *
   * @param player the player's score to calculate
   * @return the given player's score
   */
  int getScore(Player player);

  /**
   * Checks if the game has begun.
   *
   * @return whether the game started
   */
  boolean isStarted();

  /**
   * Returns whether the given player is the current player.
   *
   * @param player the given player
   * @return whether the given player is the current player
   */
  boolean isPlayerTurn(AnyPlayer player);

}
