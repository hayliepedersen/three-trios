package cs3500.threetrios.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * A model that allows for variant battle rules to take effect,
 * possibly at the same time. These rules change how the attack values result in capture.
 */
public class VariantTwoModel extends ThreeTriosModel implements TriosModel {
  private final boolean same;
  private final boolean plus;

  /**
   * Constructs a variant model.
   *
   * @param grid the grid to initialize with
   * @param deck the deck to initialize with
   */
  public VariantTwoModel(Cell[][] grid, List<Card> deck, boolean same, boolean plus) {
    super(grid, deck);

    if (same && plus) {
      throw new IllegalArgumentException("same and plus variants cannot both be true");
    }

    this.same = same;
    this.plus = plus;

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
  public VariantTwoModel(Cell[][] grid, List<Card> deck, boolean reverse, boolean fallenAce,
                         boolean same, boolean plus,
                         Random random) {
    super(grid, deck);

    if (same && plus) {
      throw new IllegalArgumentException("same and plus variants cannot both be true");
    }

    this.same = same;
    this.plus = plus;

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
    if (this.same && !this.plus) {
      // Play with same rule applied
      battlePhaseVarTwo(row, col, card);
    } else if (this.plus && !this.same) {
      // Play with reverse rule applied
      battlePhaseVarTwo(row, col, card);
    } else {

    }

  }

  /**
   * Handles the battle phase logic under the reverse rule.
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
  private void battlePhaseVarTwo(int row, int col, Card card) throws IOException {
    List<int[]> flippedCards = new ArrayList<>();
    // Starting with the initially placed card
    flippedCards.add(new int[]{row, col});

    while (!flippedCards.isEmpty()) {
      List<int[]> newlyFlippedCards = new ArrayList<>();

      for (int[] flippedCard : flippedCards) {
        int flippedRow = flippedCard[0];
        int flippedCol = flippedCard[1];

        for (int idx = 0; idx < 4; idx++) {
          if (canFlipCardVarTwo(flippedRow, flippedCol, idx, card)) {
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
  private boolean canFlipCardVarTwo(int row, int col, int directionIndex, Card placedCard) {
    int[] adjacentPos = getAdjacentPosition(row, col, directionIndex);
    int adjacentRow = adjacentPos[0];
    int adjacentCol = adjacentPos[1];

    if (!isValidPosition(adjacentRow, adjacentCol)) {
      return false;
    }

    // Check if adjacent card is flippable
    Card adjacentCard = getCardAt(adjacentRow, adjacentCol);
    if (adjacentCard == null || adjacentCard.getOwner() == currentPlayer) {
      return false;
    }

    // Retrieve the battle values for placed and adjacent cards in the given direction
    HashMap<String, Integer> placedCardDirections =
            placedCard.setCardDirections().get(placedCard.getName());
    HashMap<String, Integer> adjacentCardDirections =
            adjacentCard.setCardDirections().get(adjacentCard.getName());
    int[] battleValues = getBattleValues(directionIndex, placedCardDirections,
            adjacentCardDirections);

    // Determine if the placed card's value is less than the adjacent card's value
    // THE BIG CHANGE FOR REVERSE RULE:
    // In original model, this was '>' instead of '>='
    if (this.same) {
      return battleValues[0] >= battleValues[1];
    }
    else if (this.plus) {
      // TODO: write a method to calculate if plus is active
      return true;
    }
    else {
      return battleValues[0] > battleValues[1];
    }
  }

}