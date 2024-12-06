package cs3500.threetrios.model;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

public class VariantTwoModelTests extends ExamplarThreeTriosModel {

  /**
   * tests that it constructs properly
   * - valid vs invalid
   *
   * tests that it functions properly with same
   * tests that it function properly with plus
   * tests that both can default to the original
   * tests that plus wont work unless there are at least 2
   */

  protected VariantTwoModel sameVar;
  protected VariantTwoModel plusVar;

  public void init() {
    this.sameVar = new VariantTwoModel(bigGrid, deck, true, false, new Random(1));
    this.plusVar = new VariantTwoModel(bigGrid, deck, false, true, new Random(1));

  }

  // testing that it constructs
  @Test
  public void testValidConstruction() {
    this.init();
    Assert.assertEquals(sameVar.getGridCopy()[0][0].isEmpty(), true);
    Assert.assertEquals(plusVar.getGridCopy()[0][0].isEmpty(), true);
  }

  // testing that it doesn't construct
  @Test
  public void testInvalidConstruction() {

    Assert.assertThrows(IllegalArgumentException.class, ()->
            new VariantTwoModel(bigGrid, deck, true, true, new Random(1)));
  }

  // testing that same works
  @Test
  public void testSameWorks() throws IOException {
    this.init();

    // bvlue = [WindBird, AngryDragon, HeroKnight]
    // red = [WorldDragon, CorruptKing, SkyWhale]

    sameVar.placeCard(2, 0, worldDragon);
    Assert.assertEquals(worldDragon.getOwner(), Player.RED);

    sameVar.placeCard(1, 0, angryDragon);
    Assert.assertEquals(worldDragon.getOwner(), Player.BLUE);
  }

  // testing that the default case still works
  @Test
  public void testSameDefaults() throws IOException {
    this.init();

    // bvlue = [WindBird, AngryDragon, HeroKnight]
    // red = [WorldDragon, CorruptKing, SkyWhale]

    sameVar.placeCard(2, 0, corruptKing);
    Assert.assertEquals(corruptKing.getOwner(), Player.RED);

    sameVar.placeCard(1, 0, angryDragon);
    Assert.assertEquals(corruptKing.getOwner(), Player.BLUE);
  }


}
