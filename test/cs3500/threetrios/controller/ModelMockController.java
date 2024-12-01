package cs3500.threetrios.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cs3500.threetrios.model.AnyPlayer;
import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CardCell;
import cs3500.threetrios.model.CardModel;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.Hole;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.TriosModel;

/**
 * Represents a model mock.
 */
public class ModelMockController implements TriosModel {
  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public String determineWinner() throws IOException {
    return "Red";
  }

  @Override
  public Cell[][] getGridCopy() {
    Cell[][] bigGrid = new Cell[5][3];
    bigGrid[0][0] = new CardCell();
    bigGrid[1][0] = new CardCell();
    bigGrid[2][0] = new CardCell();
    bigGrid[3][0] = new CardCell();
    bigGrid[4][0] = new CardCell();
    bigGrid[0][1] = new Hole();
    bigGrid[1][1] = new Hole();
    bigGrid[2][1] = new Hole();
    bigGrid[3][1] = new Hole();
    bigGrid[4][1] = new Hole();
    bigGrid[0][2] = new CardCell();
    bigGrid[1][2] = new CardCell();
    bigGrid[2][2] = new CardCell();
    bigGrid[3][2] = new CardCell();
    bigGrid[4][2] = new Hole();

    return bigGrid;
  }

  @Override
  public List<Card> getPlayerHand(AnyPlayer player) {
    Card corruptKing = new CardModel("CorruptKing", "7", "3", "9", "A");
    Card angryDragon = new CardModel("AngryDragon", "2", "8", "9", "9");
    Card linAlg = new CardModel("LinAlg", "8", "1", "2", "3");
    Card systems = new CardModel("Systems", "1", "4", "3", "7");
    Card skyWhale = new CardModel("SkyWhale", "4", "5", "9", "9");

    List<Card> playerHand = new ArrayList<>(List.of(corruptKing, linAlg, skyWhale,
            systems, angryDragon));

    return playerHand;
  }

  @Override
  public List<Card> getBlueHand() {
    Card windBird = new CardModel("WindBird", "7", "2", "5", "3");
    Card heroKnight = new CardModel("HeroKnight", "A", "2", "4", "4");
    Card algo = new CardModel("algo", "3", "8", "A", "7");
    Card engineering = new CardModel("Engineering", "5", "1", "2", "A");
    Card stats = new CardModel("Stats", "6", "3", "7", "7");

    List<Card> playerHand = new ArrayList<>(List.of(algo, windBird, engineering,
            heroKnight, stats));

    return playerHand;
  }

  @Override
  public List<Card> getRedHand() {
    Card corruptKing = new CardModel("CorruptKing", "7", "3", "9", "A");
    Card angryDragon = new CardModel("AngryDragon", "2", "8", "9", "9");
    Card linAlg = new CardModel("LinAlg", "8", "1", "2", "3");
    Card systems = new CardModel("Systems", "1", "4", "3", "7");
    Card skyWhale = new CardModel("SkyWhale", "4", "5", "9", "9");

    List<Card> playerHand = new ArrayList<>(List.of(corruptKing, linAlg, skyWhale,
            systems, angryDragon));

    return playerHand;
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
    return 3;
  }

  @Override
  public Cell getCellAt(int row, int col) {
    return this.getGridCopy()[row][col];
  }

  @Override
  public Player getCellOwner(int row, int col) {
    return this.getGridCopy()[row][col].getCellCard().getOwner();
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
    return 0;
  }

  @Override
  public boolean isStarted() {
    return true;
  }

  @Override
  public boolean isPlayerTurn(AnyPlayer player) {
    return true;
  }

  @Override
  public void placeCard(int row, int col, Card card) throws IOException {
    // this is a mock
  }

  @Override
  public void startGame() {
    // this is a mock
  }

  @Override
  public void addObservers(ModelObservers modelObservers) {
    // this is a mock
  }

  @Override
  public void endTurn() {
    // this is a mock
  }
}
