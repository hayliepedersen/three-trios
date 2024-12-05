package cs3500.threetrios.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class VariantTwoModel extends ThreeTriosModel implements TriosModel {
  private boolean same;
  private boolean plus;

  public VariantTwoModel(Cell[][] grid, List<Card> deck, boolean same, boolean plus) {
    super(grid, deck);

    this.same = same;
    this.plus = plus;

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

  public VariantTwoModel(Cell[][] grid, List<Card> deck, Random rand) {
    super(grid, deck);
  }



  // TODO: very very similar to the one in the originial model
  /**
   * Checks if the placed card can flip the adjacent card in the given direction.
   *
   * @param row            the current row
   * @param col            the current column
   * @param directionIndex the direction to check
   * @param placedCard     the placed card
   * @return true if the card can be flipped in this direction, false otherwise
   */
  protected boolean canFlipCard(int row, int col, int directionIndex, Card placedCard) {
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

    // TODO: i just wanted to highlight this this is how i do the logic
    // Determine if the placed card's value is greater than the adjacent card's value

    // if same is called
    if (!this.plus && this.same) {
      return battleValues[0] >= battleValues[1];
    }
    // if plus is called
    else if (this.plus && !this.same) {
      return battleValues[0] > battleValues[1];
    }
    // if neither is called
    else {
      return battleValues[0] > battleValues[1];
    }
  }
}
