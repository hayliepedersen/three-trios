package cs3500.threetrios.view;

import java.awt.*;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Player;

public interface ViewDecorator {

  /**
   * Shows hints based on the selected card.
   *
   * @param row the row index of each card on the grid
   * @param col the col index of each card on the grid
   * @param x the x coordinate
   * @param y the y coordinate
   * @param g2d the graphics to work with
   */
  void showHints(int row, int col, int x, int y, Graphics2D g2d);

  /**
   * Shows the hints for the given player.
   *
   * @param blueEnabled true if blue player's hints were enabled
   * @param redEnabled true if red player's hints were enabled
   */
  void showHintsVisibility(boolean blueEnabled, boolean redEnabled);
}
