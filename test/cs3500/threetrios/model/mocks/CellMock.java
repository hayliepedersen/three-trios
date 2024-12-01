package cs3500.threetrios.model.mocks;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Cell;

/**
 * Represents a mock of the Cell class.
 */
public class CellMock implements Cell {

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public void addCard(Card card) {
    // Do nothing
  }

  @Override
  public Card getCellCard() {
    return new CardMock();
  }

  @Override
  public Cell getCell() {
    return this;
  }
}
