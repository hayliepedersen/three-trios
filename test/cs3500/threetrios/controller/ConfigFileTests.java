package cs3500.threetrios.controller;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.Hole;

/**
 * Represents a class for testing the configuration files.
 */
public class ConfigFileTests {
  File file;
  GridConfigurationFile gridConfig;
  CardDatabaseFile cardData;
  String path;

  /**
   * testing that the grid config file makes a grid correctly.
    */
  @Test
  public void gridInitTesting() {
    path = "docs" + File.separator + "gridCanWalk.txt";
    file = new File(path);
    gridConfig = new GridConfigurationFile(file);

    Cell[][] grid = gridConfig.makeGrid();

    Assert.assertTrue(grid[2][0] instanceof Hole);
  }

  /**
   * testing that a card database file makes a deck correctly.
   */
  @Test
  public void cardInitTesting() {
    path = "docs" + File.separator + "allCards.txt";
    file = new File(path);
    cardData = new CardDatabaseFile(file);

    Assert.assertEquals(cardData.makeDeck().size(), 10);
  }

  /**
   * testing that a card database must be given unique cards.
   */
  @Test
  public void duplicateCardError() {
    path = "docs" + File.separator + "duplicateCards.txt";
    file = new File(path);
    cardData = new CardDatabaseFile(file);

    Assert.assertThrows(IllegalArgumentException.class,
        () -> cardData.makeDeck());
  }

  /**
   * testing that a card database must be given 5 strings,
   * a name followed by attack values.
   */
  @Test
  public void invalidCardFormatError() {
    path = "docs" + File.separator + "invalidCardFormat.txt";
    file = new File(path);
    cardData = new CardDatabaseFile(file);

    Assert.assertThrows(IllegalArgumentException.class,
        () -> cardData.makeDeck());
  }

  /**
   * testing that a grid config cannot have more rows than the given dimension.
   */
  @Test
  public void tooManyRows() {
    path = "docs" + File.separator + "tooManyRows.txt";
    file = new File(path);
    gridConfig = new GridConfigurationFile(file);

    Assert.assertThrows(IllegalArgumentException.class,
        () -> gridConfig.makeGrid());
  }

  /**
   * testing that a grid config cannot have more cols than the given dimension.
   */
  @Test
  public void tooManyCols() {
    path = "docs" + File.separator + "tooManyCols.txt";
    file = new File(path);
    gridConfig = new GridConfigurationFile(file);

    Assert.assertThrows(IllegalArgumentException.class,
        () -> gridConfig.makeGrid());
  }

  /**
   * testing that a grid config cannot have less rows than the given dimension.
   */
  @Test
  public void tooFewRows() {
    path = "docs" + File.separator + "tooFewRows.txt";
    file = new File(path);
    gridConfig = new GridConfigurationFile(file);

    Assert.assertThrows(IllegalArgumentException.class,
        () -> gridConfig.makeGrid());
  }

  /**
   * testing that a grid config cannot have less cols than the given dimension.
   */
  @Test
  public void tooFewCols() {
    path = "docs" + File.separator + "tooFewCols.txt";
    file = new File(path);
    gridConfig = new GridConfigurationFile(file);

    Assert.assertThrows(IllegalArgumentException.class,
        () -> gridConfig.makeGrid());
  }

  /**
   * testing that a config file cannot be given an invalid file.
   */
  @Test
  public void falseFlie() {
    path = "docs" + File.separator + "tooFewCols.txt";
    file = new File(path);
    Assert.assertThrows(NullPointerException.class,
        () -> new GridConfigurationFile(null));
  }

  /**
   * testing that a grid config cannot be given invalid rows and columns.
   */
  @Test
  public void invalidDimensions() {
    path = "docs" + File.separator + "invalidDimensions.txt";
    file = new File(path);
    gridConfig = new GridConfigurationFile(file);

    Assert.assertThrows(IllegalArgumentException.class,
        () -> gridConfig.makeGrid());
  }



}
