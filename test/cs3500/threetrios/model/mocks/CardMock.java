package cs3500.threetrios.model.mocks;

import java.util.HashMap;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Player;

/**
 * Represents a mock of the Card class.
 */
public class CardMock implements Card {
  @Override
  public HashMap<String, HashMap<String, Integer>> setCardDirections() {
    HashMap<String, Integer> nsew = new HashMap<>();
    nsew.put("North", 2);
    nsew.put("South", 2);
    nsew.put("East", 2);
    nsew.put("West", 2);

    HashMap<String, HashMap<String, Integer>> names = new HashMap<>();
    names.put("OOD", nsew);

    return names;
  }

  @Override
  public Player getOwner() {
    return Player.RED;
  }

  @Override
  public void setCardOwner(Player player) {
    // Do nothing
  }

  @Override
  public String getName() {
    return "OOD";
  }
}
