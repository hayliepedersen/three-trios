package cs3500.threetrios.view;

import java.awt.*;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.ReadOnlyTriosModel;

public interface ViewDecoartor {

  /**
   * shows hints based on the selected card.
   *
   * @param row the row index of each card on the grid
   * @param col the col index of each card on the grid
   * @param model the model to check with
   * @param selectedCard the card selected by the player
   * @param x the x coord
   * @param y the y coord
   * @param g2d the graphics to work with
   */
  public void showHints(int row, int col, ReadOnlyTriosModel model,
                        Card selectedCard, int x, int y, Graphics2D g2d);
}
