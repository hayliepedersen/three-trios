package cs3500.threetrios.model.mocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CardModel;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.VariantOneModel;

/**
 * Represents a mock for the variant one model where the player's hands are pre-initialized.
 */
public class FallenAceMock extends VariantOneModel {
  /**
   * Constructs a variant model with a random seed for testing.
   *
   * @param grid the grid to initialize with
   * @param deck the deck to initialize with
   */
  public FallenAceMock(Cell[][] grid, List<Card> deck, boolean reverse, boolean fallenAce,
                       boolean fallenAceReverseCombo, boolean reverseFallenAceCombo,
                       Random random) {
    super(grid, deck, reverse, fallenAce, fallenAceReverseCombo, reverseFallenAceCombo);

    this.cellCount = this.countCardCells(grid);

    this.redHand = new ArrayList<>();
    this.blueHand = new ArrayList<>();
    this.modelObservers = new ArrayList<>();

    this.rows = grid.length;
    this.cols = grid[0].length;
    this.grid = grid;
    this.deck = new ArrayList<>(deck);

    if (deck.size() < this.cellCount + 1) {
      throw new IllegalArgumentException("Not enough cards to start the game.");
    }

    this.random = random;

    dealHands();

    ensureGridCells();
  }

  @Override
  public List<Card> getBlueHand() {
    return List.of(new CardModel("HeroKnight", "A", "2", "4", "4"));
  }

  @Override
  public List<Card> getRedHand() {
    return List.of(new CardModel("WorldDragon", "8", "3", "5", "7"),
            new CardModel("SkyWhale", "4", "1", "9", "9"));
  }
}
