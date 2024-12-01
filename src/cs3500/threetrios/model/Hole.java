package cs3500.threetrios.model;

/**
 * Represents a hole cell.
 *
 * <p>Cards cannot be placed to holes.</p>
 */
public class Hole implements Cell {
  @Override
  public boolean isEmpty() {
    return false;
  }

  /**
   * Throws an IllegalStatException because cards cannot be placed to holes.
   *
   * @param card the card to be placed at the cell
   */
  @Override
  public void addCard(Card card) {
    throw new IllegalStateException("Cannot place to a hole");
  }

  /**
   * Throws an IllegalStateException because holes never have cards.
   *
   * @return nothing, errors when called
   */
  @Override
  public Card getCellCard() {
    throw new IllegalStateException("Holes do not have cards");
  }

  @Override
  public Cell getCell() {
    return this;
  }
}