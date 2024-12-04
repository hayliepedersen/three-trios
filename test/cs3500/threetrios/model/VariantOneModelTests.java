package cs3500.threetrios.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class VariantOneModelTests extends ExamplarThreeTriosModel {
  protected VariantOneModel variantOneModelReverse;
  @Before
  public void setup() {
    super.setup();

    // Model where only reverse rule is applied
    this.variantOneModelReverse = new VariantOneModel(bigGrid, deck, true, false,
            new Random(1));
  }

  @Test
  public void testBattlePhaseWithComboStep() throws IOException {
    // Checking player's hands with random seed
    Assert.assertEquals("Checking red player's hand is properly initialized",
            List.of(worldDragon, corruptKing, skyWhale), variantOneModelReverse.getRedHand());
    Assert.assertEquals("Checking blue player's hand is properly initialized",
            List.of(windBird, angryDragon, heroKnight), variantOneModelReverse.getBlueHand());

    // Ensue gameplay
    variantOneModelReverse.placeCard(0, 0, worldDragon);
    variantOneModelReverse.placeCard(1, 0, angryDragon);

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
}
