package cs3500.threetrios.strategy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CardCell;
import cs3500.threetrios.model.CardModel;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.ExamplarThreeTriosModel;
import cs3500.threetrios.model.Hole;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.ThreeTriosModel;
import cs3500.threetrios.model.mocks.CornerWinMock;
import cs3500.threetrios.model.mocks.DefaultTriosMock;
import cs3500.threetrios.model.TriosModel;
import cs3500.threetrios.model.mocks.HighestFlipNumMock;

/**
 * Represents a class for testing strategies.
 */
public class StrategyTests extends ExamplarThreeTriosModel {
  private PlayerStrategy flipStrategy;
  private PlayerStrategy cornerStrategy;
  private TriosModel fiveThreeModel;
  Card systems;
  Card algo;
  Card linAlg;
  Card stats;
  Card engineering;

  @Before
  public void setup() {
    super.setup();

    this.flipStrategy = new HighestFlipNum();
    this.cornerStrategy = new GoForCorners();

    this.systems = new CardModel("Systems","1", "4", "3", "7");
    this.algo = new CardModel("algo","3", "8", "A", "7");
    this.linAlg = new CardModel("LinAlg","8", "1", "2", "3");
    this.stats = new CardModel("Stats","6", "3", "7", "7");
    this.engineering = new CardModel("Engineering","5", "1", "2", "A");

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

    List<Card> deck = List.of(corruptKing, angryDragon, windBird, heroKnight, worldDragon,
            skyWhale, systems, algo, stats, linAlg, engineering);

    this.fiveThreeModel = new ThreeTriosModel(fiveThreeGrid, deck, new Random(1));
  }

  @Test
  public void testHighestFlippableCardsStrategy() throws IOException {
    bigRandomSeedModel.placeCard(0, 0, worldDragon);
    bigRandomSeedModel.placeCard(4, 0, angryDragon);
    bigRandomSeedModel.placeCard(1, 0, corruptKing);

    // Strategy should choose (2, 0) over (3, 0) because placing at (2, 0) flips two cards
    // whereas placing at (3, 0) only flips one. Strategy chooses windBird because it's the first
    // card in player's hand that sets off chain reaction, heroKnight would also work, but they
    // achieve the same effect, simply just use windBird

    Assert.assertEquals("Checking that best move is chosen",
            new BestMove(2, 0, windBird), flipStrategy.chooseMove(bigRandomSeedModel,
                    Player.BLUE));
  }

  @Test
  public void testDefaultHighestFlipStrategy() throws IOException {
    bigRandomSeedModel.placeCard(4, 0, worldDragon);

    // Board:

    // _

    // _

    // _

    // _

    // WorldDragon:
    //  8
    // 7 5          // Owned by Red
    //  3

    // None of blue's cards can flip this card, so default move should happen,
    // playing first card in hand to the upper-leftmost position

    Assert.assertEquals("Checking that default move is chosen",
            new BestMove(0, 0, windBird), flipStrategy.chooseMove(bigRandomSeedModel,
                    Player.BLUE));
  }

  @Test
  public void testCornerStrategyBlue() {
    // Checking player's hands with random seed
    Assert.assertEquals("Checking red player's hand is properly initialized",
            List.of(corruptKing, linAlg, skyWhale, systems, angryDragon),
            fiveThreeModel.getRedHand());
    Assert.assertEquals("Checking blue player's hand is properly initialized",
            List.of(algo, windBird, engineering, heroKnight, stats), fiveThreeModel.getBlueHand());

    // Blue's highest defense value is in the top-left corner with algo card, 18 value

    Assert.assertEquals("Checking that best move is chosen",
            new BestMove(0, 0, algo), cornerStrategy.chooseMove(fiveThreeModel,
                    Player.BLUE));
  }

  @Test
  public void testCornerStrategyTopLeftWins() {
    // Checking player's hands with random seed
    Assert.assertEquals("Checking red player's hand is properly initialized",
            List.of(corruptKing, linAlg, skyWhale, systems, angryDragon),
            fiveThreeModel.getRedHand());
    Assert.assertEquals("Checking blue player's hand is properly initialized",
            List.of(algo, windBird, engineering, heroKnight, stats), fiveThreeModel.getBlueHand());

    // Should be angryDragon with top-left, highest defense score out of all cards when south and
    // east are exposed

    Assert.assertEquals("Checking that best move is chosen",
            new BestMove(0, 0, angryDragon), cornerStrategy.chooseMove(fiveThreeModel,
                    Player.RED));
  }

  @Test
  public void testFlipNumWithDefaultTriosMock() {
    TriosModel defaultMock = new DefaultTriosMock();

    Assert.assertEquals("Checking that the default move is chosen, all cards in mock "
            + "have a flip num of zero, tie occurs",
            new BestMove(0, 0, algo), flipStrategy.chooseMove(defaultMock,
                    Player.BLUE));
  }

  @Test
  public void testFlipNumWithHighestFlipNumMock() {
    TriosModel flipMock = new HighestFlipNumMock();

    Assert.assertEquals("WindBird should win with mock as it is the only card that has a "
                    + "flip num besides zero",
            new BestMove(0, 0, windBird), flipStrategy.chooseMove(flipMock,
                    Player.BLUE));
  }

  @Test
  public void testCornerStrategyWithCornerWinMock() {
    TriosModel cornerMock = new CornerWinMock();

    Card winCard = new CardModel("Winner", "1", "A", "1", "A");

    Assert.assertEquals("WinCard should win with mock as it has the best defense",
            new BestMove(0, 2, winCard), cornerStrategy.chooseMove(cornerMock,
                    Player.BLUE));
  }
}
