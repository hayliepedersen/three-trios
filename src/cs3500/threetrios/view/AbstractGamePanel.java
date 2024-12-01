package cs3500.threetrios.view;

import java.awt.Graphics2D;
import java.awt.Color;
import java.util.HashMap;
import javax.swing.JPanel;
import cs3500.threetrios.model.Card;

/**
 * Represents an abstract panel class to abstract similarities from the panel subclasses.
 */
public abstract class AbstractGamePanel extends JPanel {

  /**
   * Draws the card directions onto a card.
   *
   * @param card the card to draw from
   * @param g2d the graphics
   * @param x the x coordinate
   * @param y the y coordinate
   */
  protected void drawCardDirections(Card card, Graphics2D g2d, int x, int y) {
    g2d.fillRect(x, y, getCellSize(), getCellSize());

    // Fill the cell background with player color if a card is present
    HashMap<String, HashMap<String, Integer>> directions = card.setCardDirections();
    String cardName = card.getName();

    // Retrieve the directions for the current card
    HashMap<String, Integer> cardDirections = directions.get(cardName);

    g2d.setColor(Color.BLACK);

    if (cardDirections != null) {
      // Draw the "North" value at the top-center
      if (cardDirections.containsKey("North")) {
        g2d.drawString(displayDirectionValue(cardDirections, "North"),
                x + getCellSize() / 2 - 5, y + 15);
      }

      // Draw the "South" value at the bottom-center
      if (cardDirections.containsKey("South")) {
        g2d.drawString(displayDirectionValue(cardDirections, "South"),
                x + getCellSize() / 2 - 5, y + getCellSize() - 10);
      }

      // Draw the "West" value at the left-center
      if (cardDirections.containsKey("West")) {
        g2d.drawString(displayDirectionValue(cardDirections, "West"),
                x + 5, y + getCellSize() / 2 + 5);
      }

      // Draw the "East" value at the right-center
      if (cardDirections.containsKey("East")) {
        g2d.drawString(displayDirectionValue(cardDirections, "East"),
                x + getCellSize() - 15, y + getCellSize() / 2 + 5);
      }
    }
  }

  /**
   * Gets the cell sizes as it differs between subclasses.
   */
  protected abstract int getCellSize();

  /**
   * Displays the direction value, and A if direction value is 10.
   *
   * @param cardDirections the map to read from
   * @param direction the direction key
   * @return the direction value as a string
   */
  private String displayDirectionValue(HashMap<String, Integer> cardDirections, String direction) {
    String value = String.valueOf(cardDirections.get(direction));

    if (value.equals("10")) {
      return "A";
    }
    return value;
  }
}
