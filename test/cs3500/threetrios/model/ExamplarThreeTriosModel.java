package cs3500.threetrios.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Testing all public methods of the model.
 */
public class ExamplarThreeTriosModel {
  protected Cell[][] grid;
  protected Cell[][] bigGrid;
  protected TriosModel model;
  protected TriosModel randomSeedModel;
  protected Card corruptKing;
  protected Card angryDragon;
  protected Card windBird;
  protected Card heroKnight;
  protected Card worldDragon;
  protected Card skyWhale;
  protected List<Card> deck;
  protected TriosModel bigRandomSeedModel;

  @Before
  public void setup() {
    this.grid = new Cell[3][1];
    this.grid[0][0] = new CardCell();
    this.grid[1][0] = new CardCell();
    this.grid[2][0] = new CardCell();

    // grid
    // _
    // _
    // _

    this.bigGrid = new Cell[5][1];
    this.bigGrid[0][0] = new CardCell();
    this.bigGrid[1][0] = new CardCell();
    this.bigGrid[2][0] = new CardCell();
    this.bigGrid[3][0] = new CardCell();
    this.bigGrid[4][0] = new CardCell();

    // bigGrid
    // _
    // _
    // _
    // _
    // _

    this.corruptKing = new CardModel("CorruptKing", "7", "3", "9", "A");
    this.angryDragon = new CardModel("AngryDragon", "2", "8", "9", "9");
    this.windBird = new CardModel("WindBird", "7", "2", "5", "3");
    this.heroKnight = new CardModel("HeroKnight", "A", "2", "4", "4");
    this.worldDragon = new CardModel("WorldDragon", "8", "3", "5", "7");
    this.skyWhale = new CardModel("SkyWhale", "4", "5", "9", "9");

    this.deck = List.of(corruptKing, angryDragon, windBird, heroKnight, worldDragon, skyWhale);

    this.model = new ThreeTriosModel(grid, deck);
    this.randomSeedModel = new ThreeTriosModel(grid, deck, new Random(1));

    this.bigRandomSeedModel = new ThreeTriosModel(bigGrid, deck, new Random(1));
  }

  @Test
  public void testEvenNumberCellsThrowsException() {
    Assert.assertThrows("Should throw exception when given an even number of cells",
            IllegalArgumentException.class, () -> new ThreeTriosModel(new Cell[3][2], deck));
  }

  @Test
  public void testSmallDeckThrowsException() {
    Assert.assertThrows("Should throw exception with too small of a deck",
            IllegalArgumentException.class, () -> new ThreeTriosModel(new Cell[3][3], deck));
  }

  @Test
  public void testStartsWithRedPlayer() {
    Assert.assertEquals("Checking game starts with red player",
            Player.RED, model.getCurrentPlayer());
  }

  @Test
  public void testRedPlayersHandInitialized() {
    Assert.assertEquals("Checking red player's hand is properly initialized",
            List.of(worldDragon, corruptKing), randomSeedModel.getRedHand());
    Assert.assertEquals("Checking red player's hand is properly initialized",
            List.of(worldDragon, corruptKing), randomSeedModel.getRedHand());
  }

  @Test
  public void testBluePlayersHandInitialized() {
    Assert.assertEquals("Checking game starts with red player",
            List.of(windBird, angryDragon), randomSeedModel.getBlueHand());
    Assert.assertEquals("Checking game starts with red player",
            List.of(windBird, angryDragon), randomSeedModel.getBlueHand());
  }

  @Test
  public void testPlaceCardWorks() throws IOException {
    Cell cellBefore = this.grid[0][0];
    Card cardBefore = cellBefore.getCellCard();

    Assert.assertNull("Checking that cell has no card initially",
            cardBefore);

    // Using random seed model, so we know that red player has this card
    randomSeedModel.placeCard(0, 0, worldDragon);

    Cell cellAfter = this.grid[0][0];
    Card cardAfter = cellAfter.getCellCard();

    Assert.assertEquals("Checking card is properly placed in the correct position",
            worldDragon, cardAfter);
  }

  @Test
  public void testBattlePhasePlayerFlipsCard() throws IOException {

    // Red places WorldDragon at (0, 0)
    randomSeedModel.placeCard(0, 0, worldDragon);

    Cell firstCell = this.grid[0][0];
    Card cardAtFirst = firstCell.getCellCard();

    Assert.assertEquals("Checking card is properly placed in the correct position",
            worldDragon, cardAtFirst);
    Assert.assertEquals("Checking that RED initially owns this card",
            Player.RED, cardAtFirst.getOwner());

    // Nothing happens, Blue places WindBird at (1, 0)
    randomSeedModel.placeCard(1, 0, windBird);

    Cell secondCell = this.grid[1][0];
    Card cardAtSecond = secondCell.getCellCard();

    Assert.assertEquals("Checking card is properly placed in the correct position",
            windBird, cardAtSecond);

    // WorldDragon
    //  8
    // 7 5
    //  3

    // WindBird:
    //  7
    // 3 5
    //  2

    // Blue wins with WindBird, Red's card should be flipped, Blue owns the card at (0, 0) now
    Assert.assertEquals("Checking that BLUE now owns this card",
            Player.BLUE, cardAtFirst.getOwner());
  }

  @Test
  public void testBattlePhasePlayerDoesNotFlipCard() throws IOException {

    // Red places WorldDragon at (0, 0)
    randomSeedModel.placeCard(0, 0, worldDragon);

    Cell firstCell = this.grid[0][0];
    Card cardAtFirst = firstCell.getCellCard();

    Assert.assertEquals("Checking card is properly placed in the correct position",
            worldDragon, cardAtFirst);
    Assert.assertEquals("Checking that RED initially owns this card",
            Player.RED, cardAtFirst.getOwner());

    // Nothing happens, Blue places AngryDragon at (1, 0)
    randomSeedModel.placeCard(1, 0, angryDragon);

    Cell secondCell = this.grid[1][0];
    Card cardAtSecond = secondCell.getCellCard();

    Assert.assertEquals("Checking card is properly placed in the correct position",
            angryDragon, cardAtSecond);

    // WorldDragon
    //  8
    // 7 5
    //  3

    // AngryDragon:
    //  2
    // 9 9
    //  8

    // Blue does NOT win with AngryDragon, no flip, Red should still own card at (0, 0)
    Assert.assertEquals("Checking that RED still owns this card",
            Player.RED, cardAtFirst.getOwner());
  }

  @Test
  public void testIsGameOver() throws IOException {
    Assert.assertFalse("Game should not be over to begin with",
            randomSeedModel.isGameOver());

    // Fill the board, should end game
    randomSeedModel.placeCard(0, 0, worldDragon);
    randomSeedModel.placeCard(1, 0, angryDragon);
    randomSeedModel.placeCard(2, 0, corruptKing);

    Assert.assertTrue("Game should now be over since board has been filled",
            randomSeedModel.isGameOver());
  }

  @Test
  public void testTieWin() throws IOException {
    // Ensue gameplay
    randomSeedModel.placeCard(0, 0, worldDragon);
    randomSeedModel.placeCard(1, 0, angryDragon);
    randomSeedModel.placeCard(2, 0, corruptKing);

    // WorldDragon:
    //  8
    // 7 5          // Owned by Red
    //  3

    // AngryDragon:
    //  2
    // 9 9          // Owned by Blue
    //  8

    // CorruptKing:
    //  7
    // A 9          // Owned by Red
    //  3

    // Red has two cards on the board, none in hand
    // Blue has one card on the board, one in hand

    // TIE !

    Assert.assertEquals("Game should end in a tie",
            "Tie", randomSeedModel.determineWinner());
  }

  @Test
  public void testBattlePhaseWithComboStep() throws IOException {
    // Checking player's hands with random seed
    Assert.assertEquals("Checking red player's hand is properly initialized",
            List.of(worldDragon, corruptKing, skyWhale), bigRandomSeedModel.getRedHand());
    Assert.assertEquals("Checking blue player's hand is properly initialized",
            List.of(windBird, angryDragon, heroKnight), bigRandomSeedModel.getBlueHand());

    // Ensue gameplay
    bigRandomSeedModel.placeCard(0, 0, worldDragon);
    bigRandomSeedModel.placeCard(4, 0, angryDragon);
    bigRandomSeedModel.placeCard(1, 0, corruptKing);

    // Game-changing move.
    // Blue places at (2, 0), takes (1, 0) in first step of battle phase, AND THEN
    // Blue takes (0, 0) during the combo step.
    bigRandomSeedModel.placeCard(2, 0, windBird);

    // BEFORE BATTLE PHASE:

    // WorldDragon:
    //  8
    // 7 5          // Owned by Red
    //  3

    // CorruptKing:
    //  7
    // A 9          // Owned by Red
    //  3

    // WindBird:
    //  7
    // 9 5          // Owned by Blue
    //  2

    // _

    // AngryDragon:
    //  2
    // 9 9          // Owned by Blue
    //  8

    // AFTER BATTLE PHASE:

    // WorldDragon:
    //  8
    // 7 5          // Owned by Blue
    //  3

    // CorruptKing:
    //  7
    // A 9          // Owned by Blue
    //  3

    // WindBird:
    //  7
    // 9 5          // Owned by Blue
    //  2

    // _

    // AngryDragon:
    //  2
    // 9 9          // Owned by Blue
    //  8

    Cell firstCell = this.bigGrid[0][0];
    Card cardAtFirst = firstCell.getCellCard();

    Cell secondCell = this.bigGrid[1][0];
    Card cardAtSecond = secondCell.getCellCard();

    Cell thirdCell = this.bigGrid[2][0];
    Card cardAtThird = thirdCell.getCellCard();

    Assert.assertEquals("Checking that BLUE owns the first cell",
            Player.BLUE, cardAtFirst.getOwner());
    Assert.assertEquals("Checking that BLUE owns the second cell",
            Player.BLUE, cardAtSecond.getOwner());
    Assert.assertEquals("Checking that BLUE owns the third cell",
            Player.BLUE, cardAtThird.getOwner());
  }

  @Test
  public void testPlayerWinWithMorePoints() throws IOException {
    // Ensue gameplay
    bigRandomSeedModel.placeCard(0, 0, worldDragon);
    bigRandomSeedModel.placeCard(4, 0, angryDragon);
    bigRandomSeedModel.placeCard(1, 0, corruptKing);
    // Blue takes the whole board with this next move
    bigRandomSeedModel.placeCard(2, 0, windBird);
    // BOOM! Now Red just took the whole board
    bigRandomSeedModel.placeCard(3, 0, skyWhale);

    // Final Board:

    // WorldDragon:
    //  8
    // 7 5          // Owned by Red
    //  3

    // CorruptKing:
    //  7
    // A 9          // Owned by Red
    //  3

    // WindBird:
    //  7
    // 9 5          // Owned by Red
    //  2

    // SkyWhale:
    //  4
    // 9 9          // Owned by Red
    //  5

    // AngryDragon:
    //  2
    // 9 9          // Owned by Red
    //  8

    // In a landslide of events, Red takes the whole board from Blue when SkyWhale was placed,
    // RED WIN !

    Assert.assertEquals("Red should be the winner",
            "RED", bigRandomSeedModel.determineWinner());
  }

  @Test
  public void testDetermineWinnerThrowsException() throws IOException {
    // Ensue gameplay
    bigRandomSeedModel.placeCard(0, 0, worldDragon);
    bigRandomSeedModel.placeCard(4, 0, angryDragon);
    bigRandomSeedModel.placeCard(1, 0, corruptKing);
    bigRandomSeedModel.placeCard(2, 0, windBird);

    // No card has been placed at (3, 0), game is not over

    Assert.assertThrows("Should throw exception when game is not over",
            IllegalStateException.class, () -> bigRandomSeedModel.determineWinner());
  }

  @Test
  public void testPlaceCardRemovesFromHand() throws IOException {
    // Ensue gameplay
    bigRandomSeedModel.placeCard(0, 0, worldDragon);
    bigRandomSeedModel.placeCard(4, 0, angryDragon);
    bigRandomSeedModel.placeCard(1, 0, corruptKing);
    bigRandomSeedModel.placeCard(2, 0, windBird);

    Assert.assertEquals("Red's hand should be properly updated'",
            List.of(skyWhale), bigRandomSeedModel.getRedHand());
    Assert.assertEquals("Blue's hand should be properly updated",
            List.of(heroKnight), bigRandomSeedModel.getBlueHand());
  }

  @Test
  public void testPlaceCardToHoleThrows() {
    Cell[][] gridWithHole = new Cell[3][1];
    gridWithHole[0][0] = new CardCell();
    gridWithHole[1][0] = new Hole();
    gridWithHole[2][0] = new CardCell();

    Assert.assertThrows("Should throw exception when trying to place to a hole",
            IllegalStateException.class,
        () -> bigRandomSeedModel.placeCard(1, 0, gridWithHole[1][0].getCellCard()));
  }

  @Test
  public void testPlaceCardToAnOccupiedCellThrows() throws IOException {
    bigRandomSeedModel.placeCard(0, 0, worldDragon);

    Assert.assertThrows("Should throw exception when trying to place to an occupied cell",
            IllegalArgumentException.class,
        () -> bigRandomSeedModel.placeCard(0, 0, angryDragon));
  }

  @Test
  public void testPlaceCardWithAnotherPlayersCard() {
    // Red doesn't own AngryDragon, Blue does, so here the first move (Red move) tries using
    // Blue's card
    Assert.assertThrows("Should throw exception when trying to place another players card",
            IllegalArgumentException.class,
        () ->  bigRandomSeedModel.placeCard(0, 0, angryDragon));
  }

  @Test
  public void testNumOfFlippableCards() throws IOException {
    bigRandomSeedModel.placeCard(0, 0, worldDragon);
    bigRandomSeedModel.placeCard(4, 0, angryDragon);
    bigRandomSeedModel.placeCard(1, 0, corruptKing);
    bigRandomSeedModel.placeCard(2, 0, windBird);

    Assert.assertEquals("Should be able to flip four cards at specified position",
            4, bigRandomSeedModel.numOfFlippableCards(3, 0, skyWhale));
  }
}