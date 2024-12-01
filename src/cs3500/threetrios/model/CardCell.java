package cs3500.threetrios.model;

/**
 * Represents a card cell in a game of Three Trios.
 *
 * <p>Card cells can be empty or contain a card. All card cells start out as empty</p>
 */
public class CardCell implements Cell {
  private boolean isEmpty;
  private Card cellCard;

  /**
   * constructor for CardCell that initializes as empty.
   */
  public CardCell() {
    // All card cells start out as empty
    this.isEmpty = true;
    this.cellCard = null;
  }

  @Override
  public boolean isEmpty() {
    return isEmpty;
  }

  @Override
  public void addCard(Card card) {
    this.isEmpty = false;
    this.cellCard = card;
  }

  @Override
  public Card getCellCard() {
    return this.cellCard;
  }

  @Override
  public Cell getCell() {
    CardCell copy = new CardCell();

    copy.isEmpty = this.isEmpty;
    copy.cellCard = this.cellCard;

    return copy;
  }
}