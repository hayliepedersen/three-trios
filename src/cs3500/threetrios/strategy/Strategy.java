package cs3500.threetrios.strategy;

import java.util.HashMap;
import java.util.List;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.TriosModel;

/**
 * Represents an abstract class for game strategies.
 */
public abstract class Strategy implements PlayerStrategy {

  /**
   * Chooses the best move for the specific subclass's strategy.
   *
   * @param model the model
   * @param player the player that is playing
   * @return the best move
   */
  public abstract BestMove chooseMove(TriosModel model, Player player);

  @Override
  public BestMove defaultMove(TriosModel model, Player player) {
    int[] bestPosition = {0, 0};
    Card bestCard = null;
    boolean legalCardFound = false;

    for (Card card : model.getPlayerHand(player)) {
      for (int i = 0; i < model.getRows(); i++) {
        for (int j = 0; j < model.getCols(); j++) {
          if (model.isLegalPlay(i, j) && !legalCardFound) {
            bestPosition = new int[]{i, j};
            bestCard = card;

            legalCardFound = true;
          }
        }
      }
    }

    return new BestMove(bestPosition[0], bestPosition[1], bestCard);
  }

  /**
   * Calculates the defensive score of a card for a specific corner.
   *
   * @param cornerIndex The index of the corner (0 for top-left, 1 for top-right, etc.)
   * @param directions  A map of the card's direction values.
   * @return The calculated defensive score.
   */
  protected int calculateDefenseScore(int cornerIndex, HashMap<String, Integer> directions) {
    switch (cornerIndex) {
      case 0: // Top-left corner: only South and East are exposed
        return directions.get("South") + directions.get("East");
      case 1: // Top-right corner: only South and West are exposed
        return directions.get("South") + directions.get("West");
      case 2: // Bottom-left corner: only North and East are exposed
        return directions.get("North") + directions.get("East");
      case 3: // Bottom-right corner: only North and West are exposed
        return directions.get("North") + directions.get("West");
      default:
        return 0;
    }
  }

  /**
   * Calculates the flip risk of placing a card at a specific position.
   * The risk is based on how many directions this card could be flipped by opponent cards.
   *
   * @param row            the row position.
   * @param col            the column position.
   * @param cardDirections a map of the card's direction values.
   * @param model          the game model.
   * @param player         the current player.
   * @return the calculated flip risk score.
   */
  protected int calculateFlipRisk(int row, int col, HashMap<String, Integer> cardDirections,
                                  TriosModel model, Player player) {
    int flipRisk = 0;
    Player opponent = getOpponent(player);
    List<Card> opponentHand = model.getPlayerHand(opponent);

    // For each direction, check if any opponent card could potentially flip the card
    for (Card opponentCard : opponentHand) {
      HashMap<String, HashMap<String, Integer>> opponentDirections =
              opponentCard.setCardDirections();
      String opponentCardName = opponentCard.getName();
      HashMap<String, Integer> oppCardDirections = opponentDirections.get(opponentCardName);

      // Opponent's north position
      if (row > 0 && model.getCellOwner(row - 1, col) == opponent) {
        if (oppCardDirections.get("South") > cardDirections.get("North")) {
          flipRisk++;
        }
      }
      // Opponent's south position
      if (row < model.getRows() - 1 && model.getCellOwner(row + 1, col) == opponent) {
        if (oppCardDirections.get("North") > cardDirections.get("South")) {
          flipRisk++;
        }
      }
      // Opponent's west position
      if (col > 0 && model.getCellOwner(row, col - 1) == opponent) {
        if (oppCardDirections.get("East") > cardDirections.get("West")) {
          flipRisk++;
        }
      }
      // Opponent's east position
      if (col < model.getCols() - 1 && model.getCellOwner(row, col + 1) == opponent) {
        if (oppCardDirections.get("West") > cardDirections.get("East")) {
          flipRisk++;
        }
      }
    }

    return flipRisk;
  }

  /**
   * Gets the opponent, the opposite player.
   *
   * @param player the current player
   * @return the opposing player
   */
  private Player getOpponent(Player player) {
    Player opponent;

    if (player == Player.RED) {
      opponent = Player.BLUE;
    } else {
      opponent = Player.RED;
    }

    return opponent;
  }
}
