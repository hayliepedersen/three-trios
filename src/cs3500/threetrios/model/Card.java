package cs3500.threetrios.model;

import java.util.HashMap;

/**
 * Represents a card in the game ThreeTrios.
 */
public interface Card {

  /**
   * Sets this card's directions by making the values a hashmap to be used by the model.
   *
   * @return a HashMap of String to Integer that sets the directional values.
   */
  HashMap<String, HashMap<String, Integer>> setCardDirections();

  /**
   * Gets the owner of this card.
   *
   * @return the player who owns the card
   */
  Player getOwner();

  /**
   * Sets the card owner for the given player.
   *
   * @param player the player to own the card
   */
  void setCardOwner(Player player);

  /**
   * Gets the name of this card.
   *
   * @return the name of this card
   */
  String getName();
}