package cs3500.threetrios.model;

/**
 * Interface for cells, of which there are holes and CardCells.
 */
public interface Cell {
  /**
   * Checks if this cell is empty.
   *
   * @return true if this cell is empty, false otherwise
   */
  boolean isEmpty();

  /**
   * Adds a card to this cell.
   *
   * @param card the card to be placed at the cell
   */
  void addCard(Card card);


  /**
   * Gets the card at this cell.
   *
   * @return the card at the cell
   */
  Card getCellCard();

  /**
   * Gets an instance of this cell.
   *
   * @return an instance of the cell
   */
  Cell getCell();
}