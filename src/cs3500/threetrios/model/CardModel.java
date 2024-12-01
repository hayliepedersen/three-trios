package cs3500.threetrios.model;

import java.util.HashMap;

/**
 * CardModel that takes in integers and sets them to corresponding directions.
 */
public class CardModel implements Card {
  private final String name;
  private final int north;
  private final int south;
  private final int east;
  private final int west;
  private Player cardOwner;

  /**
   * The constructor for CardModel.
   *
   * @param north the north attack value
   * @param south the south attack value
   * @param east  the east attack value
   * @param west  the west attack value
   */
  public CardModel(String name, String north, String south, String east, String west) {
    this.name = name;
    this.north = parseHexOrInt(north);
    this.south = parseHexOrInt(south);
    this.east = parseHexOrInt(east);
    this.west = parseHexOrInt(west);
  }

  private int parseHexOrInt(String value) {
    if ("A".equalsIgnoreCase(value)) {
      return 10;
    }
    try {
      int attackValue = Integer.parseInt(value);

      if (attackValue > 9) {
        throw new IllegalArgumentException("Invalid attack value: " + attackValue);
      } else {
        return attackValue;
      }
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid value for direction: " + value);
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof CardModel)) {
      return false;
    }

    CardModel that = (CardModel) obj;

    return this.name.equals(that.name)
            && this.north == that.north
            && this.south == that.south
            && this.east == that.east
            && this.west == that.west;
  }

  @Override
  public String toString() {
    return this.name;
  }

  @Override
  public int hashCode() {
    return north + east + west + south;
  }

  @Override
  public HashMap<String, HashMap<String, Integer>> setCardDirections() {
    HashMap<String, Integer> directions = new HashMap<>();
    HashMap<String, HashMap<String, Integer>> cardConfig = new HashMap<>();

    directions.put("North", north);
    directions.put("South", south);
    directions.put("East", east);
    directions.put("West", west);

    cardConfig.put(name, directions);

    return cardConfig;
  }

  @Override
  public Player getOwner() {
    return cardOwner;
  }

  @Override
  public void setCardOwner(Player player) {
    this.cardOwner = player;
  }

  @Override
  public String getName() {
    return this.name;
  }
}