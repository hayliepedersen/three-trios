package cs3500.threetrios.model.mocks;

import java.util.List;

import cs3500.threetrios.controller.ModelObservers;
import cs3500.threetrios.model.AnyPlayer;
import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CardCell;
import cs3500.threetrios.model.CardModel;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.Hole;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.TriosModel;

/**
 * Represents a mock where there is one optimal corner.
 */
public class CornerWinMock implements TriosModel {
  private final Card algo;
  private final Card winCard;
  private final Card windBird;

  /**
   * Initializes this mock.
   */
  public CornerWinMock() {

    this.algo = new CardModel("algo", "3", "8", "A", "7");
    this.winCard = new CardModel("Winner", "1", "A", "1", "A");
    this.windBird = new CardModel("WindBird", "7", "2", "5", "3");

  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public String determineWinner() {
    return "RED";
  }

  @Override
  public Cell[][] getGridCopy() {
    Cell[][] fiveThreeGrid = new Cell[5][3];
    fiveThreeGrid[0][0] = new CardCell();
    fiveThreeGrid[1][0] = new CardCell();
    fiveThreeGrid[2][0] = new CardCell();
    fiveThreeGrid[3][0] = new CardCell();
    fiveThreeGrid[4][0] = new CardCell();
    fiveThreeGrid[0][1] = new Hole();
    fiveThreeGrid[1][1] = new Hole();
    fiveThreeGrid[2][1] = new Hole();
    fiveThreeGrid[3][1] = new Hole();
    fiveThreeGrid[4][1] = new Hole();
    fiveThreeGrid[0][2] = new CardCell();
    fiveThreeGrid[1][2] = new CardCell();
    fiveThreeGrid[2][2] = new CardCell();
    fiveThreeGrid[3][2] = new CardCell();
    fiveThreeGrid[4][2] = new Hole();

    return fiveThreeGrid;
  }

  @Override
  public List<Card> getPlayerHand(AnyPlayer player) {
    return getBlueHand();
  }

  @Override
  public List<Card> getBlueHand() {
    return List.of(algo, windBird, winCard);
  }

  @Override
  public List<Card> getRedHand() {
    return List.of();
  }

  @Override
  public Player getCurrentPlayer() {
    return Player.BLUE;
  }

  @Override
  public int getRows() {
    return 5;
  }

  @Override
  public int getCols() {
    return 3;
  }

  @Override
  public Cell getCellAt(int row, int col) {
    return getGridCopy()[row][col];
  }

  @Override
  public Player getCellOwner(int row, int col) {
    return Player.BLUE;
  }

  @Override
  public boolean isLegalPlay(int row, int col) {
    return true;
  }

  @Override
  public int numOfFlippableCards(int row, int col, Card card) {
    return 0;
  }

  @Override
  public int getScore(Player player) {
    return 5;
  }

  @Override
  public boolean isStarted() {
    return false;
  }

  @Override
  public boolean isPlayerTurn(AnyPlayer player) {
    return false;
  }

  @Override
  public void placeCard(int row, int col, Card card) {
    // Do nothing
  }

  @Override
  public void startGame() {
    // Do nothing
  }

  @Override
  public void addObservers(ModelObservers modelObservers) {
    // Do nothing
  }

  @Override
  public void endTurn() {
    // Do nothing
  }
}
