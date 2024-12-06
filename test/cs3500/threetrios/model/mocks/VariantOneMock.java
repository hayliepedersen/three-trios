package cs3500.threetrios.model.mocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CardModel;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.VariantOneModel;

/**
 * Represents a mock for the variant one model where the player's hands are pre-initialized.
 */
public class VariantOneMock extends VariantOneModel {
  /**
   * Constructs a variant model with a random seed for testing.
   *
   * @param grid the grid to initialize with
   * @param deck the deck to initialize with
   */
  public VariantOneMock(Cell[][] grid, List<Card> deck, boolean reverse, boolean fallenAce,
                        Random random) {
    super(grid, deck, reverse, fallenAce);

    this.cellCount = this.countCardCells(grid);

    this.redHand = new ArrayList<>(Arrays.asList(new CardModel("WorldDragon", "8", "3", "5", "7"),
            new CardModel("SkyWhale", "4", "1", "9", "9")));

    this.blueHand =new ArrayList<>(Arrays.asList(new CardModel("HeroKnight", "A", "2", "4", "4"),
            new CardModel("WindBird", "7", "2", "5", "3")));
    this.modelObservers = new ArrayList<>();

    this.rows = grid.length;
    this.cols = grid[0].length;
    this.grid = grid;
    this.deck = new ArrayList<>(deck);

    if (deck.size() < this.cellCount + 1) {
      throw new IllegalArgumentException("Not enough cards to start the game.");
    }

    this.random = random;

    ensureGridCells();
  }

  @Override
  public List<Card> getBlueHand() {
    return List.of(new CardModel("HeroKnight", "A", "2", "4", "4"),
            new CardModel("WindBird", "7", "2", "5", "3"));
  }

  @Override
  public List<Card> getRedHand() {
    return List.of(new CardModel("WorldDragon", "8", "3", "5", "7"),
            new CardModel("SkyWhale", "4", "1", "9", "9"));
  }
}
