package cs3500.threetrios.view;

import java.awt.Font;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import cs3500.threetrios.controller.ViewFeatures;
import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.ReadOnlyTriosModel;

/**
 * The PlayerPanel class that represents the players hand in the GUI.
 */
public class PlayerPanel extends AbstractGamePanel {
  private final Player player;
  private final int cellSize;
  private final ReadOnlyTriosModel model;
  private int index;
  private ViewFeatures viewFeatures;
  private int blueHandSize;
  private int redHandSize;

  /**
   * The constructor for PlayerPanel that handles mouse clicks.
   *
   * @param player which player holds the current hand
   * @param model  ReadOnlyTriosModel to build the gui off of
   */
  public PlayerPanel(Player player, ReadOnlyTriosModel model) {
    this.player = player;
    this.model = model;
    this.cellSize = 550 / model.getBlueHand().size();

    this.index = -1;
    this.blueHandSize = model.getBlueHand().size();
    this.redHandSize = model.getRedHand().size();

    MouseClick mouseListener = new MouseClick();
    this.addMouseListener(mouseListener);
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
    Font font = new Font("Arial", Font.BOLD, 16);
    g2d.setFont(font);

    List<Card> playerHand;

    playerHand = model.getPlayerHand(this.player);

    for (int i = 0; i < playerHand.size(); i++) {
      Card card = playerHand.get(i);
      int x = (getWidth() - cellSize) / 2;
      int y = (getHeight() - (playerHand.size() * cellSize)) / 2 + i * cellSize;

      Color lightBlue = new Color(72, 173, 254, 255);

      if (player.equals(Player.RED) && (model.getRedHand().size() < redHandSize)) {
        redHandSize -= 1;
      } else if (player.equals(Player.BLUE) && (model.getBlueHand().size() < blueHandSize)) {
        blueHandSize -= 1;
      } else if (i == index) {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRect(x, y, cellSize, cellSize);
        g2d.setStroke(new BasicStroke(1));
      }

      g2d.setColor(player == Player.RED ? Color.PINK : lightBlue);
      drawCardDirections(card, g2d, x, y);
      g2d.setColor(Color.BLACK);
      g2d.drawRect(x, y, cellSize, cellSize);
    }
  }

  @Override
  protected int getCellSize() {
    return this.cellSize;
  }

  @Override
  public Dimension getPreferredSize() {
    List<Card> playerHand = new ArrayList<>();
    if (this.player == Player.RED) {
      playerHand = model.getRedHand();
    } else if (this.player == Player.BLUE) {
      playerHand = model.getBlueHand();
    }
    return new Dimension(cellSize + 20, playerHand.size() * (cellSize + 10));
  }

  /**
   * MouseClick in PlayerPanel, updating the hand panel based on user input.
   */
  public class MouseClick extends MouseAdapter {
    /**
     * Updates the GUI based off of mouse clicks.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {
      int x = e.getPoint().x;
      int y = e.getPoint().y;
      List<Card> playerHand = model.getPlayerHand(player);
      int handSize = playerHand.size();

      int verticalOffset = (getHeight() - (handSize * cellSize)) / 2;

      for (int i = 0; i < handSize; i++) {
        int cardY = verticalOffset + i * cellSize;
        if (y >= cardY && y < cardY + cellSize) {
          if (index == i) {
            index = -1; // Deselect the card
            viewFeatures.panelPrint(-5, -5, i, player.asString);
          } else {
            index = i; // Select the card
            viewFeatures.panelPrint(-5, -5, index, player.asString);
            setSelectedCardIndex(index);
          }
          repaint();
          return;
        }
      }

      index = -1;
      repaint();
    }

    /**
     * Sets the selected card to be called back to the controller.
     *
     * @param index the hand index
     */
    public void setSelectedCardIndex(int index) {
      Card selectedCard = model.getPlayerHand(player).get(index);
      viewFeatures.cardSelected(selectedCard);
    }
  }
}
