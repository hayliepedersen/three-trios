package cs3500.threetrios.view;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CardCell;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.Hole;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.ReadOnlyTriosModel;

/**
 * Represents a decorator for showing hints in the ThreeTrios game.
 */
public class HintDecorator extends GridPanel {
  private final Player player;
  private boolean hintsEnabledRed;
  private boolean hintsEnabledBlue;

  /**
   * Constructs this hint decorator.
   *
   * @param model the model to display
   * @param player the active player
   * @param gridPanel the grid panel to decorate
   */
  public HintDecorator(ReadOnlyTriosModel model, Player player, GridPanel gridPanel) {
    super(model);
    this.player = player;

    this.hintsEnabledBlue = false;
    this.hintsEnabledRed = false;

    MouseClick mouseListener = new MouseClick();
    this.addMouseListener(mouseListener);

    this.setFocusable(true);
    this.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        // Using 'H' to toggle hints
        if (e.getKeyCode() == KeyEvent.VK_H && player.equals(Player.RED)) {
          hintsEnabledRed = !hintsEnabledRed;
          repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_H && player.equals(Player.BLUE)) {
          hintsEnabledBlue = !hintsEnabledBlue;
          repaint();
        }
      }
    });
  }

  @Override
  public void paintComponent(Graphics g) {
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

          if (hintsEnabledRed && this.player.equals(Player.RED)) {
            this.showHints(row, col, x, y, g2d);
          } else if (hintsEnabledBlue && this.player.equals(Player.BLUE)) {
            this.showHints(row, col, x, y, g2d);
          }
        }
      }
    }
  }

  private void showHints(int row, int col, int x, int y, Graphics2D g2d) {
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
}
