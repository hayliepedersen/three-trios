package cs3500.threetrios.model.mocks;

import java.util.List;

import cs3500.threetrios.controller.ModelObservers;
import cs3500.threetrios.model.AnyPlayer;
import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CardCell;
import cs3500.threetrios.model.CardModel;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.TriosModel;

/**
 * A mock that causes the move with the highest number of flippable cards to occur,
 * for the purpose of testing strategies.
 *
 * <p>Returns the highest flipNum only for windBird, so windBird should be chosen</p>
 */
public class HighestFlipNumMock implements TriosModel {
  private final Card algo;
  private final Card engineering;
  private final Card windBird;
  private final Card linAlg;
  private final Card corruptKing;
  private final Card skyWhale;

  /**
   * Initializes this mock.
   */
  public HighestFlipNumMock() {

    this.algo = new CardModel("algo","3", "8", "A", "7");
    this.engineering = new CardModel("Engineering","5", "1", "2", "A");
    this.windBird = new CardModel("WindBird", "7", "2", "5", "3");

    this.linAlg = new CardModel("LinAlg","8", "1", "2", "3");
    this.corruptKing = new CardModel("CorruptKing", "7", "3", "9", "A");
    this.skyWhale = new CardModel("SkyWhale", "4", "5", "9", "9");

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
    Cell[][] bigGrid = new Cell[5][1];
    bigGrid[0][0] = new CardCell();
    bigGrid[1][0] = new CardCell();
    bigGrid[2][0] = new CardCell();
    bigGrid[3][0] = new CardCell();
    bigGrid[4][0] = new CardCell();

    bigGrid[0][0].addCard(corruptKing);
    bigGrid[2][0].addCard(skyWhale);
    bigGrid[4][0].addCard(linAlg);

    return bigGrid;
  }

  @Override
  public List<Card> getPlayerHand(AnyPlayer player) {
    return getBlueHand();
  }

  @Override
  public List<Card> getBlueHand() {
    return List.of(algo, windBird, engineering);
  }

  @Override
  public List<Card> getRedHand() {
    return List.of(corruptKing, linAlg, skyWhale);
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
    return 1;
  }

  @Override
  public Cell getCellAt(int row, int col) {
    return getGridCopy()[row][col];
  }

  @Override
  public Player getCellOwner(int row, int col) {
    return Player.RED;
  }

  @Override
  public boolean isLegalPlay(int row, int col) {
    return true;
  }

  @Override
  public int numOfFlippableCards(int row, int col, Card card) {
    if (card.equals(windBird)) {
      return 5;
    }

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