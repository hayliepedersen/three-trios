package cs3500.threetrios.model;

import java.util.List;

/**
 * A model that allows for variant battle rules to take effect,
 * possibly at the same time. These rules change how the attack values result in capture.
 */
public class VariantOneModel extends ThreeTriosModel implements TriosModel {

  /**
   * Constructs a variant model.
   *
   * @param grid the grid to initialize with
   * @param deck the deck to initialize with
   */
  public VariantOneModel(Cell[][] grid, List<Card> deck, boolean reverse, boolean fallenAce) {
    // TODO: The idea is to construct this with true or false from the command line arguments
    // in the main class, and then in the overrided method here, can do like:
    // if (this.reverse) -> reverse game play
    // if (this.fallenAce) -> fallenAce game play
    // if (reverse && fallenAce) -> combo rules
    super(grid, deck);
  }
}
