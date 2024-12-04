package cs3500.threetrios.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cs3500.threetrios.controller.ModelObservers;

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
    // TODO: Construct this with true or false from the command line arguments
    super(grid, deck);

    this.reverse = reverse;
    this.fallenAce = fallenAce;

    this.cellCount = this.countCardCells(grid);

    if (this.cellCount % 2 == 0) {
      throw new IllegalArgumentException("Grid must have an odd number of card cells.");
    }

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

  // TODO: Once functionality is work, do abstraction between this model and original model,
  // including constructor, move stuff to startGame()

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
      battlePhaseReverse(row, col, card);
    } else if (this.fallenAce && !this.reverse) {
      // Play with only fallen ace rule applied
      battlePhaseReverse(row, col, card);
    } else if (this.reverse) {
      // Play with both rules applied
      battlePhaseReverse(row, col, card);
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
  private void battlePhaseReverse(int row, int col, Card card) throws IOException {
    List<int[]> flippedCards = new ArrayList<>();
    // Starting with the initially placed card
    flippedCards.add(new int[]{row, col});

    while (!flippedCards.isEmpty()) {
      List<int[]> newlyFlippedCards = new ArrayList<>();

      for (int[] flippedCard : flippedCards) {
        int flippedRow = flippedCard[0];
        int flippedCol = flippedCard[1];

        for (int idx = 0; idx < 4; idx++) {
          if (canFlipCardReverse(flippedRow, flippedCol, idx, card)) {
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
  private boolean canFlipCardReverse(int row, int col, int directionIndex, Card placedCard) {
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
    // In original model, this was '>' instead of '<'
    return battleValues[0] < battleValues[1];
  }
}
