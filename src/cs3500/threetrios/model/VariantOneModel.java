package cs3500.threetrios.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiPredicate;

/**
 * A model that allows for variant battle rules to take effect,
 * possibly at the same time. These rules change how the attack values result in capture.
 */
public class VariantOneModel extends ThreeTriosModel implements TriosModel {
  private final boolean fallenAce;
  private final boolean reverse;

  /**
   * Constructs a variant model.
   *
   * @param grid the grid to initialize with
   * @param deck the deck to initialize with
   */
  public VariantOneModel(Cell[][] grid, List<Card> deck, boolean reverse, boolean fallenAce) {
    super(grid, deck);

    this.reverse = reverse;
    this.fallenAce = fallenAce;

    this.cellCount = this.countCardCells(grid);

    this.redHand = new ArrayList<>();
    this.blueHand = new ArrayList<>();
    this.modelObservers = new ArrayList<>();

    this.rows = grid.length;
    this.cols = grid[0].length;
    this.grid = grid;
    this.deck = new ArrayList<>(deck);
    this.isStarted = false;

    if (deck.size() < this.cellCount + 1) {
      throw new IllegalArgumentException("Not enough cards to start the game.");
    }

    this.random = new Random();

    dealHands();

    ensureGridCells();
  }

  /**
   * Constructs a variant model with a random seed for testing.
   *
   * @param grid the grid to initialize with
   * @param deck the deck to initialize with
   */
  public VariantOneModel(Cell[][] grid, List<Card> deck, boolean reverse, boolean fallenAce,
                         Random random) {
    super(grid, deck);

    this.reverse = reverse;
    this.fallenAce = fallenAce;

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

  /**
   * Handles the battle phase logic for a player's turn.
   *
   * <p>In the battle phase, player A's newly placed card battles against the opposing player B's
   * adjacent cards. Any of player B’s cards that lose in the battle phase are flipped.
   * This means they become player A’s cards but remain on the grid.
   * After the battle phase, the turn changes over to the other player.</p>
   *
   * @param row  the row of the placed card, 0-based index
   * @param col  the column of the placed card, 0-based index
   * @param card the placed card
   */
  @Override
  protected void battlePhase(int row, int col, Card card) throws IOException {
    if (this.reverse && !this.fallenAce) {
      // Play with only reverse rule applied
      battlePhaseVariant(row, col, card, 1);
    } else if (this.fallenAce && !this.reverse) {
      // Play with only fallen ace rule applied
      battlePhaseVariant(row, col, card, 1);
    } else if (this.reverse) {
      // Play with both rules applied
      battlePhaseVariant(row, col, card, 0);
    }
    else {
      battlePhaseVariant(row, col, card, -1);
    }
  }

  /**
   * Handles the battle phase logic based on the specified rule.
   *
   * @param row  the row of the placed card, 0-based index
   * @param col  the column of the placed card, 0-based index
   * @param card the placed card
   */
  private void battlePhaseVariant(int row, int col, Card card, int isReverseRule)
          throws IOException {
    List<int[]> flippedCards = new ArrayList<>();
    // Starting with the initially placed card
    flippedCards.add(new int[]{row, col});

    while (!flippedCards.isEmpty()) {
      List<int[]> newlyFlippedCards = new ArrayList<>();

      for (int[] flippedCard : flippedCards) {
        int flippedRow = flippedCard[0];
        int flippedCol = flippedCard[1];

        for (int idx = 0; idx < 4; idx++) {
          if (canFlipCard(flippedRow, flippedCol, idx, card, isReverseRule)) {
            int[] adjacentPos = getAdjacentPosition(flippedRow, flippedCol, idx);
            int adjacentRow = adjacentPos[0];
            int adjacentCol = adjacentPos[1];

            flipCard(adjacentRow, adjacentCol);
            newlyFlippedCards.add(new int[]{adjacentRow, adjacentCol});
          }
        }
      }

      flippedCards = newlyFlippedCards;
    }
  }

  /**
   * Checks if the placed card can flip the adjacent card in the given direction.
   *
   * @param row            the current row
   * @param col            the current column
   * @param directionIndex the direction to check
   * @param placedCard     the placed card
   * @param isReverseRule  the flip condition to base the flip off of
   * @return true if the card can be flipped in this direction, false otherwise
   */
  private boolean canFlipCard(int row, int col, int directionIndex, Card placedCard,
                              int isReverseRule) {
    int[] battleValues = battleValues(row, col, directionIndex, placedCard);
    if (battleValues == null) {
      return false;
    }

    if (isReverseRule != -1) {
      BiPredicate<int[], int[]> flipCondition = isReverseRule == 1
              ? this::flipConditionReverse
              : this::flipConditionCombo;
      return flipCondition.test(battleValues, new int[]{row, col});
    }

    return battleValues[0] > battleValues[1];
  }

  // Flip condition for the reverse and fallen ace rules
  private boolean flipConditionReverse(int[] battleValues, int[] position) {
    return battleValues[0] < battleValues[1];
  }

  // Flip condition for the combination of reverse and fallen ace
  private boolean flipConditionCombo(int[] battleValues, int[] position) {
    return (battleValues[0] == 10 && battleValues[1] == 1) || battleValues[0] < battleValues[1];
  }
}
