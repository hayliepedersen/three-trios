package cs3500.threetrios.controller;

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
import cs3500.threetrios.model.Hole;
import cs3500.threetrios.model.HumanPlayer;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.ThreeTriosModel;
import cs3500.threetrios.model.TriosModel;
import cs3500.threetrios.model.mocks.DefaultTriosMock;

/**
 * A class to test the trios controller.
 */
public class TriosControllerTests {
  private TriosModel model;
  private Card corruptKing;
  private Card heroKnight;
  private ViewFeatures controller;

  @Before
  public void setup() {
    this.corruptKing = new CardModel("CorruptKing", "7", "3", "9", "A");
    Card angryDragon = new CardModel("AngryDragon", "2", "8", "9", "9");
    Card windBird = new CardModel("WindBird", "7", "2", "5", "3");
    this.heroKnight = new CardModel("HeroKnight", "A", "2", "4", "4");
    Card worldDragon = new CardModel("WorldDragon", "8", "3", "5", "7");
    Card skyWhale = new CardModel("SkyWhale", "4", "5", "9", "9");
    Card systems = new CardModel("Systems", "1", "4", "3", "7");
    Card algo = new CardModel("algo", "3", "8", "A", "7");
    Card linAlg = new CardModel("LinAlg", "8", "1", "2", "3");
    Card stats = new CardModel("Stats", "6", "3", "7", "7");
    Card engineering = new CardModel("Engineering", "5", "1", "2", "A");

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

    List<Card> deck = List.of(corruptKing, angryDragon, windBird, heroKnight, worldDragon,
            skyWhale, systems, algo, stats, linAlg, engineering);

    this.model = new ThreeTriosModel(bigGrid, deck, new Random(1));
    ModelMockController modelMock = new ModelMockController();

    DefaultTriosMock defaultMock = new DefaultTriosMock();
  }

  // tests that a card is correctly played to the grid
  @Test
  public void testCardPlaced() throws IOException {
    TriosController controller = new TriosController(model,
            new HumanPlayer(model, Player.RED), new GraphicViewMock());

    controller.cardSelected(corruptKing);

    controller.cardPlaced(0, 0, corruptKing);

    Assert.assertEquals(corruptKing, model.getCellAt(0, 0).getCellCard());
  }

  // tests that a player cannot play out of bounds
  @Test
  public void testCardPlacedOutOfBounds() throws IOException {
    TriosController controller = new TriosController(this.model,
            new HumanPlayer(this.model, Player.RED), new GraphicViewMock());

    controller.cardSelected(corruptKing);
    Assert.assertThrows(IllegalArgumentException.class, () -> controller.cardPlaced(
            6, 6, corruptKing));
  }

  // tests that a player cannot play to a Hole
  @Test
  public void testCardPlacedHole() throws IOException {
    TriosController controller = new TriosController(this.model,
            new HumanPlayer(this.model, Player.RED), new GraphicViewMock());

    controller.cardSelected(corruptKing);
    Assert.assertThrows(IllegalArgumentException.class, () -> controller.cardPlaced(
            4, 2, corruptKing));
  }

  // tests that a player cant play from a hand that isn't theirs
  @Test
  public void testCardPlacedInvalidHand() throws IOException {
    TriosController controller = new TriosController(this.model,
            new HumanPlayer(this.model, Player.RED), new GraphicViewMock());

    controller.cardSelected(heroKnight);
    controller.cardPlaced(0, 0, heroKnight);
    Assert.assertEquals(model.getCellAt(0, 0).getCellCard(), null);
  }

  // test that a card accurately selects
  @Test
  public void testCardSelection() throws IOException {
    TriosController controller = new TriosController(this.model,
            new HumanPlayer(this.model, Player.RED), new GraphicViewMock());

    controller.cardSelected(corruptKing);
    Assert.assertEquals(controller.selectedCard(), corruptKing);
  }

  // test that a card won't select if it's not your card
  @Test
  public void testCardSelectionInvalidHand() throws IOException {
    TriosController controller = new TriosController(this.model,
            new HumanPlayer(this.model, Player.RED), new GraphicViewMock());

    controller.cardSelected(heroKnight);
    Assert.assertEquals(controller.selectedCard(), null);
  }

  // test that a card won't select if it's not your turn
  @Test
  public void testCardSelectionInvalidTurn() throws IOException {
    TriosController controller = new TriosController(this.model,
            new HumanPlayer(this.model, Player.BLUE), new GraphicViewMock());

    controller.cardSelected(heroKnight);
    Assert.assertEquals(controller.selectedCard(), null);
  }

  // testing that after placing a card, the current player changes
  @Test
  public void testPlacingCardEndsTurn() throws IOException {
    TriosController controller = new TriosController(this.model,
            new HumanPlayer(this.model, Player.RED), new GraphicViewMock());

    controller.cardSelected(corruptKing);
    controller.cardPlaced(0, 0, controller.selectedCard());
    Assert.assertEquals(model.getCurrentPlayer(), Player.BLUE);
  }

  // testing that ending a turn changes the current player
  @Test
  public void testEndTurn() throws IOException {
    TriosController controller = new TriosController(this.model,
            new HumanPlayer(this.model, Player.RED), new GraphicViewMock());

    controller.endTurn();
    Assert.assertEquals(model.getCurrentPlayer(), Player.BLUE);
  }
}
