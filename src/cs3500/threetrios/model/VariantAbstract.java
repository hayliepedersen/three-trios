package cs3500.threetrios.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiPredicate;

/**
 * Represents a variant abstract class that allows for the combination of level 1 and level 2
 * features for level 3.
 */
public class VariantAbstract extends ThreeTriosModel implements TriosModel {
  protected boolean reverse;
  protected boolean fallenAce;
  protected boolean same;
  protected boolean plus;

  /**
   * Constructs the variant abstract class.
   */
  public VariantAbstract(Cell[][] grid, List<Card> deck, boolean reverse, boolean fallenAce,
                         boolean same, boolean plus) {
    super(grid, deck);

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

    this.reverse = reverse;
    this.fallenAce = fallenAce;

    if (same && plus) {
      throw new IllegalArgumentException("same and plus variants cannot both be true");
    }

    this.same = same;
    this.plus = plus;

    dealHands();

    ensureGridCells();
  }

  /**
   * Constructs the variant abstract class.
   */
  public VariantAbstract(Cell[][] grid, List<Card> deck, boolean reverse, boolean fallenAce,
                         boolean same, boolean plus, Random random) {
    super(grid, deck);

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

    this.random = random;
    this.reverse = reverse;
    this.fallenAce = fallenAce;

    if (same && plus) {
      throw new IllegalArgumentException("same and plus variants cannot both be true");
    }

    this.same = same;
    this.plus = plus;

    dealHands();

    ensureGridCells();
  }

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
  protected void battlePhaseVariant(int row, int col, Card card, int isReverseRule)
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
          if (canFlipCardVar(flippedRow, flippedCol, idx, card, isReverseRule)) {
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
   * @return true if the card can be flipped in this direction, false otherwise
   */
  protected boolean canFlipCardVar(int row, int col, int directionIndex, Card placedCard,
                                 int isReverseRule) {

    int[] battleValues = battleValues(row, col, directionIndex, placedCard);
    if (battleValues == null) {
      return false;
    }

    if (isReverseRule != -1) {
      BiPredicate<int[], int[]> flipCondition = isReverseRule == 1
              ? this::flipConditionReverse
              : this::flipConditionCombo;
      boolean firstCheck = flipCondition.test(battleValues, new int[]{row, col});
    }
    boolean firstCheck = false;


    if (this.same) {
      return battleValues[0] >= battleValues[1] || firstCheck;
    } else if (this.plus) {
      return this.canFlipCardPlus(row, col, directionIndex, placedCard)
              || battleValues[0] > battleValues[1] || firstCheck;
    }
    else {
      return battleValues[0] > battleValues[1] || firstCheck;
    }
  }

  private boolean canFlipCardPlus(int row, int col, int directionIndex, Card placedCard) {
    // Retrieve the battle values for placed and adjacent cards in the given direction
    int[] battleValues = battleValues(row, col, directionIndex, placedCard);

    if (battleValues == null) {
      return false;
    }

    int sumToCheck = battleValues[0] + battleValues[1];

    int nextDirection = nextDir(directionIndex);
    int numSums = 0;

    while (nextDirection != directionIndex) {
      battleValues = battleValues(row, col, directionIndex, placedCard);
      if (battleValues[0] + battleValues[1] == sumToCheck) {
        numSums += 1;
      }
      nextDirection = nextDir(nextDirection);
    }

    return numSums >= 2;
  }

  /**
   * Returns the next direction.
   *
   * @param dir the current direction
   * @return the next direction
   */
  private int nextDir(int dir) {
    switch (dir) {
      case 0:
        return 1;
      case 1:
        return 2;
      case 2:
        return 3;
      case 3:
        return 0;
      default:
        throw new IllegalArgumentException("direction is invalid");
    }
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
