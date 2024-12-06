package cs3500.threetrios.model;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
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
  protected Cell[][] nineGrid;

  private Card a;
  private Card b;
  private Card c;
  private Card d;
  private Card e;
  private Card f;

  public void init() {
    this.sameVar = new VariantTwoModel(bigGrid, deck, true, false, new Random(1));

    this.a = new CardModel("A", "5", "4", "3", "A");
    this.b = new CardModel("B", "5", "5", "3", "A");
    this.c = new CardModel("C", "6", "5", "3", "A");
    this.d = new CardModel("D", "5", "5", "3", "7");
    this.e = new CardModel("E", "5", "5", "1", "A");
    this.f = new CardModel("F", "A", "2", "A", "A");

    this.nineGrid = new Cell[3][3];
    this.nineGrid[0][0] = new Hole();
    this.nineGrid[0][1] = new CardCell();
    this.nineGrid[0][2] = new Hole();
    this.nineGrid[1][0] = new CardCell();
    this.nineGrid[2][0] = new Hole();
    this.nineGrid[1][1] = new CardCell();
    this.nineGrid[1][2] = new CardCell();
    this.nineGrid[2][1] = new CardCell();
    this.nineGrid[2][2] = new Hole();

    this.deck = List.of(a, b, c, d, e, f);

    this.plusVar = new VariantTwoModel(nineGrid, deck, false, true, new Random(2));

  }

  // testing that it constructs
  @Test
  public void testValidConstruction() {
    this.init();
    Assert.assertEquals(sameVar.getGridCopy()[0][0].isEmpty(), true);
    Assert.assertEquals(plusVar.getGridCopy()[0][1].isEmpty(), true);
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

  @Test
  public void testPlusWorks() throws IOException {
    this.init();
    // red = [F, B, C]
    // blue = [A, E, D]
    plusVar.placeCard(0, 1, b);
    plusVar.placeCard(1, 0, e);
    plusVar.placeCard(2, 1, c);

    Assert.assertEquals(b.getOwner(), Player.RED);
    plusVar.placeCard(1, 1, a);
    Assert.assertEquals(b.getOwner(), Player.BLUE);
  }

}
