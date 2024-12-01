package cs3500.threetrios.model.mocks;

import java.util.List;

import cs3500.threetrios.controller.ModelObservers;
import cs3500.threetrios.model.AnyPlayer;
import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CardModel;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.TriosModel;

/**
 * A mock that causes the default move to occur, for the purpose of testing strategies.
 *
 * <p>All cards have a flipNum of zero, causes a tie</p>
 */
public class DefaultTriosMock implements TriosModel {
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
    CellMock[][] bigGrid = new CellMock[5][1];
    bigGrid[0][0] = new CellMock();
    bigGrid[1][0] = new CellMock();
    bigGrid[2][0] = new CellMock();
    bigGrid[3][0] = new CellMock();
    bigGrid[4][0] = new CellMock();

    return bigGrid;
  }

  @Override
  public List<Card> getPlayerHand(AnyPlayer player) {
    return getBlueHand();
  }

  @Override
  public List<Card> getBlueHand() {
    Card algo = new CardModel("algo","3", "8", "A", "7");
    Card engineering = new CardModel("Engineering","5", "1", "2", "A");
    Card windBird = new CardModel("WindBird", "7", "2", "5", "3");

    return List.of(algo, windBird, engineering);
  }

  @Override
  public List<Card> getRedHand() {
    Card linAlg = new CardModel("LinAlg","8", "1", "2", "3");
    Card corruptKing = new CardModel("CorruptKing", "7", "3", "9", "A");
    Card skyWhale = new CardModel("SkyWhale", "4", "5", "9", "9");

    return List.of(corruptKing, linAlg, skyWhale);
  }

  @Override
  public Player getCurrentPlayer() {
    return Player.RED;
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

  /**
   * All cards flip only 0 cards, so tie-breaking default move should occur.
   */
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
