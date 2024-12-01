package cs3500.threetrios.strategy;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.TriosModel;

/**
 * A Strategy: Flip as many cards on this turn as possible.
 */
public class HighestFlipNum extends Strategy implements PlayerStrategy {
  @Override
  public BestMove chooseMove(TriosModel model, Player player) {
    int maxFlipNum = 0;
    int[] bestPosition = {0, 0};
    Card bestCard = null;

    // Check every valid position on the board with each card in hand
    // First card that sets off the chain of flipping is set as bestCard
    for (Card card : model.getPlayerHand(player)) {
      for (int i = 0; i < model.getRows(); i++) {
        for (int j = 0; j < model.getCols(); j++) {
          if (model.isLegalPlay(i, j)) {
            int flips = model.numOfFlippableCards(i, j, card);
            if (flips > maxFlipNum) {
              maxFlipNum = flips;
              bestPosition = new int[]{i, j};
              bestCard = card;
            }
          }
        }
      }
    }

    BestMove bestMove = new BestMove(bestPosition[0], bestPosition[1], bestCard);

    // If no flippable cards found, return the default move
    if (maxFlipNum == 0) {
      bestMove = defaultMove(model, player);
    }

    return bestMove;
  }
}
