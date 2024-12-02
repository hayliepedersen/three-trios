package cs3500.threetrios.view;

import java.awt.Font;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import cs3500.threetrios.controller.ViewFeatures;
import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CardCell;
import cs3500.threetrios.model.CardModel;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.Hole;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.ReadOnlyTriosModel;

/**
 * A grid panel that represents the grid in the GUI.
 */
public class GridPanel extends AbstractGamePanel implements ViewDecorator {
  private final int cellSize;
  private final ReadOnlyTriosModel model;
  private final Player player;
  private ViewFeatures viewFeatures;
  private boolean hintsEnabledRed;
  private boolean hintsEnabledBlue;

  /**
   * Constructor for GridPanel that handles MouseClicks.
   *
   * @param model ReadOnlyTrioModel to build the GUI off of.
   */
  public GridPanel(ReadOnlyTriosModel model, Player player) {
    int rows = model.getGridCopy().length;
    int cols = model.getGridCopy()[0].length;
    this.cellSize = 550 / rows;
    this.model = model;
    this.hintsEnabledBlue = false;
    this.hintsEnabledRed = false;
    this.player = player;

    MouseClick mouseListener = new MouseClick();
    this.addMouseListener(mouseListener);
    this.setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
  }

  /**
   * Sets the features for the grid panel.
   *
   * @param viewFeatures to add to this panel
   */
  public void setFeatures(ViewFeatures viewFeatures) {
    this.viewFeatures = viewFeatures;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    Cell[][] grid = model.getGridCopy();
    Font font = new Font("Arial", Font.BOLD, 16);
    g2d.setFont(font);

    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[row].length; col++) {
        int x = col * cellSize;
        int y = row * cellSize;
        Cell cell = grid[row][col];

        if (cell instanceof CardCell && !cell.isEmpty()) {
          Card card = cell.getCellCard();

          Color lightBlue = new Color(72,173,254,255);
          g2d.setColor(card.getOwner() == Player.RED ? Color.PINK : lightBlue);

          drawCardDirections(card, g2d, x, y);
        }
        else {
          if (cell instanceof Hole) {
            g2d.setColor(Color.LIGHT_GRAY);
          }
          else {
            g2d.setColor(Color.YELLOW);
          }

          g2d.fillRect(x, y, cellSize, cellSize);
          g2d.setColor(Color.BLACK);
          g2d.drawRect(x, y, cellSize, cellSize);

          // TODO: Is this "proper design"?
          if (this.player.equals(Player.RED) && hintsEnabledRed) {
            this.showHints(row, col, x, y, g2d);
          } else if (this.player.equals(Player.BLUE) && hintsEnabledBlue) {
            this.showHints(row, col, x, y, g2d);
          }
        }
      }
    }
  }

  @Override
  public void showHints(int row, int col, int x, int y, Graphics2D g2d) {
    if (viewFeatures.selectedCard() == null) {
      return;
    }

    Cell cell = model.getGridCopy()[row][col];

    if (cell instanceof CardCell) {
      g2d.setColor(Color.BLACK);
      int numFlips = model.numOfFlippableCards(row, col, viewFeatures.selectedCard());
      g2d.drawString(Integer.toString(numFlips),
              x + cellSize / 10, (int) (y + cellSize * 0.9));
    }
  }

  @Override
  public void showHintsVisibility(boolean blueEnabled, boolean redEnabled) {
    if (blueEnabled) {
      hintsEnabledBlue = true;
    } else if (redEnabled) {
      hintsEnabledRed = true;
    }
  }

  @Override
  protected int getCellSize() {
    return this.cellSize;
  }

  /**
   * Inner class MouseClick that updates the GUI based on which card was clicked.
   */
  public class MouseClick extends MouseAdapter {

    /**
     * Logs the coordinates of the grid cell that is clicked.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {
      int x = e.getPoint().x;
      int y = e.getPoint().y;

      int row = whichCoord(y);
      int col = whichCoord(x);
      viewFeatures.panelPrint(row, col, -5, "n/a");

      if (viewFeatures.selectedCard() != null) {
        try {
          viewFeatures.cardPlaced(row, col, viewFeatures.selectedCard());
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
      }

      repaint();
    }

    /**
     * Determines which coordinate was clicked on in the gui based on input mouse click.
     *
     * @param coord the coordinate clicked on
     * @return the corresponding row
     */
    private int whichCoord(int coord) {
      int numRows = model.getRows();

      int cellSize = 550 / numRows;
      int index = -1;

      for (int i = 1; i <= numRows; i++) {
        if (coord > 550 - (cellSize * i)) {
          index = numRows - i;
          break;
        }
      }

      return index;
    }
  }
}
