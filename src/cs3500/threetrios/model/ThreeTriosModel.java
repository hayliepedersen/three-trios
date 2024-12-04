package cs3500.threetrios.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import cs3500.threetrios.controller.ModelObservers;

/**
 * An implementation of {@link TriosModel} that handles the logic for
 * a game of Three Trios.
 */
public class ThreeTriosModel implements TriosModel {
  protected int rows;
  protected int cols;
  protected Cell[][] grid;
  protected int cellCount;
  protected List<Card> deck;
  protected List<Card> redHand;
  protected List<Card> blueHand;
  protected Player currentPlayer;
  protected Random random;
  protected boolean isStarted;
  protected List<ModelObservers> modelObservers;

  /**
   * Initializes the game by setting up the start of game functionality.
   *
   * @param grid the grid to initialize the game with
   * @param deck the deck to initialize the game with
   */
  public ThreeTriosModel(Cell[][] grid, List<Card> deck) {
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
    this.currentPlayer = Player.RED; // RED goes first
    this.isStarted = false;

    if (deck.size() < this.cellCount + 1) {
      throw new IllegalArgumentException("Not enough cards to start the game.");
    }

    this.random = new Random();

    dealHands();

    ensureGridCells();
  }

  /**
   * Initializes the game with a random seed for testing.
   *
   * @param grid   the grid to initialize the game with
   * @param deck   the deck to initialize the game with
   * @param random the random seed for testing
   */
  public ThreeTriosModel(Cell[][] grid, List<Card> deck, Random random) {
    this.cellCount = this.countCardCells(grid);

    this.redHand = new ArrayList<>();
    this.blueHand = new ArrayList<>();
    this.modelObservers = new ArrayList<>();

    this.rows = grid.length;
    this.cols = grid[0].length;
    this.grid = grid;
    this.deck = new ArrayList<>(deck);
    this.currentPlayer = Player.RED; // RED goes first

    if (deck.size() < this.cellCount + 1) {
      throw new IllegalArgumentException("Not enough cards to start the game.");
    }

    this.random = random;

    dealHands();

    ensureGridCells();
  }

  @Override
  public void startGame() {
    this.isStarted = true;
  }

  /**
   * counts the number of card cells in a given Cell[][].
   *
   * @param grid the Cell[][] that represents a grid
   * @return the number of card cells present
   */
  protected int countCardCells(Cell[][] grid) {
    int cellCount = 0;
    for (int rowCount = 0; rowCount < grid.length; rowCount++) {
      for (int colCount = 0; colCount < grid[0].length; colCount++) {
        Cell cell = grid[rowCount][colCount];

        if (cell instanceof CardCell) {
          cellCount += 1;
        }

      }
    }
    return cellCount;
  }

  /**
   * Deals to each player's hand.
   *
   * <p>Each player’s hand is filled with exactly (N+1)/2 cards where
   * N is the number of card cells on the grid.</p>
   */
  protected void dealHands() {
    Collections.shuffle(this.deck, this.random);

    for (int deckIdx = 0; deckIdx < (cellCount + 1) / 2; deckIdx++) {
      redHand.add(deck.remove(0));
      blueHand.add(deck.remove(0));
    }
  }

  /**
   * Ensures invariants by checking that all cells are valid.
   *
   * @throws IllegalStateException if there is an invalid card owner or all grid cells are not
   *                               either card cells or holes
   */
  protected void ensureGridCells() {
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        Cell cell = grid[row][col];

        if (cell instanceof CardCell) { // Check if it's a card cell
          Card card = cell.getCellCard();

          // If card cell contains a card, validate ownership
          if (card != null && card.getOwner() != Player.RED && card.getOwner() != Player.BLUE) {
            throw new IllegalStateException("Invalid card owner");
          }
        } else if (!(cell instanceof Hole)) {
          throw new IllegalStateException("Invalid cell type");
        }
      }
    }
  }

  @Override
  public void placeCard(int row, int col, Card card) throws IOException {
    if (!this.isLegalPlay(row, col)) {
      throw new IllegalArgumentException("Cell is not valid.");
    }

    if ((currentPlayer == Player.RED && !redHand.contains(card))
            || (currentPlayer == Player.BLUE && !blueHand.contains(card))) {
      throw new IllegalArgumentException("Player does not have this card.");
    }

    Cell cell = grid[row][col];

    cell.addCard(card);
    card.setCardOwner(currentPlayer);

    if (currentPlayer == Player.RED) {
      redHand.remove(card);
    } else {
      blueHand.remove(card);
    }

    try {
      for (ModelObservers observers : this.modelObservers) {
        observers.modelCardPlaced();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    // Perform battle phase after placing the card
    battlePhase(row, col, card);

    currentPlayer = (currentPlayer == Player.RED) ? Player.BLUE : Player.RED;

    endTurn();
    if (isGameOver()) {
      determineWinner();
    }

    try {
      for (ModelObservers observers : this.modelObservers) {
        observers.onTurnChanged();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
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
  protected void battlePhase(int row, int col, Card card) throws IOException {
    List<int[]> flippedCards = new ArrayList<>();
    // Starting with the initially placed card
    flippedCards.add(new int[]{row, col});

    while (!flippedCards.isEmpty()) {
      List<int[]> newlyFlippedCards = new ArrayList<>();

      for (int[] flippedCard : flippedCards) {
        int flippedRow = flippedCard[0];
        int flippedCol = flippedCard[1];

        for (int idx = 0; idx < 4; idx++) {
          if (canFlipCard(flippedRow, flippedCol, idx, card)) {
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
   * Gets the battle values for a given direction.
   *
   * @param directionIndex         the index of the direction
   * @param placedCardDirections   the direction values for the placed card
   * @param adjacentCardDirections the direction values for the adjacent card
   * @return an array where index 0 is the placed card's value and index 1 is the adjacent card's
   *     value
   */
  protected int[] getBattleValues(int directionIndex, HashMap<String, Integer> placedCardDirections,
                                HashMap<String, Integer> adjacentCardDirections) {
    switch (directionIndex) {
      case 0:  // North
        return new int[]{placedCardDirections.get("North"), adjacentCardDirections.get("South")};
      case 1:  // South
        return new int[]{placedCardDirections.get("South"), adjacentCardDirections.get("North")};
      case 2:  // East
        return new int[]{placedCardDirections.get("East"), adjacentCardDirections.get("West")};
      case 3:  // West
        return new int[]{placedCardDirections.get("West"), adjacentCardDirections.get("East")};
      default:
        throw new IllegalArgumentException("Invalid direction index");
    }
  }

  @Override
  public int numOfFlippableCards(int row, int col, Card card) {
    Set<String> visited = new HashSet<>();
    return countFlippableCards(row, col, card, visited);
  }

  private int countFlippableCards(int row, int col, Card card, Set<String> visited) {
    int flippableCount = 0;

    // To keep track of cells
    String cellId = row + "," + col;
    if (visited.contains(cellId)) {
      return 0; // Skip if already visited
    }

    visited.add(cellId);

    for (int idx = 0; idx < 4; idx++) {
      int newRow = row;
      int newCol = col;

      // Update row/col based on direction
      switch (idx) {
        case 0:
          newRow = row - 1;
          break; // North
        case 1:
          newRow = row + 1;
          break; // South
        case 2:
          newCol = col + 1;
          break; // East
        case 3:
          newCol = col - 1;
          break; // West

        // there is no default case in this switch statement
        default:
          break;
      }

      if (isValidPosition(newRow, newCol) && canFlipCard(row, col, idx, card)) {
        flippableCount++;
        // Count all flippable cards from this new cell
        flippableCount += countFlippableCards(newRow, newCol, card, visited);
      }
    }

    return flippableCount;
  }

  /**
   * Retrieves the adjacent row and column based on a direction index.
   *
   * @param row            the current row position
   * @param col            the current column position
   * @param directionIndex the direction of the position
   * @return the adjacent row and column
   */
  protected int[] getAdjacentPosition(int row, int col, int directionIndex) {
    int[][] directions = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};  // North, South, East, West
    return new int[]{row + directions[directionIndex][0], col + directions[directionIndex][1]};
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
  private boolean canFlipCard(int row, int col, int directionIndex, Card placedCard) {
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

    // Determine if the placed card's value is greater than the adjacent card's value
    return battleValues[0] > battleValues[1];
  }

  /**
   * Checks if the given position is within the game grid bounds.
   *
   * @param row the row to check
   * @param col the column to check
   * @return true if the position is valid, false otherwise
   */
  protected boolean isValidPosition(int row, int col) {
    return row >= 0 && row < rows && col >= 0 && col < cols;
  }

  /**
   * Changes the ownership of the card at the specified position to the current player.
   *
   * @param row the row of the card to flip
   * @param col the column of the card to flip
   */
  protected void flipCard(int row, int col) {
    Card cardToFlip = getCardAt(row, col);
    cardToFlip.setCardOwner(currentPlayer);
  }

  /**
   * Retrieves the card at the specified position in the grid.
   *
   * @param row the row of the desired card
   * @param col the column of the desired card
   * @return the Card at the specified position, or null if no card is present
   */
  protected Card getCardAt(int row, int col) {
    Cell cell = grid[row][col];
    Card card = null;

    if (cell instanceof CardCell) {
      card = cell.getCellCard();
    }

    return card;
  }

  @Override
  public boolean isGameOver() {
    for (int rowIdx = 0; rowIdx < rows; rowIdx++) {
      for (int colIdx = 0; colIdx < cols; colIdx++) {
        Cell cell = grid[rowIdx][colIdx];
        // If any card cell is empty, game is not over
        if (cell instanceof CardCell && cell.isEmpty()) {
          return false;
        }
      }
    }

    return true;
  }

  @Override
  public String determineWinner() throws IOException {
    if (!isGameOver()) {
      throw new IllegalStateException("Game is not over yet.");
    }

    String winner;

    if (getScore(Player.RED) > getScore(Player.BLUE)) {
      winner = Player.RED.asString;
      for (ModelObservers observer : this.modelObservers) {
        observer.onGameOver(winner, getScore(Player.RED));
      }
    } else if (getScore(Player.BLUE) > getScore(Player.RED)) {
      winner = Player.BLUE.asString;
      for (ModelObservers observer : this.modelObservers) {
        observer.onGameOver(winner, getScore(Player.BLUE));
      }
    } else {
      winner = "Tie";
      for (ModelObservers observer : this.modelObservers) {
        observer.onGameTie();
      }
    }

    return winner;
  }

  @Override
  public Cell[][] getGridCopy() {
    Cell[][] gridCopy = new Cell[grid.length][grid[0].length];

    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[row].length; col++) {
        gridCopy[row][col] = grid[row][col].getCell();
      }
    }

    return gridCopy;
  }

  @Override
  public List<Card> getPlayerHand(AnyPlayer player) {
    List<Card> playerHand = new ArrayList<>();
    if (player.equals(Player.RED)) {
      playerHand = getRedHand();
    } else if (player.equals(Player.BLUE)) {
      playerHand = getBlueHand();
    }
    return playerHand;
  }

  @Override
  public List<Card> getBlueHand() {
    List<Card> blueHandCopy = new ArrayList<>();

    for (Card card : this.blueHand) {
      blueHandCopy.add(card);
    }

    return blueHandCopy;
  }

  @Override
  public List<Card> getRedHand() {
    List<Card> redHandCopy = new ArrayList<>();

    for (Card card : this.redHand) {
      redHandCopy.add(card);
    }

    return redHandCopy;
  }

  @Override
  public Player getCurrentPlayer() {
    return this.currentPlayer;
  }

  @Override
  public int getRows() {
    return this.rows;
  }

  @Override
  public int getCols() {
    return this.cols;
  }

  @Override
  public Cell getCellAt(int row, int col) {
    Cell[][] gridCopy = getGridCopy();

    return gridCopy[row][col];
  }

  @Override
  public Player getCellOwner(int row, int col) {
    Cell cell = getCellAt(row, col);

    Card card = cell.getCellCard();

    return card.getOwner();
  }

  @Override
  public boolean isLegalPlay(int row, int col) {
    // If the row is out of bounds
    if (row >= this.rows || row < 0) {
      return false;
    }
    // If the column is out of bounds
    if (col >= this.cols || col < 0) {
      return false;
    }

    // If the cell is a CardCell and occupied
    Cell cell = grid[row][col];
    if (cell instanceof CardCell && !cell.isEmpty()) {
      return false;
    }

    // If cell is a hole, return false, otherwise true
    return !(cell instanceof Hole);
  }

  @Override
  public int getScore(Player player) {
    int redCount = redHand.size();
    int blueCount = blueHand.size();

    // Count cards on the grid
    for (int rowIdx = 0; rowIdx < rows; rowIdx++) {
      for (int colIdx = 0; colIdx < cols; colIdx++) {
        Cell cell = grid[rowIdx][colIdx];

        if (cell instanceof CardCell) {
          Card card = cell.getCellCard();

          if (card.getOwner() == Player.RED) {
            redCount++;
          } else if (card.getOwner() == Player.BLUE) {
            blueCount++;
          }
        }
      }
    }

    if (player == Player.RED) {
      return redCount;
    } else if (player == Player.BLUE) {
      return blueCount;
    }

    return 0;
  }

  @Override
  public boolean isStarted() {
    return this.isStarted;
  }

  @Override
  public boolean isPlayerTurn(AnyPlayer player) {
    return this.currentPlayer.equals(player.getPlayer());
  }

  @Override
  public void endTurn() {
    currentPlayer = (currentPlayer == Player.RED) ? Player.BLUE : Player.RED;

    for (ModelObservers observer : this.modelObservers) {
      try {
        observer.onTurnChanged();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void addObservers(ModelObservers modelObservers) {
    this.modelObservers.add(modelObservers);
  }

  // INVARIANT: Every cell is either a hole or a valid card cell.
  // INVARIANT: If a card cell contains a card, that card has a valid owner (RED or BLUE)
}