package cs3500.threetrios.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class VariantOneModelTests extends ExamplarThreeTriosModel {
  protected VariantOneModel variantOneModelReverse;
  private VariantOneModel variantOneModelFallenAce;

  @Before
  public void setup() {
    super.setup();

    // Model where only reverse rule is applied
    this.variantOneModelReverse = new VariantOneModel(bigGrid, deck, true, false,
            new Random(1));
    // Model where only fallenAce rule is applied
    this.variantOneModelFallenAce = new VariantOneModel(bigGrid, deck, false, true,
            new Random(1));
  }

  @Test
  public void testBattlePhaseWitReverseRule() throws IOException {
    // Checking player's hands with random seed
    Assert.assertEquals("Checking red player's hand is properly initialized",
            List.of(worldDragon, corruptKing, skyWhale), variantOneModelReverse.getRedHand());
    Assert.assertEquals("Checking blue player's hand is properly initialized",
            List.of(windBird, angryDragon, heroKnight), variantOneModelReverse.getBlueHand());

    // Ensue gameplay
    variantOneModelReverse.placeCard(0, 0, worldDragon); // Red play
    variantOneModelReverse.placeCard(1, 0, angryDragon); // Blue play

    /*
     Under reverse rule, blue should take over WorldDragon

     BEFORE BATTLE PHASE:
     WorldDragon:
      8
     7 5          // Owned by RED
      3
     AngryDragon:
      2
     9 9          // Owned by Blue
      8

     AFTER BATTLE PHASE:
     WorldDragon:
      8
     7 5          // Owned by BLUE
      3
     AngryDragon:
      2
     9 9          // Owned by Blue
      8
    */

    Cell firstCell = this.bigGrid[0][0];
    Card cardAtFirst = firstCell.getCellCard();

    Cell secondCell = this.bigGrid[1][0];
    Card cardAtSecond = secondCell.getCellCard();

    Assert.assertEquals("Checking that BLUE owns the first cell",
            Player.BLUE, cardAtFirst.getOwner());
    Assert.assertEquals("Checking that BLUE owns the second cell",
            Player.BLUE, cardAtSecond.getOwner());
  }

  @Test
  public void testBattlePhaseWithFallenAceRule() throws IOException {

    variantOneModelFallenAce.placeCard(0, 0, worldDragon); // Red play
    variantOneModelFallenAce.placeCard(4, 0, heroKnight); // Blue play

    this.skyWhale = new CardModel("SkyWhale", "4", "1", "9", "9");
    // Red playing a card with a 1 SOUTH should beat the card with an A NORTH below it
    variantOneModelFallenAce.placeCard(3, 0, skyWhale); // Red play

    /*
     SkyWhale:
      4
     9 9          // Owned by Red
      1
     HeroKnight:
      A
     4 4          // Owned by Red -> Captured with 1 vs A
      2
    */

    Cell firstCell = this.bigGrid[3][0];
    Card cardAtFirst = firstCell.getCellCard();

    Cell secondCell = this.bigGrid[4][0];
    Card cardAtSecond = secondCell.getCellCard();

    Assert.assertEquals("Checking that BLUE owns the first cell",
            Player.RED, cardAtFirst.getOwner());
    Assert.assertEquals("Checking that BLUE owns the second cell",
            Player.RED, cardAtSecond.getOwner());
  }
}
