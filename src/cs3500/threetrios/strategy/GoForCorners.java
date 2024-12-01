package cs3500.threetrios.strategy;

import java.util.HashMap;
import java.util.List;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.TriosModel;

/**
 * A Strategy: Choose the card and corner where that card will have the highest defense score
 * based off of its exposed sides (a card in a corner only has two sides exposed)
 * out of all the cards in that player's hand.
 *
 * <p>For example, a card with a south of 3, east of 2, north of
 * 6, and west of 5 should be placed in the bottom right corner because 6 + 5 = 11,
 * making it the highest defense score out of all cards when its north and west are exposed</p>
 */
public class GoForCorners extends Strategy implements PlayerStrategy {
  @Override
  public BestMove chooseMove(TriosModel model, Player player) {
    List<Card> hand = model.getPlayerHand(player);
    int rows = model.getRows();
    int cols = model.getCols();

    int[][] corners = {
            {0, 0},                    // Top-left corner
            {0, cols - 1},             // Top-right corner
            {rows - 1, 0},             // Bottom-left corner
            {rows - 1, cols - 1}       // Bottom-right corner
    };

    BestMove bestMove = null;
    int maxDefenseScore = -1;

    for (Card card : hand) {
      HashMap<String, HashMap<String, Integer>> directions = card.setCardDirections();
      String cardName = card.getName();

      // Retrieve the directions for the current card
      HashMap<String, Integer> cardDirections = directions.get(cardName);

      // Calculate defense score for each corner
      for (int i = 0; i < corners.length; i++) {
        int row = corners[i][0];
        int col = corners[i][1];

        // Check if this corner position is a legal play
        if (model.isLegalPlay(row, col)) {
          int defenseScore = calculateDefenseScore(i, cardDirections);

          // If this is the highest score found, update the best move
          if (defenseScore > maxDefenseScore) {
            maxDefenseScore = defenseScore;
            bestMove = new BestMove(row, col, card);
          }
        }
      }
    }

    // If no corner move is found, return default move
    if (bestMove == null) {
      bestMove = defaultMove(model, player);
    }

    return bestMove;
  }
}
