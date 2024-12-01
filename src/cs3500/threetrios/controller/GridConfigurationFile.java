package cs3500.threetrios.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Objects;
import java.util.Scanner;

import cs3500.threetrios.model.CardCell;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.Hole;

/**
 * A class to configure the grid given a grid configuration file.
 */
public class GridConfigurationFile {
  private final FileReader readFile;

  /**
   * Constructor for this class which takes in a file.
   *
   * <p>The constructor tests that the file can be passed into a FileReader.</p>
   * @param file the file to pass in
   */
  public GridConfigurationFile(File file) {
    Objects.requireNonNull(file);
    try {
      readFile = new FileReader(file);
    }
    catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File path or type is invalid");
    }
  }

  /**
   * Makes the grid from the config file.
   *
   * @return grid the grid to initialize the model with
   */
  public Cell[][] makeGrid() {
    Scanner scan = new Scanner(readFile);
    String info = "";
    Cell[][] grid = initGrid(scan);

    int currRow = 0;
    int totalCols = grid[0].length;

    while (scan.hasNext()) {
      if (currRow > grid.length) {
        throw new IllegalArgumentException("File has more than the specified rows");
      }
      grid = updateGrid(scan, grid, currRow, totalCols);

      currRow += 1;
    }

    if (currRow < grid.length) {
      throw new IllegalArgumentException("File did not specify all rows");
    }

    return grid;
  }

  /**
   * Initializes the grid.
   *
   * @param scan the scanner to read from the file
   * @return grid the grid to be initialized
   */
  private static Cell[][] initGrid(Scanner scan) {
    int row = 0;
    int col = 0;
    String info = "";

    for (int rowsCols = 1; rowsCols <= 2; rowsCols++) {
      try {
        info = scan.next();
      }
      catch (IllegalStateException e) {
        throw new IllegalArgumentException("the first line must specify rows and columns");
      }

      if (rowsCols == 1) {
        row = Integer.parseInt(info);
      }
      if (rowsCols == 2) {
        col = Integer.parseInt(info);
      }
    }

    Cell[][] grid = new Cell[row][col];

    return grid;
  }

  /**
   * Updates the given grid with the parameters.
   *
   * @param scan the scanner to read the file with
   * @param grid the grid to be updated
   * @param currRow the current number of rows
   * @param totalCols the total number opf columns
   * @return grid the grid after updating
   */
  private static Cell[][] updateGrid(Scanner scan, Cell[][] grid,
                                     int currRow, int totalCols) {
    String info = "";
    try {
      info = scan.next();
    }
    catch (IllegalStateException e) {
      throw new IllegalArgumentException("File not formatted properly");
    }

    if (totalCols != info.length()) {
      throw new IllegalArgumentException("Given string has too many/little characters");
    }

    for (int currentCol = 0; currentCol < info.length(); currentCol++) {
      if (info.charAt(currentCol) == 'C') {
        grid[currRow][currentCol] = new CardCell();
      }
      else if (info.charAt(currentCol) == 'X') {
        grid[currRow][currentCol] = new Hole();
      }
      else {
        throw new IllegalArgumentException("input must be X or C");
      }
    }

    return grid;
  }

}
