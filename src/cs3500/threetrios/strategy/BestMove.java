package cs3500.threetrios.strategy;

import cs3500.threetrios.model.Card;

/**
 * Represents the best move as determined by one of the {@link Strategy} classes.
 */
public class BestMove {
  public final int row;
  public final int col;
  public final Card card;

  /**
   * Constructs the best move.
   *
   * @param row the best row coordinate
   * @param col the best column coordinate
   * @param card the best card
   */
  public BestMove(int row, int col, Card card) {
    this.row = row;
    this.col = col;
    this.card = card;
  }

  /**
   * Gets this card.
   *
   * @return the best move card
   */
  public Card getCard() {
    return this.card;
  }

  /**
   * Gets this row value.
   *
   * @return the row coordinate
   */
  public int getRow() {
    return this.row;
  }

  /**
   * Gets this column value.
   *
   * @return the column coordinate
   */
  public int getCol() {
    return this.col;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof BestMove)) {
      return false;
    }

    BestMove that = (BestMove) obj;

    return this.row == that.row
            && this.col == that.col
            && this.card.equals(that.card);
  }

  @Override
  public String toString() {
    return this.row
            + ", " + this.col
            + ", " + this.card.toString();
  }

  @Override
  public int hashCode() {
    return row + col;
  }
}
