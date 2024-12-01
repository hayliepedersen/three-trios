package cs3500.threetrios.strategy;

import java.util.HashMap;
import java.util.List;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.TriosModel;

/**
 * A Strategy: Choose the card and position where the card is least likely to be flipped
 * by the opponent. If no low-risk move is found, fall back on the default move from the
 * abstract Strategy class.
 */
public class LeastVulnerable extends Strategy implements PlayerStrategy {
  @Override
  public BestMove chooseMove(TriosModel model, Player player) {
    List<Card> hand = model.getPlayerHand(player);
    int rows = model.getRows();
    int cols = model.getCols();

    BestMove bestMove = null;
    int minFlipRisk = Integer.MAX_VALUE;

    // Iterate over each card in hand
    for (Card card : hand) {
      HashMap<String, HashMap<String, Integer>> directions = card.setCardDirections();
      String cardName = card.getName();

      // Retrieve direction values for this card
      HashMap<String, Integer> cardDirections = directions.get(cardName);

      // Check each position on the board
      for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
          // Ensure the position is a legal play
          if (model.isLegalPlay(row, col)) {
            int flipRisk = calculateFlipRisk(row, col, cardDirections, model, player);

            // Update the best move if this move has a lower flip risk
            if (flipRisk < minFlipRisk) {
              minFlipRisk = flipRisk;
              bestMove = new BestMove(row, col, card);
            }
          }
        }
      }
    }

    // If no low-risk move is found, fall back to the default move
    if (bestMove == null) {
      bestMove = defaultMove(model, player);
    }

    return bestMove;
  }
}
